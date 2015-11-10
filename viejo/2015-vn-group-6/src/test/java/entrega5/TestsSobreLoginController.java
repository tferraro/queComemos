package entrega5;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import persistencia.GestorCuentas;
import persistencia.mock.GestorCuentasMock;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import webGUI.controllers.LoginController;
import webGUI.controllers.LoginData;
import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Genero;
import creacionales.BuilderUsuario;

public class TestsSobreLoginController {

	private GestorCuentas repo;
	private LoginController controller;

	@Before
	public void setUp() {
		repo = GestorCuentasMock.INSTANCIA;
		repo.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Pedro", new BigDecimal(87),
						new BigDecimal(1.86), LocalDate.of(1993, 06, 03),
						new RutinaActiva()).setSexo(Genero.MASCULINO)
				.compilar());
		repo.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Luisito", new BigDecimal(75),
						new BigDecimal(1.71), LocalDate.of(1992, 01, 12),
						new RutinaActiva()).setSexo(Genero.MASCULINO)
				.compilar());
		repo.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Lucia", new BigDecimal(63),
						new BigDecimal(1.61), LocalDate.of(1994, 2, 10),
						new RutinaActiva()).setSexo(Genero.FEMENINO).compilar());

		controller = new LoginController(GestorCuentasMock.INSTANCIA);
	}

	@After
	public void ending() {
		((GestorCuentasMock) repo).removeAll();
	}

	@Test
	public void parsearCorrectamenteUsuarioYPassword() {
		LoginData data = controller
				.parseLoginData("userName=Pepita&userPassword=4765");
		assertEquals("Pepita", data.userName);
		assertEquals("4765", data.userPassword);
	}

	@Test
	public void seRedireccionaAlLoginPorHacerLogout() {
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
		};
		Response response = new Response() {
		};
		ModelAndView retorno = controller.login(request, response);
		@SuppressWarnings("unchecked")
		String mensaje = ((HashMap<String, Object>) retorno.getModel()).get(
				"mensaje").toString();
		assertEquals("", mensaje);

	}

}
