package entrega3;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import creacionales.BuilderUsuario;
import exceptions.model.usuario.ErrorSolicitudDePerfil;
import persistencia.SolicitudesUsuario;
import persistencia.mock.GestorCuentasMock;
import persistencia.mock.SolicitudesUsuarioMock;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Usuario;
import businessModel.usuario.solicitudes.SolicitudNuevoUsuario;

public class TestsSobreGenerarPerfil {

	private GestorCuentasMock repoUsuario;
	private BuilderUsuario builder;
	private SolicitudesUsuarioMock solicitudes;

	@Before
	public void setUp() {
		repoUsuario = GestorCuentasMock.INSTANCIA;
		repoUsuario.removeAll();
		builder = new BuilderUsuario();
		builder.agregarCamposObligatorios("Pedro Lozano", new BigDecimal(86), new BigDecimal(1.68),
				LocalDate.of(1993, 06, 06), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO));
		solicitudes = SolicitudesUsuarioMock.INSTANCIA;
		solicitudes.limpiarSolicitudes();
	}

	@Test(expected = ErrorSolicitudDePerfil.class)
	public void tratarDeAgregarSolicitudMalaPorUsuarioMalo() {
		solicitudes.agregarSolicitud(null);
		assertEquals(1, solicitudes.verSolicitudesPendientes().size());
	}

	@Test(expected = ErrorSolicitudDePerfil.class)
	public void tratarDeAgregarSolicitudMalaPorRepoMalo() {
		solicitudes.agregarSolicitud(builder.compilar());
		solicitudes.aprobar(solicitudes.verSolicitudesPendientes().get(0), null);
		assertEquals(1, solicitudes.verSolicitudesPendientes().size());
	}

	@Test(expected = ErrorSolicitudDePerfil.class)
	public void tratarDeAgregarSolicitudMalaPorUserInvalido() {
		solicitudes.agregarSolicitud(new Usuario());
		assertEquals(1, solicitudes.verSolicitudesPendientes().size());
	}

	@Test
	public void agregarSolicitudDePerfilDeUsuarioValidandoElAgregado() {
		solicitudes.agregarSolicitud(builder.compilar());
		assertEquals(1, solicitudes.verSolicitudesPendientes().size());
	}

	@Test
	public void agregarSolicitudDePerfilDeUsuarioPor2() {
		solicitudes.agregarSolicitud(builder.compilar());
		SolicitudesUsuario solicitudes2 = SolicitudesUsuarioMock.INSTANCIA;
		solicitudes2.agregarSolicitud(builder.setNombre("Carlongas").compilar());
		assertEquals(2, SolicitudesUsuarioMock.INSTANCIA.verSolicitudesPendientes().size());
	}

	@Test
	public void agregarSolicitudDePerfilDeUsuarioValidandoElAgregadoEfectivo() {
		solicitudes.agregarSolicitud(builder.compilar());
		assertEquals("Pedro Lozano", solicitudes.verSolicitudesPendientes().get(0).getUsuario().getNombre());
	}

	@Test
	public void aprobarSolicitudDeUsuarioCorrectamente() {
		solicitudes.agregarSolicitud(builder.compilar());
		List<SolicitudNuevoUsuario> pendientes = solicitudes.verSolicitudesPendientes();
		solicitudes.aprobar(pendientes.get(0), repoUsuario);
	}

	@Test(expected = ErrorSolicitudDePerfil.class)
	public void rechazarUnaAprobacionDeSolicitudConUnUsuarioExistente() {
		solicitudes.agregarSolicitud(builder.compilar());
		solicitudes.agregarSolicitud(builder.compilar());
		List<SolicitudNuevoUsuario> pendientes = solicitudes.verSolicitudesPendientes();
		solicitudes.aprobar(pendientes.get(0), repoUsuario);
		solicitudes.aprobar(pendientes.get(1), repoUsuario);
	}

	@Test
	public void rechazarUnaAprobacionDeSolicitudPorYaEstarAprobada() {
		solicitudes.agregarSolicitud(builder.compilar());
		List<SolicitudNuevoUsuario> pendientes = solicitudes.verSolicitudesPendientes();
		solicitudes.aprobar(pendientes.get(0), repoUsuario);
		try {
			solicitudes.aprobar(pendientes.get(0), repoUsuario);
			fail("Se aprobo la solicitud aprobada anteriormente.");
		} catch (ErrorSolicitudDePerfil e) {
			assertTrue(e.getMessage().matches("(.*)La solicitud ya se encuentra aprobada.(.*)"));
		}
	}

	@Test
	public void rechazarSolicitudPorRazonCualquiera() {
		solicitudes.agregarSolicitud(builder.compilar());
		List<SolicitudNuevoUsuario> pendientes = solicitudes.verSolicitudesPendientes();
		solicitudes.rechazar(pendientes.get(0), "Porque Pedro me cae mal.");
		assertEquals(0, solicitudes.verSolicitudesPendientes().size());
		assertTrue(pendientes.get(0).estaRechazada());
	}

	@Test(expected = ErrorSolicitudDePerfil.class)
	public void errorPorRechazarSolicitudInexistente() {
		solicitudes.agregarSolicitud(builder.compilar());
		solicitudes.rechazar(new SolicitudNuevoUsuario().setUsuario(builder.compilar()), "Porque Pedro me cae mal.");
	}

	@Test
	public void errorAlRechazarSolicitudPorRazonCualquieraPorYaEstarAprobada() {
		solicitudes.agregarSolicitud(builder.compilar());
		List<SolicitudNuevoUsuario> pendientes = solicitudes.verSolicitudesPendientes();
		solicitudes.aprobar(pendientes.get(0), repoUsuario);
		try {
			solicitudes.rechazar(pendientes.get(0), "Porque Quiero.");
			fail("Se rechazo la solicitud rechazada anteriormente.");
		} catch (ErrorSolicitudDePerfil e) {
			assertTrue(e.getMessage().matches("(.*)La solicitud ya se encuentra aprobada.(.*)"));
		}
	}

	@Test
	public void errorAlRechazarSolicitudPorRazonCualquieraPorYaEstarRechazada() {
		solicitudes.agregarSolicitud(builder.compilar());
		List<SolicitudNuevoUsuario> pendientes = solicitudes.verSolicitudesPendientes();
		solicitudes.rechazar(pendientes.get(0), "Porque Pedro me cae mal.");
		try {
			solicitudes.rechazar(pendientes.get(0), "Porque Quiero.");
			fail("Se rechazo la solicitud rechazada anteriormente.");
		} catch (ErrorSolicitudDePerfil e) {
			assertTrue(e.getMessage().matches("(.*)La solicitud ya se encuentra rechazada.(.*)"));
		}
	}
}
