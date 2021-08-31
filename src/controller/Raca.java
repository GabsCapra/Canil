package controller;

public class Raca {
	
	public String Raca;
	public int Raca_id;

	public Raca(String raca) {
		Raca = raca;
	}

	public String getRaca() {
		return Raca;
	}

	public void setRaca(String raca) {
		Raca = raca;
	}

	@Override
	public String toString() {
		return getRaca();
	}

}
