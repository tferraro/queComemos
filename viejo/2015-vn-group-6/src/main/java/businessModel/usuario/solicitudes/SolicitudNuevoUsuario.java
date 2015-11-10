package businessModel.usuario.solicitudes;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import businessModel.usuario.Usuario;
import exceptions.model.usuario.ErrorRepoUsuario;
import exceptions.model.usuario.ErrorSolicitudDePerfil;
import exceptions.model.usuario.ErrorValidezUsuario;
import persistencia.GestorCuentas;
import persistencia.hibernate.PersistentEntity;

@Entity
@Table(name = "Solicitudes_De_Nuevos_Usuarios")
public class SolicitudNuevoUsuario extends PersistentEntity {

	@OneToOne(cascade = CascadeType.PERSIST)
	private Usuario usuario;
	@Enumerated
	private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE.setParameters();

	public SolicitudNuevoUsuario setUsuario(Usuario user) {
		if (user == null)
			throw new ErrorSolicitudDePerfil("Datos de Usuario Mal Hechos");
		try {
			user.esValido();
		} catch (ErrorValidezUsuario e) {
			throw new ErrorSolicitudDePerfil(e.getMessage());
		}
		user.estaPendiente();
		this.usuario = user;
		return this;
	}

	public Boolean estaPendiente() {
		return (estado == EstadoSolicitud.PENDIENTE) ? true : false;
	}

	public Boolean estaAprobada() {
		return (estado == EstadoSolicitud.APROBADA) ? true : false;
	}

	public Boolean estaRechazada() {
		return (estado == EstadoSolicitud.RECHAZADA) ? true : false;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void aprobar(GestorCuentas repo) {
		this.solicitudEstaPendiente();
		this.solicitudAprobada();
		this.agregarAlRepositorio(repo);
	}

	private void solicitudEstaPendiente() {
		if (this.estaAprobada())
			throw new ErrorSolicitudDePerfil("La solicitud ya se encuentra aprobada.");
		if (this.estaRechazada())
			throw new ErrorSolicitudDePerfil("La solicitud ya se encuentra rechazada.");
	}

	private void solicitudAprobada() {
		this.estado = EstadoSolicitud.APROBADA.setParameters();
	}

	private void agregarAlRepositorio(GestorCuentas repo) {
		try {
			usuario.confirmar();
			repo.guardar(usuario);
		} catch (ErrorValidezUsuario e) {
			throw new ErrorSolicitudDePerfil("El usuario no es válido: " + e.getMessage());
		} catch (ErrorRepoUsuario e) {
			throw new ErrorSolicitudDePerfil("Error al agregar el usuario: " + e.getMessage());
		} catch (NullPointerException e) {
			throw new ErrorSolicitudDePerfil("Repositorio de Usuario No Valido");
		}
	}

	public void rechazar(String razon) {
		this.solicitudEstaPendiente();
		this.estado = EstadoSolicitud.RECHAZADA.setParameters(razon);
	}

}
