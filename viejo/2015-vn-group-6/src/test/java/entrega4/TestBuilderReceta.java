package entrega4;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import businessModel.receta.Condimento;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;
import businessModel.receta.RecetaCompuesta;
import businessModel.temporada.*;
import creacionales.BuilderReceta;
import exceptions.model.receta.*;

public class TestBuilderReceta {

	private BuilderReceta builder;
	private Invierno invierno;
	private List<Ingrediente> ingredientesLemonPie;
	private List<Condimento> condimentosLemonPie;

	@Before
	public void setUp() throws Exception {
		builder = new BuilderReceta();
		invierno = new Invierno();
		ingredientesLemonPie = new ArrayList<Ingrediente>();
		condimentosLemonPie = new ArrayList<Condimento>();
	}

	@Test
	public void creacionRecetaValidaConBuilder() {
		builder.agregarCamposObligatorios("Lemon Pie", "Torta de Limon", invierno, DificultadDePreparacionReceta.MEDIA,
				ingredientesLemonPie, condimentosLemonPie);
		builder.agregarIngrediente("Limon", 2, 30);
		builder.agregarIngrediente("rayadura de 1 Limon", 1, 5);
		builder.agregarIngrediente("huevo", 2, 30);
		builder.agregarCondimento("azucar", 2);
		builder.compilar();
	}

	@Test(expected = ErrorRecetaNoValida.class)
	public void creacionRecetaInvalidaConBuilderPorFaltaDeIngredientes() {

		builder.agregarCamposObligatorios("Lemon Pie", "Torta de Limon", invierno, DificultadDePreparacionReceta.MEDIA,
				ingredientesLemonPie, condimentosLemonPie);
		builder.compilar();
	}

	@Test(expected = ErrorRecetaNoValida.class)
	public void creacionRecetaInvalidaConBuilderPorCaloriasFueraDeRango() {
		builder.agregarCamposObligatorios("Lemon Pie", "Torta de Limon", invierno, DificultadDePreparacionReceta.MEDIA,
				ingredientesLemonPie, condimentosLemonPie);
		builder.agregarIngrediente("Limon", 2, 0);
		builder.agregarIngrediente("rayadura de 1 Limon", 1, 0);
		builder.agregarIngrediente("huevo", 2, 0);
		builder.agregarCondimento("azucar", 0);
		builder.compilar();
	}

	@Test
	public void resetRecetaBuilder() {
		builder.agregarCamposObligatorios("Lemon Pie", "Torta de Limon", invierno, DificultadDePreparacionReceta.MEDIA,
				ingredientesLemonPie, condimentosLemonPie);
		builder.agregarIngrediente("Limon", 2, 30);
		builder.agregarIngrediente("rayadura de 1 Limon", 1, 5);
		builder.agregarIngrediente("huevo", 2, 30);
		builder.agregarCondimento("azucar", 2);
		builder.compilar();
		builder.reset();

		assertNull(builder.getNombre());
		assertNull(builder.getDescripcion());
		assertNull(builder.getTemporada());
		assertEquals(DificultadDePreparacionReceta.NA, builder.getDificultad());
		builder.getIngredientes().isEmpty();
		builder.getCondimento().isEmpty();
	}

	@Test
	public void crearRecetaCompuestaCOnBuilder() {
		Receta sub = new BuilderReceta()
				.agregarCamposObligatorios("Lemon Pie", "Torta de Limon", invierno, DificultadDePreparacionReceta.MEDIA,
						ingredientesLemonPie, condimentosLemonPie)
				.agregarIngrediente("Limon", 2, 30).agregarIngrediente("rayadura de 1 Limon", 1, 5)
				.agregarIngrediente("huevo", 2, 30).agregarCondimento("azucar", 2).compilar();
		Receta superReceta = new BuilderReceta().agregarCamposObligatorios("Lemon Pie", "Torta de Limon", invierno,
				DificultadDePreparacionReceta.MEDIA, ingredientesLemonPie, condimentosLemonPie).setSubReceta(sub)
				.compilar();
		assertEquals(sub, ((RecetaCompuesta) superReceta).getSubReceta());
	}
}
