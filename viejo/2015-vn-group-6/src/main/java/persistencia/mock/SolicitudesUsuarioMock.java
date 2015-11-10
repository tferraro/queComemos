package persistencia.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import businessModel.usuario.Usuario;
import businessModel.usuario.solicitudes.SolicitudNuevoUsuario;
import exceptions.model.usuario.ErrorSolicitudDePerfil;
import persistencia.SolicitudesUsuario;
import persistencia.GestorCuentas;

public enum SolicitudesUsuarioMock implements SolicitudesUsuario {
	INSTANCIA;

	private List<SolicitudNuevoUsuario> listaSolicitudes = new ArrayList<>();

	@Override
	public void agregarSolicitud(Usuario user) {
		listaSolicitudes.add(new SolicitudNuevoUsuario().setUsuario(user));
	}

	@Override
	public List<SolicitudNuevoUsuario> verSolicitudesPendientes() {
		return listaSolicitudes.stream().filter(sol -> sol.estaPendiente()).collect(Collectors.toList());
	}

	@Override
	public void aprobar(SolicitudNuevoUsuario solicitud, GestorCuentas repo) {
		if (!listaSolicitudes.contains(solicitud))
			throw new ErrorSolicitudDePerfil("La solicitud no existe");
		solicitud.aprobar(repo);
	}

	@Override
	public void rechazar(SolicitudNuevoUsuario solicitud, String string) {
		if (!listaSolicitudes.contains(solicitud))
			throw new ErrorSolicitudDePerfil("La solicitud no existe");
		solicitud.rechazar(string);
	}

	public void limpiarSolicitudes() {
		listaSolicitudes.clear();
	}

	@Override
	public List<SolicitudNuevoUsuario> verSolicitudes() {
		return listaSolicitudes;
	}
}
