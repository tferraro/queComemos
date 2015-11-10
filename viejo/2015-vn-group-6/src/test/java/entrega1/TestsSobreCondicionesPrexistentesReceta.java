package entrega1;

import org.junit.Before;
import org.junit.Test;

import businessModel.condicionMedica.*;
import businessModel.receta.*;
import businessModel.usuario.*;
import exceptions.model.receta.ErrorRecetaNoSaludable;
import persistencia.mock.RecetarioMock;

public class TestsSobreCondicionesPrexistentesReceta {

	private RecetarioMock repositorio = RecetarioMock.INSTANCIA;
	private Receta receta;
	private Usuario userDiabetico;
	private Usuario userHipertenso;
	private Usuario userVegano;
	private Usuario userCeliaco;
	private Usuario userCeliacoDiabetico;
	private Usuario userHipertensoVegano;

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

		receta = new Receta().setNombre("Receta Generica");
		receta.setDescripcion("Receta para probar diferentes ingredientes");
		receta.agregarIngrediente(cuadril);
		receta.agregarIngrediente(morronRojo);
		receta.agregarCondimento(tomillo);

		userVegano = new Usuario();
		userVegano.agregarCondicionMedica(new Vegano());
		userHipertenso = new Usuario();
		userHipertenso.agregarCondicionMedica(new Hipertenso());
		userDiabetico = new Usuario();
		userDiabetico.agregarCondicionMedica(new Diabetico());
		userCeliaco = new Usuario();
		userCeliaco.agregarCondicionMedica(new Celiaco());
		userCeliacoDiabetico = new Usuario();
		userCeliacoDiabetico.agregarCondicionMedica(new Celiaco());
		userCeliacoDiabetico.agregarCondicionMedica(new Diabetico());
		userHipertensoVegano = new Usuario();
		userHipertensoVegano.agregarCondicionMedica(new Hipertenso());
		userHipertensoVegano.agregarCondicionMedica(new Vegano());
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void malaRecetaParaVeganoPorCarne() {
		Ingrediente carne = new Ingrediente();
		carne.setCaloriasIndividuales(50);
		carne.setCantidad(3);
		carne.setNombre("Carne");
		receta.agregarIngrediente(carne);
		userVegano.agregarReceta(receta);
		userVegano.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void malaRecetaParaVeganoPorChivito() {
		Ingrediente chivito = new Ingrediente();
		chivito.setCaloriasIndividuales(50);
		chivito.setCantidad(3);
		chivito.setNombre("Chivito");
		receta.agregarIngrediente(chivito);
		userVegano.agregarReceta(receta);
		userVegano.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void malaRecetaParaVeganoPorPollo() {
		Ingrediente pollo = new Ingrediente();
		pollo.setCaloriasIndividuales(50);
		pollo.setCantidad(3);
		pollo.setNombre("Pollo");
		receta.agregarIngrediente(pollo);
		userVegano.agregarReceta(receta);
		userVegano.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void malaRecetaParaVeganoPorChori() {
		Ingrediente chori = new Ingrediente();
		chori.setCaloriasIndividuales(50);
		chori.setCantidad(3);
		chori.setNombre("Chori");
		receta.agregarIngrediente(chori);
		userVegano.agregarReceta(receta);
		userVegano.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void malaRecetaParaHipertensoPorSal() {
		Condimento sal = new Condimento();
		sal.setNombre("Sal");
		sal.setCantidad(200);
		receta.agregarCondimento(sal);
		userHipertenso.agregarReceta(receta);
		userHipertenso.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void malaRecetaParaHipertensoPorCaldo() {
		Condimento caldo = new Condimento();
		caldo.setNombre("Caldo");
		caldo.setCantidad(200);
		receta.agregarCondimento(caldo);
		userHipertenso.agregarReceta(receta);
		userHipertenso.meEsAdecuada(receta);
	}

	@Test
	public void buenaRecetaParaDiabeticoPorPocoAzucar() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(99);
		receta.agregarCondimento(azucar);
		userDiabetico.agregarReceta(receta);
		userDiabetico.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void malaRecetaParaDiabeticoPorAltoAzucar() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(200);
		receta.agregarCondimento(azucar);
		userDiabetico.agregarReceta(receta);
		userDiabetico.meEsAdecuada(receta);
	}

	@Test
	public void buenaRecetaGenericaParaCeliaco() {
		userCeliaco.agregarReceta(receta);
		userCeliaco.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void usuarioDiabeticoCeliacoMalaRecetaPorSuCondicionDeDiabetico() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(200);
		receta.agregarCondimento(azucar);
		userCeliacoDiabetico.meEsAdecuada(receta);
	}

	@Test
	public void usuarioDiabeticoCeliacoBuenaReceta() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(20);
		receta.agregarCondimento(azucar);
		userCeliacoDiabetico.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void usuarioHipertensoVeganoMalaRecetaPorSuCondicionDeVegano() {
		Ingrediente chivito = new Ingrediente();
		chivito.setCaloriasIndividuales(50);
		chivito.setCantidad(3);
		chivito.setNombre("Chivito");
		receta.agregarIngrediente(chivito);
		userHipertensoVegano.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void usuarioHipertensoVeganoMalaRecetaPorSuCondicionDeHipertension() {
		Condimento caldo = new Condimento();
		caldo.setNombre("Caldo");
		caldo.setCantidad(200);
		receta.agregarCondimento(caldo);
		userHipertensoVegano.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void usuarioHipertensoVeganoMalaRecetaPorSusCondiciones() {
		Ingrediente chivito = new Ingrediente();
		chivito.setCaloriasIndividuales(50);
		chivito.setCantidad(3);
		chivito.setNombre("Chivito");
		Condimento caldo = new Condimento();
		caldo.setNombre("Caldo");
		caldo.setCantidad(200);
		receta.agregarCondimento(caldo);
		userHipertensoVegano.meEsAdecuada(receta);
	}

	@Test
	public void usuarioHiperTensoVeganoBuenaReceta() {
		userHipertensoVegano.meEsAdecuada(receta);
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void usuarioNoLeEsSaludableRecetaPorSubRecetaPorIngrediente() {
		Ingrediente carne = new Ingrediente();
		carne.setCaloriasIndividuales(50);
		carne.setCantidad(3);
		carne.setNombre("Carne");
		receta.agregarIngrediente(carne);
		userVegano.agregarReceta(receta);
		userVegano.meEsAdecuada(new RecetaCompuesta().setSubReceta(receta).setNombre("SuperReceta"));
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void usuarioNoLeEsSaludableRecetaPorSubRecetaPorCondimento() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(200);
		receta.agregarCondimento(azucar);
		userCeliacoDiabetico.meEsAdecuada(new RecetaCompuesta().setSubReceta(receta).setNombre("SuperReceta"));
	}
}
