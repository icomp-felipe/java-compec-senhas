package compec.ufam.model;

import java.io.*;
import java.net.*;
import compec.ufam.utils.*;

/** Modelagem e implementação das funções de saída de dados do Cliente do leilão
 *  @author Felipe André
 *  @version 47.00, 14/01/2015 */
public class Cliente implements DatabaseDefaults {
	
	/** Atributos de controle da classe */
	private PrintWriter outStream;
	private Mesa mesa;
	private String hostAddress;
	private Socket hostSocket;

	/*********************************************************************************************/
	/*						BLOCO DE CONSTRUTORES												 */
	/*********************************************************************************************/
	
	/** Encadeamento de construtor setando os atrinutos */
	public Cliente(Mesa mesa, String hostAddress) {
		this(mesa, hostAddress, null);
	}
	
	/** Construtor setando os atrinutos */
	public Cliente(Mesa mesa, String hostAddress, Socket socket) {
		String addr = hostAddress.trim();
		this.mesa = mesa;
		this.hostSocket = socket;
		if (addr.equals("127.0.0.1"))
			try { this.hostAddress = Inet4Address.getLocalHost().getHostAddress(); }
			catch (UnknownHostException exception) { this.hostAddress = hostAddress; }
		else
			this.hostAddress = addr;
		if (socket != null)
			try { outStream = new PrintWriter(hostSocket.getOutputStream(), true); }
			catch (IOException exception) {}
	}
	
	/*********************************************************************************************/
	/*						GETTERS E MAIS GETTERS												 */
	/*********************************************************************************************/
	
	/** Getter para nome de usuário */
	public Mesa getMesa() {
		return mesa;
	}

	public String getMesaAsString() {
		return mesa.getMesaID();
	}
	
	/** Getter para endereço IP do cliente */
	public String getHostAddress() {
		return hostAddress;
	}

	@Override
	/** Getter formatado para a lista de usuários a ser enviada pela rede */
	public String getMessage() {
		return getMesaAsString();
	}
	
	/** Getter formatado para o nome da Thread */
	public String getThreadResume() {
		return ("Thread da Mesa " + mesa.getMesaID() + "@" + hostAddress);
	}
	
	
	/** Getter para o Socket do cliente */
	public Socket getSocket() {
		return hostSocket;
	}
	
	/*********************************************************************************************/
	/*						BLOCO DE CONTROLE DO SISTEMA										 */
	/*********************************************************************************************/
	
	/** Imprime uma mensagem na stream de saída */
	private synchronized void writeToStream(String message) {
		outStream.print(message);
		outStream.flush();
	}
	
	/** Encerra a conexão TCP */
	public boolean close() {
		try { hostSocket.close(); return true;}
		catch (IOException exception) { return false; }
	}
	
	/*********************************************************************************************/
	/*			BLOCO DE IMPLEMENTAÇÃO DAS FUNCIONALIDADES DO CLIENTE DO LEILÃO					 */
	/*********************************************************************************************/

	/** Envia uma mensagem de eco ao cliente */
	public void sendPassword(Senha senha) {
		writeToStream(senha.getMessage(true));
	}
	
	public void enviaSenha(Senha senha) {
		writeToStream(senha.getMessage(false));
	}
	
	/** Envia uma mensagem de eco ao cliente */
	public void echo() {
		writeToStream(Integer.toString(Constants.Echo.ECHO_REQUEST));
	}
	
}
