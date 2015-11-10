package entrega6;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import creacionales.BuilderUsuario;
import exceptions.model.usuario.ErrorRepoUsuario;
import persistencia.GestorCuentas;
import persistencia.hibernate.GestorCuentasHibernate;
import persistencia.hibernate.SolicitudesUsuarioHibernate;
import businessModel.condicionMedica.Diabetico;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Genero;
import businessModel.usuario.Usuario;
import businessModel.usuario.solicitudes.SolicitudNuevoUsuario;

public class TestSobrePersistenciaSolicitudes extends AbstractPersistenceTest
		implements WithGlobalEntityManager {

	private GestorCuentas repoUsuarios;
	private Usuario user;

	@Before
	public void setUp() {
		repoUsuarios = new GestorCuentasHibernate();
		user = new BuilderUsuario()
				.agregarCamposObligatorios("Pedrito", new BigDecimal(78),
						new BigDecimal(1.98), LocalDate.of(2011, 04, 1),
						new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarMail("pedroFargo@gmail.com")
				.agregarPreferencias("enchilada")
				.agregarPreferencias("chorizo").agregarSexo(Genero.MASCULINO)
				.agregarCondicionMedica(new Diabetico()).compilar();
	}

	@Test
	public void persistirSolicitudDeNuevoUsuario() {
		SolicitudesUsuarioHibernate solicitudes = new SolicitudesUsuarioHibernate();
		SolicitudNuevoUsuario solicitud = new SolicitudNuevoUsuario();
		solicitud.setUsuario(user);
		solicitudes.agregarNuevaSolicitud(solicitud);
		assertEquals(user, solicitudes.verSolicitud(solicitud).getUsuario());
	}

	@Test
	public void persistirSolicitudYAprobarLaMisma() {
		SolicitudesUsuarioHibernate solicitudes = new SolicitudesUsuarioHibernate();
		SolicitudNuevoUsuario solicitud = new SolicitudNuevoUsuario();
		solicitud.setUsuario(user);
		solicitudes.agregarNuevaSolicitud(solicitud);
		solicitudes.verSolicitud(solicitud).aprobar(repoUsuarios);
		assertEquals(user, repoUsuarios.obtener(user));
	}

	@Test
	public void persistirSolicitudYAprobarlaDesdeElRepo() throws Exception {
		SolicitudesUsuarioHibernate solicitudes = new SolicitudesUsuarioHibernate();
		SolicitudNuevoUsuario solicitud = new SolicitudNuevoUsuario();
		solicitud.setUsuario(user);
		solicitudes.agregarNuevaSolicitud(solicitud);
		solicitudes.aprobar(solicitud, repoUsuarios);
		assertEquals(user, repoUsuarios.obtener(user));
	}

	@Test
	public void persistirSolicitudYRechazarla() {
		SolicitudesUsuarioHibernate solicitudes = new SolicitudesUsuarioHibernate();
		SolicitudNuevoUsuario solicitud = new SolicitudNuevoUsuario();
		solicitud.setUsuario(user);
		solicitudes.agregarNuevaSolicitud(solicitud);
		solicitudes.rechazar(solicitudes.verSolicitud(solicitud), "Por que si");

		assertTrue(solicitudes.verSolicitud(solicitud).estaRechazada());
	}

	@Test
	public void probarQueNoSePuedanVerLosUsuariosPendientes() {
		SolicitudesUsuarioHibernate solicitudes = new SolicitudesUsuarioHibernate();
		SolicitudNuevoUsuario solicitud = new SolicitudNuevoUsuario();
		solicitud.setUsuario(user);
		solicitudes.agregarNuevaSolicitud(solicitud);
		solicitudes.rechazar(solicitudes.verSolicitud(solicitud), "Por que si");
		try {
			repoUsuarios.obtener(user);
			fail("No Exception Throwed");
		} catch (ErrorRepoUsuario e) {
			assertEquals("getUsuario: No existe Usuario con dicho Nombre",
					e.getMessage());
		}
	}

	@Test
	public void verListaSolicitudes() {
		SolicitudesUsuarioHibernate solicitudes = new SolicitudesUsuarioHibernate();
		Usuario user2 = new BuilderUsuario()
				.agregarCamposObligatorios("Pedrito2", new BigDecimal(78),
						new BigDecimal(1.98), LocalDate.of(2011, 04, 1),
						new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarMail("pedroFargo@gmail.com")
				.agregarPreferencias("enchilada")
				.agregarPreferencias("chorizo").agregarSexo(Genero.MASCULINO)
				.agregarCondicionMedica(new Diabetico()).compilar();
		SolicitudNuevoUsuario solicitud = new SolicitudNuevoUsuario();
		solicitud.setUsuario(user);
		SolicitudNuevoUsuario solicitud2 = new SolicitudNuevoUsuario();
		solicitud.setUsuario(user2);
		solicitudes.agregarNuevaSolicitud(solicitud);
		solicitudes.agregarNuevaSolicitud(solicitud2);
		assertEquals(2, solicitudes.verSolicitudes().size());
	}
}
