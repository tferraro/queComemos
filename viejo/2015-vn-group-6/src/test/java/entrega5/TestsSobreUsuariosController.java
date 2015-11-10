package entrega5;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Genero;
import creacionales.BuilderUsuario;
import persistencia.GestorCuentas;
import persistencia.mock.GestorCuentasMock;
import persistencia.mock.RecetarioMock;
import spark.Request;
import spark.Response;
import spark.Session;
import webGUI.controllers.UsuariosController;
import webGUI.runner.WebRoot;

public class TestsSobreUsuariosController {

	private GestorCuentas repo;
	private UsuariosController controller;
	private Request pedroRequest;

	@Before
	public void setup() {
		repo = GestorCuentasMock.INSTANCIA;
		repo.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Pedro", new BigDecimal(87),
						new BigDecimal(1.86), LocalDate.of(1993, 06, 03),
						new RutinaActiva()).setSexo(Genero.MASCULINO)
				.setPassword("1234").compilar());
		repo.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Luisito", new BigDecimal(75),
						new BigDecimal(1.71), LocalDate.of(1992, 01, 12),
						new RutinaActiva()).setSexo(Genero.MASCULINO)
				.setPassword("1234").compilar());
		repo.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Lucia", new BigDecimal(63),
						new BigDecimal(1.61), LocalDate.of(1994, 2, 10),
						new RutinaActiva()).setSexo(Genero.FEMENINO)
				.setPassword("1234af").compilar());

		controller = new UsuariosController(GestorCuentasMock.INSTANCIA,
				RecetarioMock.INSTANCIA);
		
		Session mockSesion = Mockito.mock(Session.class);
		Mockito.doNothing().when(mockSesion).invalidate();
		Mockito.when(mockSesion.attribute("nombre")).thenReturn("Pedro");
		Mockito.when(mockSesion.attribute("contrasenia")).thenReturn("1234");
		pedroRequest = new Request() {
			Session sesion = mockSesion;

			public String body() {
				return "";
			}

			public Session session(boolean tipo) {
				return sesion;
			}

			public Session session() {
				return sesion;
			}

			public String params(String key) {
				return "Pedro";
			}
		};
	}

	@After
	public void ending() {
		((GestorCuentasMock) repo).removeAll();
	}

	@Test
	public void verPerfilDeUsuarioConMalaDireccion() {
		Session mockSesion = Mockito.mock(Session.class);
		Mockito.doNothing().when(mockSesion).invalidate();
		Request request = new Request() {
			Session sesion = mockSesion;

			public String body() {
				return "";
			}

			public Session session(boolean tipo) {
				return sesion;
			}

			public Session session() {
				return sesion;
			}

			public String params(String key) {
				return "Nombre que no Existe";
			}
		};
		Response response = new Response() {
			public String location = null;

			public void redirect(String location) {
				this.location = location;
			}

			public String body() {
				return location;
			}
		};
		assertEquals(null, controller.perfilUsuario(request, response));
		assertEquals(WebRoot.INSTANCIA().root, response.body());
	}

	@Test
	public void verPerfilDeUsuarioConMalUsuarioEnSession() {
		Session mockSesion = Mockito.mock(Session.class);
		Mockito.doNothing().when(mockSesion).invalidate();
		Mockito.when(mockSesion.attribute("nombre")).thenReturn("Pedrito");
		Mockito.when(mockSesion.attribute("contrasenia")).thenReturn("1234");
		Request request = new Request() {
			Session sesion = mockSesion;

			public String body() {
				return "";
			}

			public Session session(boolean tipo) {
				return sesion;
			}

			public Session session() {
				return sesion;
			}

			public String params(String key) {
				return "Pedro";
			}
		};
		Response response = new Response() {
			public String location = null;

			public void redirect(String location) {
				this.location = location;
			}

			public String body() {
				return location;
			}
		};
		assertEquals(null, controller.perfilUsuario(request, response));
		assertEquals(WebRoot.INSTANCIA().root, response.body());
	}

	@Test
	public void verPerfilDeUsuarioConMalPasswordEnSession() {
		Session mockSesion = Mockito.mock(Session.class);
		Mockito.doNothing().when(mockSesion).invalidate();
		Mockito.when(mockSesion.attribute("nombre")).thenReturn("Pedro");
		Mockito.when(mockSesion.attribute("contrasenia")).thenReturn("12345");
		Request request = new Request() {
			Session sesion = mockSesion;

			public String body() {
				return "";
			}

			public Session session(boolean tipo) {
				return sesion;
			}

			public Session session() {
				return sesion;
			}

			public String params(String key) {
				return "Pedro";
			}
		};
		Response response = new Response() {
			public String location = null;

			public void redirect(String location) {
				this.location = location;
			}

			public String body() {
				return location;
			}
		};
		assertEquals(null, controller.perfilUsuario(request, response));
		assertEquals(WebRoot.INSTANCIA().root, response.body());
	}

	@Test
	public void verPerfilDeUsuarioDevuelveModelAndView() {

		Response response = new Response() {
			public String location = null;

			public void redirect(String location) {
				this.location = location;
			}

			public String body() {
				return location;
			}
		};
		assertTrue(controller.perfilUsuario(pedroRequest, response) != null);
		assertEquals(null, response.body());
	}

	@Test
	public void verHomeDePedroQueDevuelveModelAndView() {

		Response response = new Response() {
			public String location = null;

			public void redirect(String location) {
				this.location = location;
			}

			public String body() {
				return location;
			}
		};
		
		assertTrue(controller.home(pedroRequest, response) != null);
		assertEquals(null, response.body());
	}
}
