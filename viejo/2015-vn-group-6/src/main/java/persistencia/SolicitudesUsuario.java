package persistencia;

import java.util.List;

import businessModel.usuario.Usuario;
import businessModel.usuario.solicitudes.SolicitudNuevoUsuario;

public interface SolicitudesUsuario {

	void agregarSolicitud(Usuario user);

	List<SolicitudNuevoUsuario> verSolicitudesPendientes();

	void aprobar(SolicitudNuevoUsuario solicitud, GestorCuentas repo);

	void rechazar(SolicitudNuevoUsuario solicitud, String string);

	List<SolicitudNuevoUsuario> verSolicitudes();
}