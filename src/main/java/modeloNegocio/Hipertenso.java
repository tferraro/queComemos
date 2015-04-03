package modeloNegocio;

public class Hipertenso implements Condicion {

	@Override
	public boolean estadoValido(Usuario usr) {
		if(usr.getPreferencias().size() > 0)
			return true;
		return false;
	}

	@Override
	public boolean estaSubsanada(Usuario usr) {
		if(usr.getRutina().tipo() == Rutina.TipoRutina.ACTIVA)
			return true;
		return false;
	}

}
