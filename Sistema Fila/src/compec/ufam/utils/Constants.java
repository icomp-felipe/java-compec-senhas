package compec.ufam.utils;

/** Contém as constantes utilizadas por todo o software
*	@author Felipe André
*	@version 2.5, 19/01/2015 */
public final class Constants {
	
	/** Define as constantes principais para controle de String */
	public final class String {
		public static final java.lang.String SEPARATOR = "#";
		public static final char EOL = 0x0;
	}
	
	/** Define as constantes de controle de portas de serviÃ§o */
	public final class Ports {
		public static final int PASSWD_PORT = 5000;
	}
	
	/** Define as constantes de controle de login */
	public final class Login {
		public static final int TRY_LOGIN    = 11;
		public static final int OK_RET_LOGIN = 12;
		public static final int DENIED_LOGIN = 13;
		public static final int REQUEST_END  = 14;
	}

	/** Define as constantes de envio de lote */
	public static class Passwd {
		public static final int REQUEST_PWD  = 21;
		public static final int RETURN_PWD	 = 22;
	}
	
	/** Defina as constantes das mensagens de eco */
	public static class Echo {
		public static final int ECHO_REQUEST = 71;
		public static final int ECHO_REPLY	 = 72;
		public static final int ATTENTION 	 = 73;
	}
	
}
