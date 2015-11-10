package exceptions.model.usuario;

public class ErrorAlSugerirUnaReceta extends RuntimeException {
	public ErrorAlSugerirUnaReceta(String msj) {
		super(msj);
	}
}
