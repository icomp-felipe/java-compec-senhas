package compec.ufam.io;

import java.io.*;
import java.net.*;
import compec.ufam.parsers.*;
import compec.ufam.server.*;
import compec.ufam.utils.*;

public class IOServer extends IOModel {

	/** Atributos de controle da classe */
	private final TelaServidor serverScreen;
	private String clientIP;
	
	public IOServer(Socket socket, TelaServidor serverScreen) throws IOException {
		super(socket);
		this.serverScreen = serverScreen;
		clientIP = getClientIP();
		setName("Thread do Cliente: " + clientIP);
		start();
	}

	/*********************************************************************************************/
	/*						BLOCO DE CONTROLE DO SISTEMA										 */
	/*********************************************************************************************/
	
	@Override
	/** Loop fica escutando as mensagens de entrada do sistema */
	public void run() {
		while (!isInterrupted()) {
			try { parseInputMessage(); }
			catch (IOException exception) {	encerraConexao(); }
		}
		closeSocketStreams();
	}
	
	/** Finaliza a conexão com o cliente */
	private void encerraConexao() {
		try { socket.close(); }
		catch (IOException exception) { exception.printStackTrace(); }
		finally { finalizaSessao(); }
		
	}
	
	/** Executa as etapas finais da desconexão de um cliente */
	private void finalizaSessao() {
		System.out.println("A conexão com " + clientIP + " foi encerrada!");
		serverScreen.remUsuario(socket);
		interrupt();
	}
	
	/** Getter para o IP */
	public String getIP() {
		return clientIP;
	}
	
	/*********************************************************************************************/
	/*						BLOCO DE PROCESSAMENTO DE MENSAGENS DO CLIENTE						 */
	/*********************************************************************************************/
	
	@Override
	/** Manipula as streams de E/S de rede */
	public void parseInputMessage() throws IOException {
		
		String inputQuery = StringUtils.readStreamBuffer(inStream);
		
		if (inputQuery == null)
			throw new IOException("Conexão com o cliente encerrada!");
		
		String response = ServerStringParser.parse(serverScreen, inputQuery, this);
		
		if (notNull(response))
			writeToStream(response);
	}
	
}
