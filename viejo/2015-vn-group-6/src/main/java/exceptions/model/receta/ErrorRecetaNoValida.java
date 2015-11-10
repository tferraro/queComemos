package exceptions.model.receta;

public class ErrorRecetaNoValida extends RuntimeException {
	public ErrorRecetaNoValida(String msj) {
		super(msj);
	}
}
