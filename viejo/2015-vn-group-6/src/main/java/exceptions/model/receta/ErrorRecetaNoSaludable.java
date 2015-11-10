package exceptions.model.receta;

public class ErrorRecetaNoSaludable extends RuntimeException {
	public ErrorRecetaNoSaludable(String msj) {
		super(msj);
	}
}
