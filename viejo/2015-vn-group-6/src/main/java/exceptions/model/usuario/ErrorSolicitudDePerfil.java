package exceptions.model.usuario;

public class ErrorSolicitudDePerfil extends RuntimeException {
	public ErrorSolicitudDePerfil(String msj) {
		super(msj);
	}
}
