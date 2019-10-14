package compec.ufam.parsers;

import compec.ufam.utils.*;

/** Classe auxiliar que contém a implementação de métodos
 *  úteis de manipulação das mensagens do sistema
 *  @author Felipe André
 *  @version 3.00, 09/01/2015 */
public abstract class DefaultStringParser {
	
	/** Linkando o separador universal das mensagens de sistema */
	protected static String separador = Constants.String.SEPARATOR;

	/** MÃ©todo de debug das mensagens do sistema */
	protected static void debug(String string) {
		System.out.println(string);
	}
	
	/** MÃ©todo utilizado para exibir a mensagem literal proveniente
	 *  das streams do cliente */
	protected static String formatString(String string) {
		return string.replaceAll(Constants.String.SEPARATOR,"\\\\n") + "\"";
	}
	
	/** Converte um inteiro em String */
	protected static String toString(int number) {
		return Integer.toString(number);
	}
	
	/** Retorna o ID da mensagem */
	protected static int getMessageID(String inputStream) {
		try { return Integer.parseInt(inputStream.split(Constants.String.SEPARATOR)[0]); }
		catch (NumberFormatException exception) { return -1; }
	}
	
}
