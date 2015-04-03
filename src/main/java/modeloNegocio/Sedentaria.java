package modeloNegocio;

public class Sedentaria extends Rutina {

	
	public Sedentaria(Rutina.Level lvl) {
		this.nivelEjercicio = lvl;
	}
	
	@Override
	int aporte() {
		if(this.nivelEjercicio == Rutina.Level.LEVE)
			return -30;
		if(this.nivelEjercicio == Rutina.Level.MEDIANO)
			return 30;
		return 0;
	}

	@Override
	TipoRutina tipo() {
		return Rutina.TipoRutina.SEDENTARIA;
	}

}
