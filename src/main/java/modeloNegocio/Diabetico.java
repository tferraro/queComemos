package modeloNegocio;

public class Diabetico implements Condicion {

	@Override
	public boolean estadoValido(Usuario usr) {
		if(usr.getSexo() != 0)
			if(usr.getPreferencias().size() > 0)
				return true;
		return false;
	}

	@Override
	public boolean estaSubsanada(Usuario usr) {
		if((usr.getRutina().tipo() == Rutina.TipoRutina.ACTIVA) || (usr.getPeso() <= 70))
			return true;
		return false;
	}

}
