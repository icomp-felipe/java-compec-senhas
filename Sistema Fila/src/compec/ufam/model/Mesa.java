package compec.ufam.model;

public class Mesa {

	private int mesaID;

	public Mesa(int mesaID) {
		this.mesaID = mesaID;
	}

	public int getMesaAsInteger() {
		return mesaID;
	}
	
	public String getMesaID() {
		return Integer.toString(mesaID);
	}
	
}
