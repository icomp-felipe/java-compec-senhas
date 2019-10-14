package compec.ufam.io;

import java.io.*;
import java.net.*;
import compec.ufam.utils.*;

/** Classe que contém o modelo da implementação dos métodos de E/S do sistema
 *  @author Felipe André
 *  @version 1.00, 15/01/2015 */
public abstract class IOModel extends Thread {

	/** Atributos de controle da classe */
	protected final Socket socket;
	protected PrintWriter outStream;
	protected InputStream inStream;
	protected String separador = Constants.String.SEPARATOR;
	
	public IOModel(Socket socket) throws IOException {
		this.socket = socket;
		loadSocketStreams();
	}
	
	/** Converte um inteiro em String */
	protected String toString(int number) {
		return Integer.toString(number);
	}
	
	/** Carrega as streams de E/S do socket */
	private void loadSocketStreams() throws IOException {
		inStream = socket.getInputStream();
		outStream = new PrintWriter(socket.getOutputStream(), true);
	}
	
	/** Imprime uma mensagem na stream de saída */
	protected synchronized void writeToStream(String message) {
		String msg = (message + '\0');
		outStream.print(msg);
		outStream.flush();
	}
	
	/** Finaliza as streams de E/S do socket */
	protected void closeSocketStreams() {
		try {
			inStream.close();
			outStream.close();
			socket.close();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/** Retorna o endereço IP do cliente conectado */
	protected String getClientIP() {
		String clientIP = socket.getInetAddress().toString().replaceFirst("/","").trim();
		if (clientIP.endsWith("127.0.0.1"))
			try { clientIP = Inet4Address.getLocalHost().getHostAddress(); }
			catch (UnknownHostException exception) { }
		return clientIP;
	}
	
	/** Verifica se uma classe não é nula */
	protected boolean notNull(Object source) {
		return (source != null);
	}
	
	/** Getter para o Socket */
	public Socket getSocket() {
		return socket;
	}
	
	/** Retorna o endereço IP atual do sistema */
	public static String getLocalIP() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	/** Método abstrato que manipula as streams de E/S de rede */
	public abstract void parseInputMessage() throws IOException;
	public abstract void run();
	
}
