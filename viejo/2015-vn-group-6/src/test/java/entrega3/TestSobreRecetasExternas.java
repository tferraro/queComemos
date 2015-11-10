package entrega3;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import businessModel.condicionMedica.*;
import businessModel.receta.*;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Genero;
import businessModel.usuario.Usuario;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import consulta.filtro.*;
import consulta.ordenadores.*;
import creacionales.BuilderUsuario;
import exceptions.model.receta.ErrorRecetaNoSaludable;
import externalEntities.recetas.*;
import persistencia.mock.RecetarioMock;
import queComemos.entrega3.repositorio.BusquedaRecetas;
import queComemos.entrega3.repositorio.RepoRecetas;

public class TestSobreRecetasExternas {

	private RepositorioExterno repo = RepoRecetasExterno.INSTANCIA;

	@Test
	public void pruebaSobreFormatoDeDatosExternos() {
		RepoRecetas repo = new RepoRecetas();
		BusquedaRecetas busqueda = new BusquedaRecetas();
		Type listType = new TypeToken<ArrayList<RecetaExterna>>() {
		}.getType();
		List<RecetaExterna> listaExterna = new Gson().fromJson(repo.getRecetas(busqueda), listType);
		assertEquals("ensalada caesar", listaExterna.get(0).getNombre());
	}

	@Test
	public void buscarTodasLasRecetasExternas() {
		assertEquals(12, repo.getRecetas(new Receta()).size());
	}

	@Test
	public void buscarRecetaPorNombreCanelones() {
		assertEquals("canelones de ricota y verdura",
				repo.getRecetas(new Receta().setNombre("canelones de ricota y verdura")).get(0).getNombre());
	}

	@Test
	public void buscarRecetasFaciles() {
		Receta patronesBusqueda = new Receta();
		patronesBusqueda.setDificultad(DificultadDePreparacionReceta.FACIL);
		assertEquals(6, repo.getRecetas(patronesBusqueda).size());
	}

	@Test
	public void buscarRecetasDificiles() {
		Receta patronesBusqueda = new Receta();
		patronesBusqueda.setDificultad(DificultadDePreparacionReceta.DIFICIL);
		assertEquals(2, repo.getRecetas(patronesBusqueda).size());
	}

	@Test
	public void buscarRecetasMedias() {
		Receta patronesBusqueda = new Receta();
		patronesBusqueda.setDificultad(DificultadDePreparacionReceta.MEDIA);
		assertEquals(4, repo.getRecetas(patronesBusqueda).size());
	}

	@Test
	public void buscarRecetaConLangostinosComoCriterioYTomate() {
		Receta patronesBusqueda = new Receta();
		Ingrediente langostino = new Ingrediente();
		langostino.setNombre("langostinos");
		langostino.setCantidad(10);
		langostino.setCaloriasIndividuales(89);
		patronesBusqueda.agregarIngrediente(langostino);
		assertEquals(1, repo.getRecetas(patronesBusqueda).size());
		assertTrue(repo.getRecetas(patronesBusqueda).get(0).contieneIngrediente("tomate"));
		assertTrue(repo.getRecetas(patronesBusqueda).get(0).contieneIngrediente("langostinos"));
	}

	@Test
	public void buscarRecetaFacilConTomate() {
		Receta patronesBusqueda = new Receta();
		patronesBusqueda.setDificultad(DificultadDePreparacionReceta.FACIL);
		Ingrediente tomate = new Ingrediente();
		tomate.setNombre("tomate");
		tomate.setCantidad(2);
		tomate.setCaloriasIndividuales(69);
		patronesBusqueda.agregarIngrediente(tomate);
		assertEquals(1, repo.getRecetas(patronesBusqueda).size());
		assertTrue(repo.getRecetas(patronesBusqueda).get(0).contieneIngrediente("tomate"));
	}

	@Test
	public void buscarRecetasExternasYVerificarQueTengaEnLaDescripcionElTiempoDePreparacion() {
		for (Receta r : repo.getRecetas(new Receta())) {
			assertEquals("Tiempo de preparacion: 0", r.getDescripcion());
		}
	}

	@Test
	public void buscarRecetaConAzucarComoCondimento() throws Exception {
		Receta patronesBusqueda = new Receta();
		Condimento azucarr = new Condimento();
		azucarr.setNombre("azucar");
		azucarr.setCantidad(2);
		patronesBusqueda.agregarCondimento(azucarr);
		assertEquals(1, repo.getRecetas(patronesBusqueda).size());
		assertTrue(repo.getRecetas(patronesBusqueda).get(0).contieneCondimento("azucar"));
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void buscarRecetaConSalQueEsMalaParaHipertenso() {
		Receta patronesBusqueda = new Receta();
		Condimento sall = new Condimento();
		sall.setNombre("sal gruesa");

		patronesBusqueda.agregarCondimento(sall);
		assertEquals(1, repo.getRecetas(patronesBusqueda).size());
		assertTrue(repo.getRecetas(patronesBusqueda).get(0).contieneCondimento("sal gruesa"));

		new BuilderUsuario()
				.agregarCamposObligatorios("Pedro", new BigDecimal(89), new BigDecimal(1.85),
						LocalDate.of(2011, 05, 13), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarCondicionMedica(new Hipertenso()).agregarPreferencias("Waffles").compilar()
				.meEsAdecuada(repo.getRecetas(patronesBusqueda).get(0));

	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void buscarRecetaConAzucarQueEsMalaParaDiabeticos() {
		Receta patronesBusqueda = new Receta();
		Condimento azucarr = new Condimento();
		azucarr.setNombre("azucar");
		azucarr.setCantidad(2);
		patronesBusqueda.agregarCondimento(azucarr);

		new BuilderUsuario()
				.agregarCamposObligatorios("MisterX", new BigDecimal(89), new BigDecimal(1.85),
						LocalDate.of(2011, 05, 13), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarCondicionMedica(new Diabetico()).agregarSexo(Genero.MASCULINO).agregarPreferencias("Waffles")
				.compilar().meEsAdecuada(repo.getRecetas(patronesBusqueda).get(0));
	}

	@Test(expected = ErrorRecetaNoSaludable.class)
	public void buscarRecetaConPolloQueEsMalaParaVeganos() throws Exception {
		Receta patronesBusqueda = new Receta();
		Ingrediente pollito = new Ingrediente();
		pollito.setNombre("pollo");
		patronesBusqueda.agregarIngrediente(pollito);
		assertEquals(1, repo.getRecetas(patronesBusqueda).size());

		new BuilderUsuario()
				.agregarCamposObligatorios("MisterX", new BigDecimal(89), new BigDecimal(1.85),
						LocalDate.of(2011, 05, 13), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarCondicionMedica(new Vegano()).agregarSexo(Genero.MASCULINO).agregarPreferencias("Waffles")
				.compilar().meEsAdecuada(repo.getRecetas(patronesBusqueda).get(0));
	}

	@Test
	public void buscarRecetaFacilBuenaParaCeliaco() {
		Receta patronesBusqueda = new Receta();
		patronesBusqueda.setDificultad(DificultadDePreparacionReceta.FACIL);
		new BuilderUsuario()
				.agregarCamposObligatorios("MisterX", new BigDecimal(89), new BigDecimal(1.85),
						LocalDate.of(2011, 05, 13), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarCondicionMedica(new Celiaco()).agregarSexo(Genero.MASCULINO).agregarPreferencias("Waffles")
				.compilar().meEsAdecuada(repo.getRecetas(patronesBusqueda).get(0));
	}

	@Test
	public void RecetasExternasAdecuadasParaUsuarioSinCondicionMedica() throws Exception {
		Usuario userComun = new BuilderUsuario()
				.agregarCamposObligatorios("MisterX", new BigDecimal(89), new BigDecimal(1.85),
						LocalDate.of(2011, 05, 13), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarSexo(Genero.MASCULINO).agregarPreferencias("Waffles").compilar();
		for (Receta r : repo.getRecetas(new Receta())) {
			userComun.meEsAdecuada(r);
		}
	}

	@Test
	public void consultaSinFiltrosUsandoFuenteExterna() {
		Filtro base = new FiltroConsulta(RecetarioMock.INSTANCIA);
		base.agregarFuenteExterna(repo, new Receta());
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").compilar();
		assertEquals(12, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroParaRecetaFavoritaEnsaladaCaesarUsandoFuenteExterna() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").compilar();
		misterX.agregarFavorito(new Receta().setNombre("ensalada caesar"));
		Filtro base = new FiltroPorFavorito(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, new Receta());
		assertEquals(1, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroPorCondicionesParaVeganoUsandoFuenteExterna() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").agregarCondicionMedica(new Vegano()).compilar();
		Receta patronesBusqueda = new Receta();
		Filtro base = new FiltroPorCondicionesPrexistentes(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, patronesBusqueda);
		assertEquals(11, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroPorCondicionesParaDiabeticoFuenteExterna() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").agregarSexo(Genero.MASCULINO).agregarCondicionMedica(new Diabetico())
				.compilar();
		Receta patronesBusqueda = new Receta();
		Filtro base = new FiltroPorCondicionesPrexistentes(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, patronesBusqueda);
		assertEquals(11, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroPorCondicionesCeliacoUsandoFuenteExternaBuscandoRecetas() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").agregarCondicionMedica(new Celiaco()).compilar();
		Receta patronesBusqueda = new Receta();
		Filtro base = new FiltroPorCondicionesPrexistentes(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, patronesBusqueda);
		assertEquals(12, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroPorCondicionesHipertensoUsandoFuenteExterna() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").agregarCondicionMedica(new Hipertenso()).compilar();

		Filtro base = new FiltroPorCondicionesPrexistentes(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, new Receta());
		assertEquals(11, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroCombinadoCondicionesPorFavoritosDeHipertensoUsandoFuenteExterna() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").agregarCondicionMedica(new Hipertenso()).compilar();
		misterX.agregarFavorito(new Receta().setNombre("ensalada caesar"));
		misterX.agregarFavorito(new Receta().setNombre("ensalada lechuga agridulce"));
		misterX.agregarFavorito(new Receta().setNombre("gambas al ajillo"));
		misterX.agregarFavorito(new Receta().setNombre("churrasco a la sal"));

		Filtro base = new FiltroPorCondicionesPrexistentes(
				new FiltroPorFavorito(new FiltroConsulta(RecetarioMock.INSTANCIA)));
		base.agregarFuenteExterna(repo, new Receta());
		assertEquals(3, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroPorCondicionesUsuarioConCondicionesMultiplesUsandoFuenteExterna() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").agregarCondicionMedica(new Hipertenso())
				.agregarCondicionMedica(new Diabetico()).agregarCondicionMedica(new Vegano())
				.agregarSexo(Genero.MASCULINO).compilar();
		Filtro base = new FiltroPorCondicionesPrexistentes(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, new Receta());
		assertEquals(9, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroPorFavoritoParaRecetaRaraQueNoEstaEnLaFuenteExtarna() throws Exception {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").compilar();
		misterX.agregarFavorito(new Receta().setNombre("Rara"));
		Filtro base = new FiltroPorFavorito(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, new Receta());
		assertEquals(0, base.consultar(misterX).size());
	}

	@Test
	public void ConsultaUsandoFiltroPorFavoritoDeDificultadFacil() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").compilar();
		misterX.agregarFavorito(new Receta().setNombre("ensalada caesar"));
		misterX.agregarFavorito(new Receta().setNombre("ensalada lechuga agridulce"));
		misterX.agregarFavorito(new Receta().setNombre("gambas al ajillo"));
		misterX.agregarFavorito(new Receta().setNombre("churrasco a la sal"));
		Receta patronesBusqueda = new Receta();
		patronesBusqueda.setDificultad(DificultadDePreparacionReceta.FACIL);
		Filtro base = new FiltroPorFavorito(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, patronesBusqueda);
		assertEquals(2, base.consultar(misterX).size());
	}

	@Test
	public void consultaUsandoFiltroPorCondicionesCeliacoOrdenadoDiezUsandoFuenteExternaBuscandoRecetas() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").agregarCondicionMedica(new Celiaco()).compilar();
		Receta patronesBusqueda = new Receta();
		Filtro base = new FiltroPorCondicionesPrexistentes(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, patronesBusqueda);
		OrdenadorReceta ordenador = new OrdenadorRecetaPorDiezPrimeros(new OrdenadorRecetaBase());
		assertEquals(10, ordenador.ordenar(base.consultar(misterX)).size());
	}

	@Test
	public void consultaSinFiltrosOrdenandoAlfaUsandoFuenteExterna() {
		Filtro base = new FiltroConsulta(RecetarioMock.INSTANCIA);
		base.agregarFuenteExterna(repo, new Receta());
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").compilar();
		OrdenadorReceta ordenador = new OrdenadorRecetaPorAlfabetico(new OrdenadorRecetaBase());
		assertEquals("canelones de ricota y verdura", ordenador.ordenar(base.consultar(misterX)).get(0).getNombre());
	}

	@Test
	public void consultaSinFiltrosOrdenandoPorCaloriasUsandoFuenteExterna() {
		Filtro base = new FiltroConsulta(RecetarioMock.INSTANCIA);
		base.agregarFuenteExterna(repo, new Receta());
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").compilar();
		OrdenadorReceta ordenador = new OrdenadorRecetaPorCalorias(new OrdenadorRecetaBase());
		assertEquals("ensalada capresse", ordenador.ordenar(base.consultar(misterX)).get(0).getNombre());
	}

	@Test
	public void consultaSinFiltrosOrdenandoPorParesUsandoFuenteExterna() {
		Filtro base = new FiltroConsulta(RecetarioMock.INSTANCIA);
		base.agregarFuenteExterna(repo, new Receta());
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").compilar();
		OrdenadorReceta ordenador = new OrdenadorRecetaPorPares(new OrdenadorRecetaBase());
		assertEquals("ensalada lechuga agridulce", ordenador.ordenar(base.consultar(misterX)).get(0).getNombre());
	}

	@Test
	public void consultaUsandoFiltroPorCondicionesCeliacoOrdenadoDiezYCaloriasUsandoFuenteExternaBuscandoRecetas() {
		Usuario misterX = new BuilderUsuario()
				.agregarCamposObligatorios("MixterX", new BigDecimal(70), new BigDecimal(1.90),
						LocalDate.of(1995, 02, 04), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarPreferencias("papas").agregarCondicionMedica(new Celiaco()).compilar();
		Receta patronesBusqueda = new Receta();
		Filtro base = new FiltroPorCondicionesPrexistentes(new FiltroConsulta(RecetarioMock.INSTANCIA));
		base.agregarFuenteExterna(repo, patronesBusqueda);
		OrdenadorReceta ordenador = new OrdenadorRecetaPorCalorias(
				new OrdenadorRecetaPorDiezPrimeros(new OrdenadorRecetaBase()));
		assertEquals(10, ordenador.ordenar(base.consultar(misterX)).size());
		assertEquals("ensalada capresse", ordenador.ordenar(base.consultar(misterX)).get(0).getNombre());
	}
}
