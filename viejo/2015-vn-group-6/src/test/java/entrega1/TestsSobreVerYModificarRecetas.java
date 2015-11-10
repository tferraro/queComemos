package entrega1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import businessModel.receta.*;
import businessModel.usuario.Usuario;
import exceptions.model.receta.ErrorNoSePuedeVerOModificarReceta;
import persistencia.mock.RecetarioMock;

public class TestsSobreVerYModificarRecetas {

	private RecetarioMock repositorio = RecetarioMock.INSTANCIA;
	private Usuario user, extranio;
	private Receta recetaPrivada, recetaDelExtranio;

	@Before
	public void setUp() {
		repositorio.removerTodasLasRecetas();

		recetaPrivada = new Receta().setNombre("Receta de Pollo Privada");
		recetaPrivada
				.setDescripcion("Receta de Pollo Privada para hacer tests");

		recetaDelExtranio = new Receta().setNombre("Pollo al disco del extranio");
		recetaDelExtranio
				.setDescripcion("Receta de pollo al disco del extranio.");

		Ingrediente morronRojo = new Ingrediente();
		morronRojo.setCaloriasIndividuales(10);
		morronRojo.setCantidad(4);
		morronRojo.setNombre("Morron Rojo");
		recetaPrivada.agregarIngrediente(morronRojo);

		Ingrediente morronVerde = new Ingrediente();
		morronVerde.setCaloriasIndividuales(10);
		morronVerde.setCantidad(4);
		morronVerde.setNombre("Morron Verde");
		recetaDelExtranio.agregarIngrediente(morronVerde);

		Ingrediente cuadril = new Ingrediente();
		cuadril.setCaloriasIndividuales(50);
		cuadril.setCantidad(3);
		cuadril.setNombre("Cuadril");
		recetaPrivada.agregarIngrediente(cuadril);

		Condimento tomillo = new Condimento();
		tomillo.setNombre("Tomillo");
		tomillo.setCantidad(200);
		recetaPrivada.agregarCondimento(tomillo);

		Condimento aji = new Condimento();
		aji.setNombre("Aji");
		aji.setCantidad(4);
		recetaDelExtranio.agregarCondimento(aji);

		user = new Usuario();
		user.setNombre("User");
		extranio = new Usuario();
		extranio.setNombre("Extraño");
	}

	@Test
	public void usuarioPuedeVerRecetaPropia() {
		user.agregarReceta(recetaPrivada);
		user.puedeVerRecetasPrivadas(user);
	}

	@Test(expected = ErrorNoSePuedeVerOModificarReceta.class)
	public void usuarioNoPuedeVerRecetaAjena() {
		user.agregarReceta(recetaPrivada);
		user.puedeVerRecetasPrivadas(user);
		user.puedeVerRecetasPrivadas(extranio);
	}

	@Test
	public void sePuedeVerRecetaPublica() {
		repositorio.guardarPublica(recetaDelExtranio);
		List<Receta> listaRecetas = repositorio.obtener();
		assertEquals("Pollo al disco del extranio", listaRecetas.get(0)
				.getNombre());
	}

	@Test
	public void usuarioModificaRecetaPropia() {
		user.agregarReceta(recetaPrivada);
		Receta recetaAmodificar = user.verRecetas(user).get(0);
		assertEquals("Hay mas de 2 ingredientes en la receta de prueba", 2,
				recetaAmodificar.getIngredientes().size());

		Ingrediente ingSecreto = new Ingrediente();
		ingSecreto.setNombre("3er Ingrediente");
		ingSecreto.setCaloriasIndividuales(100);
		ingSecreto.setCantidad(1);
		recetaAmodificar.agregarIngrediente(ingSecreto);
		user.modificarReceta(recetaAmodificar, user);
		assertEquals("La receta no se modifico correctamente", 3, user
				.verRecetas(user).get(0).getIngredientes().size());
	}

	@Test
	public void usuarioModificaSegundaRecetaPropia() {
		user.agregarReceta(recetaDelExtranio);
		user.agregarReceta(recetaPrivada);
		Receta recetaAmodificar = user.verRecetas(user).get(1);
		assertEquals("Hay mas de 2 ingredientes en la receta de prueba", 2,
				recetaAmodificar.getIngredientes().size());

		Ingrediente ingSecreto = new Ingrediente();
		ingSecreto.setNombre("3er Ingrediente");
		ingSecreto.setCaloriasIndividuales(100);
		ingSecreto.setCantidad(1);
		recetaAmodificar.agregarIngrediente(ingSecreto);
		user.modificarReceta(recetaAmodificar, user);
		assertEquals("La receta no se modifico correctamente", 3, user
				.verRecetas(user).get(1).getIngredientes().size());
	}

	@Test(expected = ErrorNoSePuedeVerOModificarReceta.class)
	public void usuarioNoPuedeModificarUnaRecetaAjena() {
		user.agregarReceta(recetaPrivada);
		// Teoricamente no deberia poder verla, por eso igual probamos
		Receta recetaAmodificar = user.verRecetas(user).get(0);

		Ingrediente ingSecreto = new Ingrediente();
		ingSecreto.setNombre("3er Ingrediente");
		ingSecreto.setCaloriasIndividuales(100);
		ingSecreto.setCantidad(1);
		recetaAmodificar.agregarIngrediente(ingSecreto);
		user.modificarReceta(recetaAmodificar, extranio);
	}

	@Test
	public void usuarioModificaRecetaPublica() {
		Receta recetaPublica = new Receta().setNombre("Receta Publica #1");
		Ingrediente morronVerde = new Ingrediente();
		morronVerde.setCaloriasIndividuales(10);
		morronVerde.setCantidad(4);
		morronVerde.setNombre("Morron Verde");
		recetaPublica.agregarIngrediente(morronVerde);
		repositorio.guardarPublica(recetaPublica);

		Receta recetaAModificar = repositorio.obtener().get(0);
		Ingrediente morronRojo = new Ingrediente();
		morronRojo.setCaloriasIndividuales(10);
		morronRojo.setCantidad(4);
		morronRojo.setNombre("Morron Rojo");
		recetaAModificar.agregarIngrediente(morronRojo);
		user.modificarRecetaPublica(recetaAModificar);
		assertEquals(
				repositorio.obtener().get(0).getIngredientes().size() + 1,
				user.verRecetas(user).get(0).getIngredientes().size());
	}

	@Test(expected = ErrorNoSePuedeVerOModificarReceta.class)
	public void usuarioNoVeModificacionesDeOtroUsuarioEnRecetaPublica() {
		Receta recetaPublica = new Receta().setNombre("Receta Publica #1");
		Ingrediente morronVerde = new Ingrediente();
		morronVerde.setCaloriasIndividuales(10);
		morronVerde.setCantidad(4);
		morronVerde.setNombre("Morron Verde");
		recetaPublica.agregarIngrediente(morronVerde);
		repositorio.guardarPublica(recetaPublica);

		Receta recetaAModificar = repositorio.obtener().get(0);
		Ingrediente morronRojo = new Ingrediente();
		morronRojo.setCaloriasIndividuales(10);
		morronRojo.setCantidad(4);
		morronRojo.setNombre("Morron Rojo");
		recetaAModificar.agregarIngrediente(morronRojo);
		user.modificarRecetaPublica(recetaAModificar);
		user.verRecetas(extranio);
	}

	@Test
	public void clonarCorrectamenteUnaReceta() {
		Receta recetaClonada = recetaPrivada.getClone();
		// Cambiamos la descripcion
		recetaClonada.setDescripcion("Hola");
		// Agregamos un condimento
		Condimento cond1 = new Condimento();
		cond1.setNombre("Cond1");
		cond1.setCantidad(2);
		recetaClonada.agregarCondimento(cond1);
		// Alteramos un condimento
		recetaClonada.getCondimentos().get(0).setCantidad(2);
		// Agregamos un ingrediente
		Ingrediente ing1 = new Ingrediente();
		ing1.setNombre("Ing1");
		ing1.setCaloriasIndividuales(100);
		ing1.setCantidad(2);
		recetaClonada.agregarIngrediente(ing1);
		// Alteramos un ingrediente
		recetaClonada.getIngredientes().get(0).setCantidad(1);
		// Chequear que no se haya modificado la original
		assertEquals("Se modifico la descripcion",

		"Receta de Pollo Privada para hacer tests",
				recetaPrivada.getDescripcion());
		assertEquals("Se modifico la cantidad de Condimentos", 1, recetaPrivada
				.getCondimentos().size());
		assertEquals("Se modifico los Condimentos", new Integer(200),
				recetaPrivada.getCondimentos().get(0).getCantidad());
		assertEquals("Se modifico la cantidad de Ingredientes", 2,
				recetaPrivada.getIngredientes().size());
		assertEquals("Se modifico los Ingredientes", new Integer(4),
				recetaPrivada.getIngredientes().get(0).getCantidad());
	}

	@Test
	public void clonarCorrectamenteSubReceta() {
		Receta superReceta = new RecetaCompuesta().setSubReceta(recetaPrivada).setNombre("Receta Master");
		Receta superClon = superReceta.getClone();
		// Cambiamos la descripcion
		superClon.setDescripcion("Hola");
		// Agregamos un condimento
		Condimento cond1 = new Condimento();
		cond1.setNombre("Cond1");
		cond1.setCantidad(2);
		superClon.agregarCondimento(cond1);
		// Alteramos un condimento
		superClon.getCondimentos().get(0).setCantidad(2);
		// Agregamos un ingrediente
		Ingrediente ing1 = new Ingrediente();
		ing1.setNombre("Ing1");
		ing1.setCaloriasIndividuales(100);
		ing1.setCantidad(2);
		superClon.agregarIngrediente(ing1);
		// Alteramos un ingrediente
		superClon.getIngredientes().get(0).setCantidad(1);
		// Cambiamos la subReceta
		((RecetaCompuesta) superClon).setSubReceta(recetaDelExtranio);
		// Chequear que no se haya modificado la original
		assertEquals("Se modifico la descripcion", null,
				superReceta.getDescripcion());
		assertEquals("Se modifico la cantidad de Condimentos", 0, superReceta
				.getCondimentos().size());
		assertEquals("Se modifico la cantidad de Ingredientes", 0, superReceta
				.getIngredientes().size());
		assertEquals("Se modifico la subreceta", recetaPrivada,
				((RecetaCompuesta) superReceta).getSubReceta());
	}
}
