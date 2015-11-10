package entrega5;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import consulta.filtro.Filtro;
import consulta.monitorSincronico.MonitorDiezRecetasMasConsultadas;
import consulta.ordenadores.OrdenadorReceta;
import creacionales.BuilderReceta;
import creacionales.BuilderUsuario;
import businessModel.condicionMedica.Diabetico;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Receta;
import businessModel.rutina.RutinaActiva;
import businessModel.temporada.Invierno;
import businessModel.temporada.Temporada;
import businessModel.usuario.Genero;
import externalEntities.recetas.RepoRecetasExterno;
import persistencia.mock.GestorCuentasMock;
import persistencia.mock.RecetarioMock;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import webGUI.controllers.ConsultaController;
import webGUI.runner.WebRoot;

public class TestsSobreConsultaController {

	private Request pedroRequest;
	private Response response;
	private ConsultaController controller;

	@Before
	public void setup() {
		RepoRecetasExterno.INSTANCIA.getRecetas(new Receta()).forEach(
				r -> RecetarioMock.INSTANCIA.guardarPublica(r));

		RecetarioMock.INSTANCIA.guardar(new BuilderReceta()
				.agregarNombre("Lemon Pie")
				.agregarDescripcion("Torta de Limon")
				.agregarTemporada(new Invierno())
				.agregarDificultad(DificultadDePreparacionReceta.MEDIA)
				.agregarIngrediente("Limon", 2, 30)
				.agregarIngrediente("rayadura de 1 Limon", 1, 5)
				.agregarIngrediente("huevo", 2, 30)
				.agregarCondimento("azucar", 2).compilar());
		GestorCuentasMock.INSTANCIA.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Pedro", new BigDecimal(87),
						new BigDecimal(1.86), LocalDate.of(1993, 06, 03),
						new RutinaActiva()).setSexo(Genero.MASCULINO)
				.setPassword("1234").agregarPreferencias("Peceto")
				.agregarPreferencias("Colita de tu mama")
				.agregarDisgustos("Humus")
				.agregarCondicionMedica(new Diabetico()).compilar());
		GestorCuentasMock.INSTANCIA.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Luisito", new BigDecimal(75),
						new BigDecimal(1.71), LocalDate.of(1992, 01, 12),
						new RutinaActiva()).setSexo(Genero.MASCULINO)
				.setPassword("1234").compilar());
		GestorCuentasMock.INSTANCIA.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Lucia", new BigDecimal(63),
						new BigDecimal(1.61), LocalDate.of(1994, 2, 10),
						new RutinaActiva()).setSexo(Genero.FEMENINO)
				.setPassword("1234a").compilar());
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

			public String queryParams(String key) {
				switch (key) {
				case "filtro_nombre":
					return "Sin Filtro";
				case "ordenador_nombre":
					return "Sin Orden";
				case "receta_nombre":
					return "cane";
				case "receta_dificultad":
					return "DIFICIL";
				case "receta_temporada":
					return "Todo el Año";
				case "receta_caloria_min":
					return "29";
				case "receta_caloria_max":
					return "41";
				}
				return null;
			}
		};
		response = new Response() {
			public String location = null;

			public void redirect(String location) {
				this.location = location;
			}

			public String body() {
				return location;
			}
		};
		controller = new ConsultaController(GestorCuentasMock.INSTANCIA,
				RecetarioMock.INSTANCIA);
	}

	@After
	public void ending() {
		GestorCuentasMock.INSTANCIA.removeAll();
		RecetarioMock.INSTANCIA.removerTodasLasRecetas();
		MonitorDiezRecetasMasConsultadas.INSTANCIA().reiniciarEstadistica();
	}

	@Test
	public void probarMetodover() {
		assertTrue(controller.ver(pedroRequest, response) != null);
		assertEquals(null, response.body());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void probarMetodomostrarSinListarNada() {
		ModelAndView mv = controller.ver(pedroRequest, response);
		HashMap<String, Object> viewModel = (HashMap<String, Object>) mv
				.getModel();

		assertEquals(Filtro.obtenerNombres(RecetarioMock.INSTANCIA).size(),
				((List<String>) viewModel.get("filtros")).size());
		assertEquals(OrdenadorReceta.obtenerNombres().size(),
				((List<String>) viewModel.get("ordenadores")).size());
		assertEquals(Temporada.nombres().stream().map(t -> t.getNombre())
				.collect(Collectors.toList()).size(),
				((List<String>) viewModel.get("temporadas")).size());
		assertEquals(Temporada.nombres().stream().map(t -> t.getNombre())
				.collect(Collectors.toList()).size(),
				((List<String>) viewModel.get("temporadas")).size());
		assertEquals(WebRoot.INSTANCIA().perfilUsuario("Pedro"),
				((String) viewModel.get("linkUsuario")));
		assertEquals(WebRoot.INSTANCIA().consulta(),
				((String) viewModel.get("linkConsultas")));
	}

	@Test
	public void probarMetodoConsultar() {
		controller.consultar(pedroRequest, response);
		assertEquals(1, MonitorDiezRecetasMasConsultadas.INSTANCIA()
				.recetasMasConsultada().size());
		assertEquals("canelones de ricota y verdura",
				MonitorDiezRecetasMasConsultadas.INSTANCIA()
						.recetasMasConsultada().get(0).receta);
	}
}
