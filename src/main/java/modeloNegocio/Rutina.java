package modeloNegocio;

public abstract class Rutina {

	public static enum TipoRutina {ACTIVA, SEDENTARIA};
	public static enum Level {NADA, LEVE, MEDIANO, INTENSIVO};
	public Level nivelEjercicio;
	
	abstract TipoRutina tipo();
	abstract int aporte();
}
