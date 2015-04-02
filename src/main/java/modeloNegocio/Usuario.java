package modeloNegocio;


import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

public class Usuario {

	private String nombre = null;
	private char sexo = 0;
	private Calendar fechaNacimiento = null;
	private double peso = 0;
	private double altura = 0;

	private Set<String> preferencias = new LinkedHashSet<String>();
	private Set<String> comidasDisgustan = new LinkedHashSet<String>();
	private Set<Condicion> condiciones = new LinkedHashSet<Condicion>();
	private Rutina rutina;
	private Set<Receta> recetasAgregadas = new LinkedHashSet<Receta>();
	
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
	public Calendar getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Calendar fechaNacimiento) {
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
	public void agregarPreferencia(String pref) {
		preferencias.add(pref);
	}
	public void removerPreferencia(String pref) {

		preferencias.remove(pref);
	}
	public Set<String> getPreferencias() {
		return preferencias;
	}
	public void agregarComidaFea(String comida) {
		comidasDisgustan.add(comida);
	}
	public void removerComidaFea(String comida) {

		comidasDisgustan.remove(comida);
	}
	public Set<String> getComidasFeas() {
		return comidasDisgustan;
	}
	public void agregarCondicion(Condicion cond) {
		condiciones.add(cond);
	}
	public void removerCondicion(Condicion cond) {

		condiciones.remove(cond);		
	}
	public Set<Condicion> getCondiciones() {
		return condiciones;
	}
	public Rutina getRutina() {
		return rutina;
	}
	public void setRutina(Rutina rutina) {
		this.rutina = rutina;
	}
	public void agregarReceta(Receta receta) {
		this.recetasAgregadas.add(receta);
	}
	public void removerReceta(Receta receta) {

		this.recetasAgregadas.remove(receta);		
	}
	public Set<Receta> getRecetas() {
		return recetasAgregadas;
	}

	
	//Métodos publicos
	public double IMC() {
		double imc =  peso / (altura * altura);
		long imc2 = Math.round(imc*100); //Redondea con criterio de 5, cuidado!
		double imcRedondeado = (((double )imc2) / 100);
		return imcRedondeado;
	}
	public boolean esValido() {
		if(faltanCamposObligatorios())
			return false;
		if(this.nombre.length() <= 4)
			return false;
		for(Condicion cond : this.condiciones)
			if(!cond.estadoValido(this))
				return false;
		if(!fechaNacimientoValida())
			return false;
		return true;
	}
	public boolean sigueRutinaSaludable() {
		if(18 < this.IMC() && this.IMC() < 30)
			if(condicionesSubsanadas())
				return true;
		return false;
	}
	
	//Métodos auxiliares privadas
	private boolean condicionesSubsanadas() {
		if(this.getCondiciones().size() == 0)
			return true;
		for(Condicion cond : this.getCondiciones())
			if(cond.estaSubsanada(this))
				return true;
		return false;
	}
	private boolean faltanCamposObligatorios() {
		if(this.nombre == null)
			return true;
		if(this.peso == 0)
			return true;
		if(this.altura == 0)
			return true;
		if(this.fechaNacimiento == null)
			return true;
		if(this.rutina == null)
			return true;
		return false;
	}
	private boolean fechaNacimientoValida() {
		if(this.fechaNacimiento.get(Calendar.YEAR) <= Calendar.getInstance().get(Calendar.YEAR))
			if(this.fechaNacimiento.get(Calendar.MONTH) <= Calendar.getInstance().get(Calendar.MONTH))
				if(this.fechaNacimiento.get(Calendar.DATE) < Calendar.getInstance().get(Calendar.DATE))
						return true;
		//El chequeo se hace asi para evitar el error por el tiempo, solo nos importa la fecha
		return false;
	}
}