package compec.ufam.io;

import java.io.*;
import java.net.*;

import javax.swing.*;

import compec.ufam.client.TelaConecta;
import compec.ufam.client.TelaPrincipal;
import compec.ufam.parsers.*;
import compec.ufam.utils.*;

public class IOClient extends IOModel {
	
	/** Frame atualmente em execução */
	private JFrame currentScreen;

	/** Seta os atributos e inicia a escuta do socket de entrada */
	public IOClient(Socket socket, JFrame currentScreen) throws IOException {
		super(socket);
		this.currentScreen = currentScreen;
		setName("Thread de Conexão com o Servidor");
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
			catch (IOException exception) {
				if (currentScreen instanceof TelaConecta)
					((TelaConecta) currentScreen).semConexao(exception);
				else if (currentScreen instanceof TelaPrincipal)
					((TelaPrincipal) currentScreen).semConexao(exception);
				break;
			}
		}
		closeSocketStreams();
	}
	
	/** Altera a frame atual */
	public void setCurrentFrame(JFrame currentFrame) {
		this.currentScreen = currentFrame;
	}
	
	/*********************************************************************************************/
	/*						BLOCO DE PROCESSAMENTO DE MENSAGENS DO SERVIDOR						 */
	/*********************************************************************************************/
	
	@Override
	/** Manipula as streams de E/S de rede */
	public void parseInputMessage() throws IOException {
		
		String inputQuery = StringUtils.readStreamBuffer(inStream);
		
		if (inputQuery == null)
			throw new IOException("Conexão com o servidor encerrada!");
		if (inputQuery.equals(toString(Constants.Echo.ECHO_REQUEST))) {
			writeToStream(toString(Constants.Echo.ECHO_REPLY));
			return;
		}
		
		ClientStringParser.parse(currentScreen, inputQuery);
	}
	
	/*********************************************************************************************/
	/*					BLOCO DE IMPLEMENTAÇÃO DAS UTILIDADES DO SISTEMA						 */
	/*********************************************************************************************/
	
	/** Tenta se logar ao servidor */
	public void tryLogin(String userID) throws UnknownHostException, IOException {
		String query = Constants.Login.TRY_LOGIN + separador + userID;
		writeToStream(query);
	}
	
	public void chamarAtencao() {
		writeToStream(toString(Constants.Echo.ATTENTION));
	}
	
	public void solicitaSenha() {
		writeToStream(toString(Constants.Passwd.REQUEST_PWD));
	}
	
	/** Informa ao servidor sua saída do sistema */
	public void sairSistema() {
		writeToStream(toString(Constants.Login.REQUEST_END));
	}
	
}
