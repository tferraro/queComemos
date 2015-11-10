package externalEntities.mail;

import java.util.ArrayList;
import java.util.List;

public class Mail {

	private String emisor;
	private List<String> receptores = new ArrayList<String>();
	private List<String> cc = new ArrayList<String>();
	private List<String> cco = new ArrayList<String>();
	private String passEmisor;
	private String titulo;
	private String cuerpo;

	private String USER_NAME = "dds.utn.group6";
	private String PASSWORD = "dds-utnGroup6";

	public void mailDeAdmin() {
		this.emisor = USER_NAME;
		this.passEmisor = PASSWORD;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public List<String> getReceptores() {
		return receptores;
	}

	public void agregarReceptor(String receptor) {
		this.receptores.add(receptor);
	}

	public List<String> getCC() {
		return cc;
	}

	public void agregarCC(String cc) {
		this.cc.add(cc);
	}

	public List<String> getCCCO() {
		return cco;
	}

	public void agregarCCO(String cco) {
		this.cco.add(cco);
	}

	public String getPassEmisor() {
		return passEmisor;
	}

	public void setPassEmisor(String passEmisor) {
		this.passEmisor = passEmisor;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
}
