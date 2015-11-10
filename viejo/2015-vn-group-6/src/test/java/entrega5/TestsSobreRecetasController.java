package entrega5;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import creacionales.BuilderReceta;
import creacionales.BuilderUsuario;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Receta;
import businessModel.rutina.RutinaActiva;
import businessModel.temporada.Invierno;
import businessModel.temporada.Otonio;
import businessModel.temporada.Primavera;
import businessModel.usuario.Genero;
import persistencia.mock.GestorCuentasMock;
import persistencia.mock.RecetarioMock;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import webGUI.controllers.RecetasController;

public class TestsSobreRecetasController {

	private RecetasController controller;
	private Request pedroRequest;
	private Response response;

	@Before
	public void setup() {
		controller = new RecetasController(RecetarioMock.INSTANCIA,
				GestorCuentasMock.INSTANCIA);
		GestorCuentasMock.INSTANCIA.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Pedro", new BigDecimal(87),
						new BigDecimal(1.86), LocalDate.of(1993, 06, 03),
						new RutinaActiva()).setSexo(Genero.MASCULINO)
				.setPassword("1234").compilar());
		GestorCuentasMock.INSTANCIA.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Luisito", new BigDecimal(75),
						new BigDecimal(1.71), LocalDate.of(1992, 01, 12),
						new RutinaActiva()).setSexo(Genero.MASCULINO)
				.setPassword("1234").compilar());
		GestorCuentasMock.INSTANCIA.guardar(new BuilderUsuario()
				.agregarCamposObligatorios("Lucia", new BigDecimal(63),
						new BigDecimal(1.61), LocalDate.of(1994, 2, 10),
						new RutinaActiva()).setSexo(Genero.FEMENINO)
				.setPassword("1234af").compilar());
		RecetarioMock.INSTANCIA.guardarPublica(new BuilderReceta()
				.agregarNombre("Lemon Pie")
				.agregarDescripcion("Torta de Limon")
				.agregarTemporada(new Invierno())
				.agregarDificultad(DificultadDePreparacionReceta.MEDIA)
				.agregarIngrediente("Limon", 2, 30)
				.agregarIngrediente("rayadura de 1 Limon", 1, 5)
				.agregarIngrediente("huevo", 2, 30)
				.agregarCondimento("azucar", 2).compilar());
		RecetarioMock.INSTANCIA.guardarPublica(new BuilderReceta()
				.agregarNombre("QQQQQQQQ").agregarDescripcion("Why not Q?")
				.agregarTemporada(new Primavera())
				.agregarDificultad(DificultadDePreparacionReceta.FACIL)
				.agregarIngrediente("huevo", 2, 30)
				.agregarCondimento("azucar", 2).compilar());
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
				return "Lemon Pie";
			}

			public String queryParams(String key) {
				switch (key) {
				case "receta_dificultad":
					return "DIFICIL";

				case "receta_temporada":
					return "Otoño";
				case "nuevo_condimento_nombre":
					return "Cinamon";
				case "nuevo_condimento_cant":
					return "4";
				case "ingrediente_nombre":
					return "Guacamole";
				case "ingrediente_calorias":
					return "12";
				case "ingrediente_cantidad":
					return "5";
				default:
					return "";
				}
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
	}

	@After
	public void ending() {
		GestorCuentasMock.INSTANCIA.removeAll();
		RecetarioMock.INSTANCIA.removerTodasLasRecetas();
	}

	@Test
	public void elegirRecetaDevuelveBien() {
		assertTrue(controller.elegirReceta(pedroRequest, response) != null);
		assertEquals(null, response.body());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void mostrarReceta() {
		ModelAndView mv1 = controller.mostrarReceta(GestorCuentasMock.INSTANCIA
				.obtenerUsuarioSiEsValido("Pedro", "1234"), "Lemon Pie");
		assertEquals(false,
				((HashMap<String, Object>) mv1.getModel())
						.containsKey("unfavButton"));
		assertEquals(false,
				((HashMap<String, Object>) mv1.getModel())
						.containsKey("unfavName"));
		assertEquals("glyphicon glyphicon-star-empty",
				((HashMap<String, Object>) mv1.getModel()).get("favButton")
						.toString());
		assertEquals("fav",
				((HashMap<String, Object>) mv1.getModel()).get("favName")
						.toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void mostrarRecetaYfavearFunciona() {
		GestorCuentasMock.INSTANCIA.obtenerUsuarioSiEsValido("Pedro", "1234")
				.agregarFavorito(
						RecetarioMock.INSTANCIA
								.obtenerSinDiscriminar(new Receta()
										.setNombre("Lemon Pie")));
		ModelAndView mv1 = controller.mostrarReceta(GestorCuentasMock.INSTANCIA
				.obtenerUsuarioSiEsValido("Pedro", "1234"), "Lemon Pie");
		assertEquals(false,
				((HashMap<String, Object>) mv1.getModel())
						.containsKey("favButton"));
		assertEquals(false,
				((HashMap<String, Object>) mv1.getModel())
						.containsKey("favName"));
		assertEquals("glyphicon glyphicon-star",
				((HashMap<String, Object>) mv1.getModel()).get("unfavButton")
						.toString());
		assertEquals("unfav",
				((HashMap<String, Object>) mv1.getModel()).get("unfavName")
						.toString());
	}

	@Test
	public void actualizarDificultad() {
		Receta lemonpie = RecetarioMock.INSTANCIA.obtenerSinDiscriminar(new Receta()
		.setNombre("Lemon Pie"));
		assertEquals(DificultadDePreparacionReceta.MEDIA, lemonpie.getDificultad());	
		controller.actualizarDificultad(pedroRequest, response);
		lemonpie = RecetarioMock.INSTANCIA.obtenerSinDiscriminar(new Receta()
		.setNombre("Lemon Pie"));
		assertEquals(DificultadDePreparacionReceta.DIFICIL, lemonpie.getDificultad());
	}
	
	@Test
	public void actualizarTemporada() {
		Receta lemonpie = RecetarioMock.INSTANCIA.obtenerSinDiscriminar(new Receta()
		.setNombre("Lemon Pie"));
		assertEquals(new Invierno().getNombre(), lemonpie.getTemporada().getNombre());	
		controller.actualizarTemporada(pedroRequest, response);
		lemonpie = RecetarioMock.INSTANCIA.obtenerSinDiscriminar(new Receta()
		.setNombre("Lemon Pie"));
		assertEquals(new Otonio().getNombre(), lemonpie.getTemporada().getNombre());	
	}
	
	@Test
	public void agregarCondimento() {
		Receta lemonpie = RecetarioMock.INSTANCIA.obtenerSinDiscriminar(new Receta()
		.setNombre("Lemon Pie"));
		assertEquals(1, lemonpie.getCondimentosTotales().size());	
		controller.agregarCondimento(pedroRequest, response);
		lemonpie = RecetarioMock.INSTANCIA.obtenerSinDiscriminar(new Receta()
		.setNombre("Lemon Pie"));
		assertEquals(2, lemonpie.getCondimentosTotales().size());	
	}
	
	@Test
	public void agregarIngrediente() {
		Receta lemonpie = RecetarioMock.INSTANCIA.obtenerSinDiscriminar(new Receta()
		.setNombre("Lemon Pie"));
		assertEquals(3, lemonpie.getIngredientesTotales().size());	
		controller.agregarIngrediente(pedroRequest, response);
		lemonpie = RecetarioMock.INSTANCIA.obtenerSinDiscriminar(new Receta()
		.setNombre("Lemon Pie"));
		assertEquals(4, lemonpie.getIngredientesTotales().size());	
	}
}
