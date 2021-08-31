package model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cachorro {
	private int ID;
	private String Pedigree;
	private String Nome;
	private int Raca_id;
	private String Raca;
	private String NPai;
	private String NMae;
	private Date DN;
	private String Sexo;

	
	
	public Cachorro(int iD, String pedigree, String nome, int raca_id, String raca, String nPai, String nMae, Date dN,
			String sexo) {
		super();
		ID = iD;
		Pedigree = pedigree;
		Nome = nome;
		Raca_id = raca_id;
		Raca = raca;
		NPai = nPai;
		NMae = nMae;
		DN = dN;
		Sexo = sexo;
	}

	public int getID() {
		return ID;
	}
	
	public String getPedigree() {
		return Pedigree;
	}

	public String getNome() {
		return Nome;
	}

	public int getRaca_id(){
		return Raca_id;
	}
	
	public String getRaca() {
		return Raca;
	}

	public String getNPai() {
		return NPai;
	}

	public String getNMae() {
		return NMae;
	}

	public String getDN() {
		return new SimpleDateFormat("dd/MM/yyyy").format(DN);
	}

	public String getSexo() {
		return Sexo;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setPedigree(String pedigree) {
		Pedigree = pedigree;
	}

	public void setNome(String nome) {
		Nome = nome;
	}
	
	public void setRaca_id(int raca_id) {
		Raca_id = raca_id;
	}

	public void setRaca(String raca) {
		Raca = raca;
	}

	public void setNPai(String nPai) {
		NPai = nPai;
	}

	public void setNMae(String nMae) {
		NMae = nMae;
	}

	public void setDN(Date date) {
		DN = date;
	}

	public void setSexo(String sexo) {
		Sexo = sexo;
	}
	@Override
	public String toString() {
		return "Cachorro [ID=" + ID + ", Pedigree=" + Pedigree + ", Nome=" + Nome + ", Raca_id=" + Raca_id + ", Raca="
				+ Raca + ", NPai=" + NPai + ", NMae=" + NMae + ", DN=" + DN + ", Sexo=" + Sexo + "]";
	}
}
