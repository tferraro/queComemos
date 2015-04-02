package modeloNegocio;

import java.util.GregorianCalendar;

public class Usuario {

	private String nombre;
	private char sexo;
	private GregorianCalendar fechaNacimiento;
	private double peso;
	private double altura;
	
	//Getters & Setters	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public char getSexo() {
		return sexo;
	}
	public void setSexo(char sexo) {
		this.sexo = sexo;
	}
	public GregorianCalendar getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(GregorianCalendar fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}	
	public double getAltura() {
		return altura;
	}
	public void setAltura(double altura) {
		this.altura = altura;
	}
	
	//Métodos especificos
	public double IMC() {
		double imc =  peso / (altura * altura);
		long imc2 = Math.round(imc*100);
		double imcRedondeado = (((double )imc2) / 100);
		return imcRedondeado;
	}
}