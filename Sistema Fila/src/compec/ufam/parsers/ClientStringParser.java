package compec.ufam.parsers;

import javax.swing.*;

import com.phill.libs.ui.AlertDialog;

import compec.ufam.client.*;
import compec.ufam.model.*;
import compec.ufam.utils.*;

public class ClientStringParser extends DefaultStringParser {
	
	/** Método que realiza a multiplexação das tarefas do cliente */
	public static void parse(JFrame frame, String inputStream) {
		int messageID = getMessageID(inputStream);
		
		/** Multiplexação de Tarefas */
		switch (messageID) {
		
			/** Bloco cuida das mensagens de Login no sistema */
			case Constants.Login.OK_RET_LOGIN:
				Senha senha = loadSenha(inputStream);
				respostaLogin(frame, senha);
				break;
			case Constants.Login.DENIED_LOGIN:
				respostaLogin(frame, null);
				break;
				
			case Constants.Passwd.RETURN_PWD:
				respostaSolicitacao(frame,inputStream);
				break;
		}
	}
	
	private static Senha loadSenha(String inputStream) {
		try {
			String[] splitter = inputStream.split(separador);
			
			Mesa mesaID = new Mesa(Integer.parseInt(splitter[2]));
			int senhaAtul = Integer.parseInt(splitter[1]);
			
			return new Senha(mesaID,senhaAtul);
		}
		catch (Exception exception) {
			return null;
		}
	}
	
	private static void respostaSolicitacao(JFrame frame, String inputStream) {
		Senha senha = loadSenha(inputStream);
		if (senha == null)
			AlertDialog.error(null, "Falha ao ler senha atual!");
		else if (frame instanceof TelaPrincipal)
			((TelaPrincipal) frame).atualizaSenha(senha);
	}

	/** Processa a resposta de login ao servidor */
	private static void respostaLogin(JFrame frame, Senha senha) {
		if (frame instanceof TelaConecta)
			((TelaConecta) frame).returnStatus(senha);
		else
			System.out.println("Frame não disponível");
	}

}
