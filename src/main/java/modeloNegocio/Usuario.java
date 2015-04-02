package modeloNegocio;

public class Usuario {

	private double peso;
	private double altura;
	
	//Getters & Setters	
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