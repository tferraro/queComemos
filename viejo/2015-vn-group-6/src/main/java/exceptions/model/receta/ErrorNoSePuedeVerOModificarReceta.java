package exceptions.model.receta;

public class ErrorNoSePuedeVerOModificarReceta extends RuntimeException {
	public ErrorNoSePuedeVerOModificarReceta(String msj) {
		super(msj);
	}
}