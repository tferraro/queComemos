package persistencia.hibernate;

import java.util.List;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import persistencia.GestorCuentas;
import persistencia.SolicitudesUsuario;
import businessModel.usuario.Usuario;
import businessModel.usuario.solicitudes.SolicitudNuevoUsuario;

public class SolicitudesUsuarioHibernate implements SolicitudesUsuario, WithGlobalEntityManager {

	String query = "SELECT s FROM SolicitudNuevoUsuario s";
	
	@Override
	public void agregarSolicitud(Usuario user) {
		SolicitudNuevoUsuario solicitud = new SolicitudNuevoUsuario();
		solicitud.setUsuario(user);
		entityManager().persist(solicitud);
	}

	public void agregarNuevaSolicitud(SolicitudNuevoUsuario solicitud) {
		entityManager().persist(solicitud);
	}

	@Override
	public List<SolicitudNuevoUsuario> verSolicitudesPendientes() {
		return null;
	}

	@Override
	public void aprobar(SolicitudNuevoUsuario solicitud, GestorCuentas repo) {
		solicitud.aprobar(repo);

	}

	@Override
	public void rechazar(SolicitudNuevoUsuario solicitud, String string) {
		solicitud.rechazar(string);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SolicitudNuevoUsuario> verSolicitudes() {
		return (List<SolicitudNuevoUsuario>) entityManager().createQuery(query).getResultList();
	}

	public SolicitudNuevoUsuario verSolicitud(SolicitudNuevoUsuario solicitud) {
		return entityManager().find(SolicitudNuevoUsuario.class, solicitud.getId());
	}

}
