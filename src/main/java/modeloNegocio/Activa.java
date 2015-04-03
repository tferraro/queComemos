package modeloNegocio;

public class Activa extends Rutina {
	
	public Activa(Rutina.Level lvl) {
		this.nivelEjercicio = lvl;
	}
	
	@Override
	int aporte() {
		return 30;
	}

	@Override
	TipoRutina tipo() {
		return Rutina.TipoRutina.ACTIVA;
	}
}
