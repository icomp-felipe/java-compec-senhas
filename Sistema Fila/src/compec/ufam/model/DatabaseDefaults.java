package compec.ufam.model;

import compec.ufam.utils.Constants;

/** Interface que auxilia na montagem das mensagens do sistema
 *  @author Felipe André
 *  @version 47.00, 14/01/2015 */
@FunctionalInterface
public interface DatabaseDefaults {
	
	/** Montagem da mensagem a ser transmitida */
	public String separador = Constants.String.SEPARATOR;
	public String getMessage();

}
