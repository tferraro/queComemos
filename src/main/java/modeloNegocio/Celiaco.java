package modeloNegocio;

public class Celiaco implements Condicion {

	@Override
	public boolean estadoValido(Usuario usr) {
		return true;
	}

	@Override
	public boolean estaSubsanada(Usuario usr) {
		return true;
	}

}
