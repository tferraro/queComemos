package modeloNegocio;

public interface Condicion {
	boolean estadoValido(Usuario usr);
	boolean estaSubsanada(Usuario usr);
}
