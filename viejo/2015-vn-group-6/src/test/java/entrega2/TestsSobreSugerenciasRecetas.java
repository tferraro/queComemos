package entrega2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import exceptions.model.usuario.ErrorAlSugerirUnaReceta;
import businessModel.condicionMedica.*;
import businessModel.receta.Condimento;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;
import businessModel.usuario.Grupo;
import businessModel.usuario.Recetable;
import businessModel.usuario.Usuario;

public class TestsSobreSugerenciasRecetas {

	private Recetable user, grupito, usrDiabetico, usrCeliaco, usrVegano,
			usrHipertenso;
	private Receta recetaPrueba;

	@Before
	public void setUp() {
		Usuario aux = new Usuario();
		aux.agregarCondicionMedica(new Diabetico());
		aux.agregarCondicionMedica(new Celiaco());
		aux.agregarCondicionMedica(new Hipertenso());
		aux.agregarCondicionMedica(new Vegano());
		aux.agregarComidaQueNoGustan("Chocolate");
		aux.agregarComidaQueNoGustan("Papa");
		user = aux;

		Usuario aux1 = new Usuario();
		aux1.agregarCondicionMedica(new Diabetico());
		aux1.agregarComidaQueNoGustan("Chocotorta");
		usrDiabetico = aux1;

		Usuario aux2 = new Usuario();
		aux2.agregarCondicionMedica(new Celiaco());
		usrCeliaco = aux2;

		Usuario aux3 = new Usuario();
		aux3.agregarCondicionMedica(new Hipertenso());
		usrHipertenso = aux3;

		Usuario aux4 = new Usuario();
		aux4.agregarCondicionMedica(new Vegano());
		usrVegano = aux4;

		Grupo auxG = new Grupo();
		auxG.setNombre("Grupo Testers");
		auxG.agregarInteres("Batata");
		auxG.agregarInteres("Pollo");
		auxG.agregarIntegrante((Usuario) usrDiabetico);
		auxG.agregarIntegrante((Usuario) usrCeliaco);
		auxG.agregarIntegrante((Usuario) usrHipertenso);
		auxG.agregarIntegrante((Usuario) usrVegano);
		grupito = auxG;

		recetaPrueba = new Receta().setNombre("Pure de Batata");
		Ingrediente batata = new Ingrediente();
		batata.setNombre("Batata");
		batata.setCaloriasIndividuales(100);
		batata.setCantidad(3);
		recetaPrueba.agregarIngrediente(batata);
	}

	@Test
	public void sugerenciaAlUsuarioCorrecta() {
		user.sugerir(recetaPrueba);
	}

	@Test
	public void sugerenciaAlUsuarioCorrectaConChequeoDeExistenciaDeReceta() {
		user.sugerir(recetaPrueba);
		assertEquals(1, ((Usuario) user).getRecetasPrivadas().size());
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlUsuarioRechazadaPorComidasQueDisgustan() {
		Ingrediente papa = new Ingrediente();
		papa.setNombre("Papa");
		papa.setCaloriasIndividuales(100);
		papa.setCantidad(2);
		recetaPrueba.agregarIngrediente(papa);
		user.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlUsuarioRechazadaPorDiabeticoConMas100GramosAzucar() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(101);
		recetaPrueba.agregarCondimento(azucar);
		user.sugerir(recetaPrueba);

	}

	@Test
	public void sugerenciaAlUsuarioCorrectaPorDiabeticoConMenos100GramosAzucar() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(99);
		recetaPrueba.agregarCondimento(azucar);
		user.sugerir(recetaPrueba);

	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlUsuarioRechazadaPorHipertensoConSal() {
		Condimento sal = new Condimento();
		sal.setNombre("Sal");
		sal.setCantidad(200);
		recetaPrueba.agregarCondimento(sal);
		user.sugerir(recetaPrueba);

	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlUsuarioRechazadaPorHipertensoConCaldo() {
		Condimento caldo = new Condimento();
		caldo.setNombre("Caldo");
		caldo.setCantidad(2);
		recetaPrueba.agregarCondimento(caldo);
		user.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlUsuarioRechazadaPorVeganoConPollo() {
		Ingrediente ingNoVegano = new Ingrediente();
		ingNoVegano.setNombre("Pollo");
		ingNoVegano.setCaloriasIndividuales(1000);
		ingNoVegano.setCantidad(2);
		recetaPrueba.agregarIngrediente(ingNoVegano);
		user.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlUsuarioRechazadaPorVeganoConCarne() {
		Ingrediente ingNoVegano = new Ingrediente();
		ingNoVegano.setNombre("Carne");
		ingNoVegano.setCaloriasIndividuales(1000);
		ingNoVegano.setCantidad(2);
		recetaPrueba.agregarIngrediente(ingNoVegano);
		user.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlUsuarioRechazadaPorVeganoConChivito() {
		Ingrediente ingNoVegano = new Ingrediente();
		ingNoVegano.setNombre("Chivito");
		ingNoVegano.setCaloriasIndividuales(1000);
		ingNoVegano.setCantidad(2);
		recetaPrueba.agregarIngrediente(ingNoVegano);
		user.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlUsuarioRechazadaPorVeganoConChori() {
		Ingrediente ingNoVegano = new Ingrediente();
		ingNoVegano.setNombre("Chori");
		ingNoVegano.setCaloriasIndividuales(1000);
		ingNoVegano.setCantidad(2);
		recetaPrueba.agregarIngrediente(ingNoVegano);
		user.sugerir(recetaPrueba);
	}

	@Test
	public void sugerenciaAlGrupoCorrectaPorInteresBatata() {
		grupito.sugerir(recetaPrueba);
		grupito.sugerir(new Receta().setNombre("Pollo al disco"));
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerirRecetaAlGrupoRechazadaPorNoInteres() {
		grupito.sugerir(new Receta().setNombre("Gato al horno"));
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlGrupoRechazadaPorVeganoConChori() {
		Ingrediente ingNoVegano = new Ingrediente();
		ingNoVegano.setNombre("Chori");
		ingNoVegano.setCaloriasIndividuales(1000);
		ingNoVegano.setCantidad(2);
		recetaPrueba.agregarIngrediente(ingNoVegano);
		grupito.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlGrupoAceptadaAPesarQueNoGustaChocotorta() {
		grupito.sugerir(new Receta().setNombre("Chocotorta"));
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlGruopoRechazadaPorDiabeticoConMas100GramosAzucar() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(101);
		recetaPrueba.agregarCondimento(azucar);
		grupito.sugerir(recetaPrueba);

	}

	@Test
	public void sugerenciaAlGrupoCorrectaPorDiabeticoConMenos100GramosAzucar() {
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(99);
		recetaPrueba.agregarCondimento(azucar);
		grupito.sugerir(recetaPrueba);

	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlGrupoRechazadaPorHipertensoConSal() {
		Condimento sal = new Condimento();
		sal.setNombre("Sal");
		sal.setCantidad(200);
		recetaPrueba.agregarCondimento(sal);
		grupito.sugerir(recetaPrueba);

	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlGrupoRechazadaPorHipertensoConCaldo() {
		Condimento caldo = new Condimento();
		caldo.setNombre("Caldo");
		caldo.setCantidad(2);
		recetaPrueba.agregarCondimento(caldo);
		grupito.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlGrupoRechazadaPorVeganoConPollo() {
		Ingrediente ingNoVegano = new Ingrediente();
		ingNoVegano.setNombre("Pollo");
		ingNoVegano.setCaloriasIndividuales(1000);
		ingNoVegano.setCantidad(2);
		recetaPrueba.agregarIngrediente(ingNoVegano);
		grupito.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlGrupoRechazadaPorVeganoConCarne() {
		Ingrediente ingNoVegano = new Ingrediente();
		ingNoVegano.setNombre("Carne");
		ingNoVegano.setCaloriasIndividuales(1000);
		ingNoVegano.setCantidad(2);
		recetaPrueba.agregarIngrediente(ingNoVegano);
		grupito.sugerir(recetaPrueba);
	}

	@Test(expected = ErrorAlSugerirUnaReceta.class)
	public void sugerenciaAlGrupoRechazadaPorVeganoConChivito() {
		Ingrediente ingNoVegano = new Ingrediente();
		ingNoVegano.setNombre("Chivito");
		ingNoVegano.setCaloriasIndividuales(1000);
		ingNoVegano.setCantidad(2);
		recetaPrueba.agregarIngrediente(ingNoVegano);
		grupito.sugerir(recetaPrueba);
	}

}
