package compec.ufam.parsers;

import java.io.*;
import java.net.*;
import compec.ufam.io.*;
import compec.ufam.model.*;
import compec.ufam.server.*;
import compec.ufam.utils.*;

public class ServerStringParser extends DefaultStringParser {
	
	/** Método que realiza a multiplexação das tarefas do servidor */
	public static String parse(TelaServidor server, String inputStream, IOServer stream) throws IOException {
		String IP = stream.getIP();
		int port = ( (InetSocketAddress) stream.getSocket().getRemoteSocketAddress()).getPort();
		
		int messageID = getMessageID(inputStream);
		
		if ((messageID != Constants.Login.TRY_LOGIN) && (messageID != Constants.Login.REQUEST_END))
			if (!server.isLogged(IP)) {
				debug("Comando de usuário não logado rejeitado!\nInfos -> IP: " + IP + ", Remote Port: " + port + ", Command: \"" + formatString(inputStream));
				return null;
			}

		/** Multiplexação de Tarefas */
		switch (messageID) {
		
			/** Tratamento de Login */
			case Constants.Login.TRY_LOGIN:
				return tryLogin(server, inputStream, IP, stream);
			
			/** Encerra a conexão com o servidor */
			case Constants.Login.REQUEST_END:
				throw new IOException("O cliente " + server.getUsernameByIP(IP) + " se desconectou!");
				
			case Constants.Passwd.REQUEST_PWD:
				return server.getStatus(IP);
			
			case Constants.Echo.ATTENTION:
				server.chamarAtencao();
				return null;
				
				/** Processa uma resposta do cliente */
			case Constants.Echo.ECHO_REPLY:
				server.echoReply(IP);
				return null;
				
			/** Tratamento de mensagens erradas */
			default:
				debug("Comando de " + server.getUserByIP(IP) + " não reconhecido: \"" + formatString(inputStream));
				break;
		}
		
		return null;
	}
	
	/** Processa o login do cliente solicitante */
	private static String tryLogin(TelaServidor server, String inputStream, String IP, IOServer stream) {
		try {
			Mesa mesa = new Mesa(Integer.parseInt(inputStream.split(separador)[1]));
			Cliente cliente = new Cliente(mesa, IP, stream.getSocket());
			return server.tryLogin(cliente);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
