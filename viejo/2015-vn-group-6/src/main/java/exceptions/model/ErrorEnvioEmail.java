package exceptions.model;

public class ErrorEnvioEmail extends RuntimeException {
	public ErrorEnvioEmail(String msj) {
		super(msj);
	}
}
