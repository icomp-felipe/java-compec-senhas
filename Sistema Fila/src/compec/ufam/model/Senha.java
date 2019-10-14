package compec.ufam.model;

import compec.ufam.utils.Constants;
import compec.ufam.utils.StringUtils;

public class Senha {

	private String separador = Constants.String.SEPARATOR;
	private Mesa mesa;
	private int senha;
	
	public Senha(Mesa mesa, int senha) {
		this.mesa = mesa;
		this.senha = senha;
	}

	public String getTextFieldMesa() {
		return (mesa.getMesaAsInteger() == 0) ? "" : getMesa();
	}
	
	public String getTextFieldSenha() {
		return (mesa.getMesaAsInteger() == 0) ? "" : getSenha();
	}
	
	public String getMesa() {
		return mesa.getMesaID();
	}

	public boolean nula() {
		return (senha == 0);
	}
	
	public String getSenha() {
		return Integer.toString(senha);
	}
	
	public int getSenhaAsInteger() {
		return senha;
	}

	public int getMesaAsInteger() {
		return mesa.getMesaAsInteger();
	}
	
	public String getResume() {
		return "Senha: " + senha + " - Mesa: " + mesa.getMesaID() + "\n";
	}
	
	public String getMessage(boolean isReturn) {
		int code;
		if (isReturn)
			code = Constants.Passwd.RETURN_PWD;
		else
			code = Constants.Login.OK_RET_LOGIN;
		String message = code + separador + getSenha() + separador + getMesa();
		return message;
	}
	
	public String getLog() {
		return String.format("[INFO] Senha %s chamada pela Mesa %s em %s",senha,mesa.getMesaID(),StringUtils.getCurrentDate());
	}
	
}
