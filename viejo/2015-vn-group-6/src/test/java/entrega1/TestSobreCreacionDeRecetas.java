package entrega1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import businessModel.receta.*;
import businessModel.temporada.*;
import businessModel.usuario.Usuario;
import exceptions.model.receta.ErrorRecetaNoValida;
import persistencia.mock.RecetarioMock;

public class TestSobreCreacionDeRecetas {

	private RecetarioMock repositorio = RecetarioMock.INSTANCIA;
	private Receta receta;
	private Receta recetaPublica;
	private Usuario user;
	private List<Ingrediente> mockIngredientes = new ArrayList<Ingrediente>();
	private List<Condimento> mockCondimentos = new ArrayList<Condimento>();
	private Temporada invierno;
	private Temporada verano;

	@Before
	public void setUp() {
		repositorio.removerTodasLasRecetas();

		Ingrediente morronRojo = new Ingrediente();
		morronRojo.setCaloriasIndividuales(10);
		morronRojo.setCantidad(4);
		morronRojo.setNombre("Morron Rojo");

		Ingrediente cuadril = new Ingrediente();
		cuadril.setCaloriasIndividuales(50);
		cuadril.setCantidad(3);
		cuadril.setNombre("Cuadril");

		Condimento tomillo = new Condimento();
		tomillo.setNombre("Tomillo");
		tomillo.setCantidad(200);

		mockIngredientes.add(morronRojo);
		mockIngredientes.add(cuadril);
		mockCondimentos.add(tomillo);

		user = new Usuario();
		receta = new Receta().setNombre("Pollo");
		receta.setDescripcion("Una Descripcion");
		receta.setDificultad(DificultadDePreparacionReceta.FACIL);

		recetaPublica = new Receta().setNombre("Pollo");
		recetaPublica.setDescripcion("Una Descripcion");
		recetaPublica.setDificultad(DificultadDePreparacionReceta.DIFICIL);

		invierno = new Invierno();
		verano = new Verano();
	}

	@Test
	public void agregarIngredientesAUnaReceta() {
		receta.getIngredientes().addAll(mockIngredientes);
		receta.getCondimentos().addAll(mockCondimentos);
		assertEquals(2, receta.getIngredientesTotales().size());
	}

	@Test
	public void usuarioAgregaUnaRecetaPrivada() {
		receta.getIngredientes().addAll(mockIngredientes);
		receta.getCondimentos().addAll(mockCondimentos);
		user.agregarReceta(receta);
	}

	@Test
	public void agregarRecetaPublica() {
		Ingrediente ing = new Ingrediente();
		ing.setNombre("Ingrediente X");
		ing.setCaloriasIndividuales(100);
		ing.setCantidad(5);
		recetaPublica.getIngredientes().add(ing);
		repositorio.guardarPublica(recetaPublica);
	}

	@Test(expected = ErrorRecetaNoValida.class)
	public void noPuedeAgregarRecetaPublicaSinIngredientes() {
		repositorio.guardarPublica(recetaPublica);
	}

	@Test(expected = ErrorRecetaNoValida.class)
	public void noPuedeAgregarRecetaPublicaConPocasCalorias() {
		Ingrediente ingDeBajasCalorias = new Ingrediente();
		ingDeBajasCalorias.setCaloriasIndividuales(1);
		ingDeBajasCalorias.setCantidad(3);
		ingDeBajasCalorias.setNombre("Ingrediente Secreto");
		recetaPublica.getIngredientes().add(ingDeBajasCalorias);
		repositorio.guardarPublica(recetaPublica);
	}

	@Test(expected = ErrorRecetaNoValida.class)
	public void noPuedeAgregarRecetaPublicaConDemaciadasCalorias() {
		Ingrediente ingDeMuchasCalorias = new Ingrediente();
		ingDeMuchasCalorias.setCaloriasIndividuales(1001);
		ingDeMuchasCalorias.setCantidad(5);
		ingDeMuchasCalorias.setNombre("Ingrediente Secreto #2");
		receta.getIngredientes().add(ingDeMuchasCalorias);
		repositorio.guardarPublica(recetaPublica);
	}

	@Test(expected = ErrorRecetaNoValida.class)
	public void usuarioNoPuedeAgregaUnaRecetaSinIngredientes() {
		receta.getCondimentos().addAll(mockCondimentos);
		user.agregarReceta(receta);
	}

	@Test(expected = ErrorRecetaNoValida.class)
	public void usuarioNoPuedeAgregaUnaRecetaConPocasCalorias() {
		Ingrediente ingDeBajasCalorias = new Ingrediente();
		ingDeBajasCalorias.setCaloriasIndividuales(1);
		ingDeBajasCalorias.setCantidad(3);
		ingDeBajasCalorias.setNombre("Ingrediente Secreto");
		receta.getIngredientes().add(ingDeBajasCalorias);
		receta.getCondimentos().addAll(mockCondimentos);
		user.agregarReceta(receta);
	}

	@Test(expected = ErrorRecetaNoValida.class)
	public void usuarioNoPuedeAgregaUnaRecetaConDemaciadasCalorias() {
		Ingrediente ingDeMuchasCalorias = new Ingrediente();
		ingDeMuchasCalorias.setCaloriasIndividuales(1001);
		ingDeMuchasCalorias.setCantidad(5);
		ingDeMuchasCalorias.setNombre("Ingrediente Secreto #2");
		receta.getIngredientes().add(ingDeMuchasCalorias);
		receta.getCondimentos().addAll(mockCondimentos);
		user.agregarReceta(receta);
	}

	@Test
	public void recetaDePolloCon190Calorias() {
		receta.getIngredientes().addAll(mockIngredientes);
		receta.getCondimentos().addAll(mockCondimentos);
		assertEquals(receta.calcularCalorias(), new BigDecimal(190));
	}

	@Test
	public void agregarRecetaConSubRecetas() {
		receta.getIngredientes().addAll(mockIngredientes);
		receta.getCondimentos().addAll(mockCondimentos);
		Receta superReceta = new RecetaCompuesta().setSubReceta(receta).setNombre("Pure y Receta X");
		Ingrediente papas = new Ingrediente();
		papas.setCantidad(4);
		papas.setCaloriasIndividuales(15);
		superReceta.agregarIngrediente(papas);
		user.agregarReceta(superReceta);
	}

	@Test
	public void obtenerIngredientesTotalesConSubReceta() {
		receta.getIngredientes().addAll(mockIngredientes);
		Receta superReceta = new RecetaCompuesta().setSubReceta(receta).setNombre("Pure y Receta X");
		Ingrediente papas = new Ingrediente();
		papas.setCantidad(4);
		papas.setCaloriasIndividuales(15);
		superReceta.agregarIngrediente(papas);
		assertEquals(2, receta.getIngredientesTotales().size());
		assertEquals(3, superReceta.getIngredientesTotales().size());

	}

	@Test
	public void validarCaloriasDeRecetaConSubRecetas() {
		receta.getIngredientes().addAll(mockIngredientes);
		receta.getCondimentos().addAll(mockCondimentos);
		Receta superReceta = new RecetaCompuesta().setSubReceta(receta).setNombre("Pure y Receta X");
		Ingrediente papas = new Ingrediente();
		papas.setCantidad(4);
		papas.setCaloriasIndividuales(15);
		superReceta.agregarIngrediente(papas);
		assertEquals(new BigDecimal(250), superReceta.calcularCalorias());
	}

	@Test
	public void validarRecetaTengaCondimentos() {
		receta.getCondimentos().addAll(mockCondimentos);
		assertEquals(1, receta.getCondimentosTotales().size());
	}

	@Test
	public void validarTemporadaInviernoReceta() {
		receta.setTemporada(invierno);
		receta.getIngredientes().addAll(mockIngredientes);
		receta.getCondimentos().addAll(mockCondimentos);
		assertEquals("Invierno", receta.getTemporada().getNombre());
	}

	@Test
	public void validarTemporadaVeranoReceta() {
		receta.setTemporada(verano);
		receta.getIngredientes().addAll(mockIngredientes);
		receta.getCondimentos().addAll(mockCondimentos);
		assertEquals("Verano", receta.getTemporada().getNombre());
	}

	@Test
	public void recetaDePolloNivelDificultadFacil() {
		assertTrue(receta.getDificultad() == DificultadDePreparacionReceta.FACIL);
	}

	@Test
	public void testDeIdentidadCondimento() {
		Condimento tomillo = new Condimento();
		tomillo.setNombre("Tomillo");
		tomillo.setCantidad(200);
		tomillo.soy("tomillo Rojo");
	}

}
