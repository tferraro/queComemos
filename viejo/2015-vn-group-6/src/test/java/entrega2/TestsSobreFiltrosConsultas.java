package entrega2;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import consulta.filtro.*;
import consulta.ordenadores.*;
import creacionales.BuilderUsuario;
import exceptions.model.receta.ErrorConsulta;
import persistencia.mock.RecetarioMock;
import businessModel.condicionMedica.*;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.receta.*;
import businessModel.usuario.*;

public class TestsSobreFiltrosConsultas {

	private Receta recPure, recChurrasco, recEnsalada;
	private Usuario userPedro, userLuis;
	private RecetarioMock repo = RecetarioMock.INSTANCIA;
	private BuilderUsuario builder;

	@Before
	public void setUp() {
		recPure = new Receta().setNombre("Pure de Papas");
		Ingrediente papa = new Ingrediente();
		papa.setNombre("Papa");
		papa.setCaloriasIndividuales(200);
		papa.setCantidad(2);
		recPure.agregarIngrediente(papa);
		Ingrediente churrasco = new Ingrediente();
		churrasco.setNombre("Churrasco con Sal");
		churrasco.setCaloriasIndividuales(1000);
		churrasco.setCantidad(1);
		Condimento sal = new Condimento();
		sal.setNombre("Sal");
		sal.setCantidad(5);
		recChurrasco = new Receta().setNombre("Churrasco a la plancha");
		recChurrasco.agregarIngrediente(churrasco);
		recChurrasco.agregarCondimento(sal);
		recEnsalada = new Receta().setNombre("Ensalada de Lechuga y Tomate");
		Ingrediente lechuga = new Ingrediente();
		lechuga.setNombre("Lechuga");
		lechuga.setCaloriasIndividuales(5);
		lechuga.setCantidad(2);
		Ingrediente tomate = new Ingrediente();
		tomate.setNombre("Tomate");
		tomate.setCaloriasIndividuales(50);
		tomate.setCantidad(2);
		recEnsalada.agregarIngrediente(tomate);
		recEnsalada.agregarIngrediente(lechuga);
		recEnsalada.agregarCondimento(sal);

		builder = new BuilderUsuario();
		builder.agregarCamposObligatorios("Pedro", new BigDecimal(95),
				new BigDecimal(1.86), LocalDate.of(1993, 3, 6),
				new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO));
		builder.agregarSexo(Genero.MASCULINO);
		userPedro = builder.compilar();
		builder.agregarCamposObligatorios("Luisito", new BigDecimal(78),
				new BigDecimal(1.86), LocalDate.of(1993, 3, 6),
				new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO));
		builder.agregarSexo(Genero.MASCULINO);
		userLuis = builder.compilar();

		userPedro.agregarReceta(recPure);
		repo.guardarPublica(recChurrasco);
		userLuis.agregarReceta(recEnsalada);

		Grupo grupito = new Grupo();
		grupito.agregarIntegrante(userLuis);
		grupito.agregarIntegrante(userPedro);
		// AL AGREGAR OTRO GRUPO, VERIFICO EN LOS TEST QUE NO SE DUPLIQUEN LAS
		// RECETAS EN LAS CONSULTAS
		Grupo grupito2 = new Grupo();
		grupito2.agregarIntegrante(userLuis);
		grupito2.agregarIntegrante(userPedro);

	}
	
	@After
	public void ending() {
		repo.removerTodasLasRecetas();		
	}

	@Test
	public void consultaSinFiltros() {
		Filtro base = new FiltroConsulta(repo);
		assertEquals(3, base.consultar(userPedro).size());
	}

	@Test(expected = ErrorConsulta.class)
	public void creacionDeFiltroCompuestoRompePorFailFast() {
		new FiltroPorCalorias(null);
	}
	@Test
	public void consultaConFiltroCalorias() {
		Filtro filtroCalorias = new FiltroPorCalorias(new FiltroConsulta(repo));
		assertEquals(2, filtroCalorias.consultar(userPedro).size());
	}

	@Test
	public void consultaConFiltroCaloriasEHipertenso() {
		userPedro.agregarCondicionMedica(new Hipertenso());
		userPedro.agregarCondicionMedica(new Vegano());
		userPedro.agregarCondicionMedica(new Diabetico());
		Filtro filtroCondiciones = new FiltroPorCondicionesPrexistentes(
				new FiltroPorCalorias(new FiltroConsulta(repo)));
		assertEquals(1, filtroCondiciones.consultar(userPedro).size());
		// Luis no tiene sobrepeso ni es hipertenso
		assertEquals(3, filtroCondiciones.consultar(userLuis).size());
	}

	@Test
	public void consultaConFiltroCaloriasYVegano() {
		userPedro.agregarCondicionMedica(new Vegano());
		Receta recAux = new RecetaCompuesta().setSubReceta(recPure).setNombre("Chivito con Pure");
		Ingrediente chivito = new Ingrediente();
		chivito.setNombre("Chivito");
		chivito.setCaloriasIndividuales(10);
		chivito.setCantidad(1);
		recAux.agregarIngrediente(chivito);
		userPedro.agregarReceta(recAux);

		Filtro filtroCondiciones = new FiltroPorCondicionesPrexistentes(
				new FiltroPorCalorias(new FiltroConsulta(repo)));
		assertEquals(2, filtroCondiciones.consultar(userPedro).size());
	}

	@Test
	public void consultaConFiltroCaloriasYDiabetico() {
		userPedro.agregarCondicionMedica(new Diabetico());
		Receta recAux = new RecetaCompuesta().setSubReceta(recPure).setNombre("Chivito con Pure");
		Ingrediente chivito = new Ingrediente();
		chivito.setNombre("Chivito");
		chivito.setCaloriasIndividuales(10);
		chivito.setCantidad(1);
		recAux.agregarIngrediente(chivito);
		Condimento azucar = new Condimento();
		azucar.setNombre("Azucar");
		azucar.setCantidad(101);
		recAux.agregarCondimento(azucar);
		recEnsalada.agregarCondimento(azucar);
		userPedro.agregarReceta(recAux);

		Filtro filtroCondiciones = new FiltroPorCondicionesPrexistentes(
				new FiltroPorCalorias(new FiltroConsulta(repo)));
		assertEquals(1, filtroCondiciones.consultar(userPedro).size());
	}

	@Test
	public void consultaConFiltroFavoritos() {
		userPedro.agregarFavorito(recChurrasco);
		Filtro filtroFavorito = new FiltroPorFavorito(new FiltroConsulta(repo));
		assertEquals(1, filtroFavorito.consultar(userPedro).size());
	}

	@Test
	public void consultaConFiltroCaloriasCondicionesMedicasYFavoritos() {
		userPedro.agregarCondicionMedica(new Hipertenso());
		userPedro.agregarFavorito(recChurrasco);
		Filtro filtroFavorito = new FiltroPorFavorito(
				new FiltroPorCondicionesPrexistentes(new FiltroPorCalorias(
						new FiltroConsulta(repo))));
		assertEquals(0, filtroFavorito.consultar(userPedro).size());
	}

	@Test
	public void consultaConFiltroIngredientesNoCaros() {
		Ingrediente ingCaro = new Ingrediente();
		ingCaro.setNombre("Lomo");
		ingCaro.setCaloriasIndividuales(100);
		ingCaro.setCantidad(1);
		recChurrasco.agregarIngrediente(ingCaro);
		Filtro filtroCaros = new FiltroPorIngredientesBaratos(
				new FiltroConsulta(repo));
		assertEquals(2, filtroCaros.consultar(userPedro).size());
	}

	@Test
	public void consultaSinFiltroOrdenadoConSolo10() {
		for (Integer i = 0; i < 10; i++) {
			// Agregamos 10 Receta mas iguales
			Receta recAux = new Receta().setNombre("Pure de Papas " + i.toString());
			Ingrediente papa = new Ingrediente();
			papa.setNombre("Papa");
			papa.setCaloriasIndividuales(200);
			papa.setCantidad(2);
			recAux.agregarIngrediente(papa);
			userPedro.agregarReceta(recAux);
		}
		Filtro base = new FiltroConsulta(repo);
		OrdenadorReceta soloDiezPrimeros = new OrdenadorRecetaPorDiezPrimeros(
				new OrdenadorRecetaBase());
		assertEquals(13, base.consultar(userPedro).size());
		assertEquals(10, soloDiezPrimeros.ordenar(base.consultar(userPedro))
				.size());
	}

	@Test
	public void consultaConFiltroCaloriasYCondicionesMedicasOrdenadoDeAParesSolo10() {
		for (Integer i = 0; i < 10; i++) {
			// Agregamos 10 Receta mas iguales
			Receta recAux = new Receta().setNombre("Pure de Papas " + i.toString());
			Ingrediente papa = new Ingrediente();
			papa.setNombre("Papa");
			papa.setCaloriasIndividuales(200);
			papa.setCantidad(2);
			recAux.agregarIngrediente(papa);
			userPedro.agregarReceta(recAux);
		}
		userPedro.agregarCondicionMedica(new Hipertenso());
		Filtro filtroCondiciones = new FiltroPorCondicionesPrexistentes(
				new FiltroPorCalorias(new FiltroConsulta(repo)));
		OrdenadorReceta soloDiezPrimeros = new OrdenadorRecetaPorDiezPrimeros(
				new OrdenadorRecetaBase());
		assertEquals(11, filtroCondiciones.consultar(userPedro).size());
		assertEquals(10,
				soloDiezPrimeros
						.ordenar(filtroCondiciones.consultar(userPedro)).size());
		OrdenadorReceta soloLosParesDeLosDiezPrimeros = new OrdenadorRecetaPorPares(
				soloDiezPrimeros);
		// Los Pure que tienen número son apartir del 2do en adelante
		assertEquals(
				"Pure de Papas 0",
				soloLosParesDeLosDiezPrimeros
						.ordenar(filtroCondiciones.consultar(userPedro)).get(0)
						.getNombre());
		assertEquals(
				"Pure de Papas 2",
				soloLosParesDeLosDiezPrimeros
						.ordenar(filtroCondiciones.consultar(userPedro)).get(1)
						.getNombre());
		assertEquals(
				"Pure de Papas 4",
				soloLosParesDeLosDiezPrimeros
						.ordenar(filtroCondiciones.consultar(userPedro)).get(2)
						.getNombre());
		assertEquals(
				"Pure de Papas 6",
				soloLosParesDeLosDiezPrimeros
						.ordenar(filtroCondiciones.consultar(userPedro)).get(3)
						.getNombre());
		assertEquals(
				"Pure de Papas 8",
				soloLosParesDeLosDiezPrimeros
						.ordenar(filtroCondiciones.consultar(userPedro)).get(4)
						.getNombre());
		assertEquals(
				5,
				soloLosParesDeLosDiezPrimeros.ordenar(
						filtroCondiciones.consultar(userPedro)).size());
	}

	@Test
	public void consultaSinFiltroOrdenadoAlfabeticamente() {
		Filtro base = new FiltroConsulta(repo);
		OrdenadorReceta ordAlfa = new OrdenadorRecetaPorAlfabetico(
				new OrdenadorRecetaBase());
		Receta recAux = new Receta().setNombre("Aaaaaaaaaa");
		Ingrediente papa = new Ingrediente();
		papa.setNombre("Papa");
		papa.setCaloriasIndividuales(200);
		papa.setCantidad(2);
		recAux.agregarIngrediente(papa);
		userPedro.agregarReceta(recAux);
		assertEquals("Aaaaaaaaaa", ordAlfa.ordenar(base.consultar(userPedro))
				.get(0).getNombre());
		assertEquals("Churrasco a la plancha",
				ordAlfa.ordenar(base.consultar(userPedro)).get(1).getNombre());
		assertEquals("Ensalada de Lechuga y Tomate",
				ordAlfa.ordenar(base.consultar(userPedro)).get(2).getNombre());
		assertEquals("Pure de Papas", ordAlfa
				.ordenar(base.consultar(userPedro)).get(3).getNombre());
	}

	@Test
	public void consultaSinFiltroOrdenadoPorCaloriasYAlfabeticamente() {
		Filtro base = new FiltroConsulta(repo);
		OrdenadorReceta ordenador = new OrdenadorRecetaPorCalorias(
				new OrdenadorRecetaPorAlfabetico(new OrdenadorRecetaBase()));
		// Ordena de menor a mayor
		assertEquals("Ensalada de Lechuga y Tomate",
				ordenador.ordenar(base.consultar(userPedro)).get(0).getNombre());
		assertEquals("Pure de Papas",
				ordenador.ordenar(base.consultar(userPedro)).get(1).getNombre());
		assertEquals("Churrasco a la plancha",
				ordenador.ordenar(base.consultar(userPedro)).get(2).getNombre());
	}

	@Test
	public void consultaConFiltroCaloriasOrdenadoPorCaloriasYAlfabeticamente() {
		OrdenadorReceta ordenador = new OrdenadorRecetaPorCalorias(
				new OrdenadorRecetaPorAlfabetico(new OrdenadorRecetaBase()));
		Filtro filtroCalorias = new FiltroPorCalorias(new FiltroConsulta(repo));
		assertEquals(filtroCalorias.consultar(userPedro).size(), 2);
		// Ordena de menor a mayor
		assertEquals("Ensalada de Lechuga y Tomate",
				ordenador.ordenar(filtroCalorias.consultar(userPedro)).get(0)
						.getNombre());
		assertEquals("Pure de Papas",
				ordenador.ordenar(filtroCalorias.consultar(userPedro)).get(1)
						.getNombre());
	}

	@Test
	public void consultaConFiltroIngredientesNoCarosOrdenadoPorCaloriasYAlfabeticamente() {
		Ingrediente ingCaro = new Ingrediente();
		ingCaro.setNombre("Lomo");
		ingCaro.setCaloriasIndividuales(100);
		ingCaro.setCantidad(1);
		recEnsalada.agregarIngrediente(ingCaro);
		OrdenadorReceta ordenador = new OrdenadorRecetaPorCalorias(
				new OrdenadorRecetaPorAlfabetico(new OrdenadorRecetaBase()));
		Filtro filtroCaros = new FiltroPorIngredientesBaratos(
				new FiltroConsulta(repo));
		assertEquals(filtroCaros.consultar(userPedro).size(), 2);
		// Ordena de menor a mayor
		assertEquals("Pure de Papas",
				ordenador.ordenar(filtroCaros.consultar(userPedro)).get(0)
						.getNombre());
		assertEquals("Churrasco a la plancha",
				ordenador.ordenar(filtroCaros.consultar(userPedro)).get(1)
						.getNombre());
	}

	@Test
	public void consultaConFiltroIngredientesNoCarosOrdenadoPorCaloriasYAlfabeticamenteSiendoCeliaco() {
		userPedro.agregarCondicionMedica(new Celiaco());
		Ingrediente ingCaro = new Ingrediente();
		ingCaro.setNombre("Lomo");
		ingCaro.setCaloriasIndividuales(100);
		ingCaro.setCantidad(1);
		recEnsalada.agregarIngrediente(ingCaro);
		OrdenadorReceta ordenador = new OrdenadorRecetaPorCalorias(
				new OrdenadorRecetaPorAlfabetico(new OrdenadorRecetaBase()));
		Filtro filtroCaros = new FiltroPorIngredientesBaratos(
				new FiltroConsulta(repo));
		assertEquals(2, filtroCaros.consultar(userPedro).size());
		// Ordena de menor a mayor
		assertEquals("Pure de Papas",
				ordenador.ordenar(filtroCaros.consultar(userPedro)).get(0)
						.getNombre());
		assertEquals("Churrasco a la plancha",
				ordenador.ordenar(filtroCaros.consultar(userPedro)).get(1)
						.getNombre());
	}

	@Test
	public void consultaSinFiltrosConRecetaPublicaModificadaPorPedro() {
		Receta recetaMod = repo.obtener().get(repo.obtener().size() - 1);
		Ingrediente ingCaro = new Ingrediente();
		ingCaro.setNombre("Lomo");
		ingCaro.setCaloriasIndividuales(100);
		ingCaro.setCantidad(1);
		recetaMod.agregarIngrediente(ingCaro);
		userPedro.modificarRecetaPublica(recetaMod);
		Filtro base = new FiltroConsulta(repo);
		assertEquals(4, base.consultar(userPedro).size());
	}
}
