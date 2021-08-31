package controller;

public class Sexo {
	
	private String Sexo;
	

	public Sexo(String sexo) {
		Sexo = sexo;
	}

	public String getSexo() {
		return Sexo;
	}

	@Override
	public String toString() {
		return getSexo();
	}
	
}
