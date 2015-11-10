package entrega4;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import consulta.Consulta;
import consulta.filtro.Filtro;
import consulta.filtro.FiltroConsulta;
import consulta.filtro.FiltroPorCondicionesPrexistentes;
import consulta.monitorAsincronico.GestorMonitorAsincrono;
import consulta.monitorAsincronico.MonitorAsincronoFavorito;
import consulta.ordenadores.OrdenadorRecetaBase;
import creacionales.BuilderUsuario;
import externalEntities.recetas.RepoRecetasExterno;
import persistencia.mock.RecetarioMock;
import businessModel.condicionMedica.Diabetico;
import businessModel.condicionMedica.Hipertenso;
import businessModel.condicionMedica.Vegano;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Genero;
import businessModel.usuario.Usuario;

public class TestsSobreActualizacionesAsincronicasFavoritos {

	private RecetarioMock repoRecetas = RecetarioMock.INSTANCIA;
	private Usuario usuarioFavorito;
	private Filtro filtro;
	private Receta rececitaPublica;
	private Receta recetaPrivada;
	private Receta recetaFavorita;

	@Before
	public void setUp() {
		repoRecetas.removerTodasLasRecetas();
		GestorMonitorAsincrono.INSTANCIA.agregarMonitorAsincronico(
				new MonitorAsincronoFavorito());
		
		usuarioFavorito = new BuilderUsuario()
		.agregarCamposObligatorios("Pedrito", new BigDecimal(78),
				new BigDecimal(1.98), LocalDate.of(2011, 04, 1),
				new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
		.selecionarOpcionConsultasFavoritas().compilar();
		
		filtro = new FiltroConsulta(RecetarioMock.INSTANCIA);
		
		rececitaPublica = new Receta().setNombre("La Publica");
		Ingrediente ingredienteX = new Ingrediente();
		ingredienteX.setNombre("X");
		ingredienteX.setCaloriasIndividuales(15);
		rececitaPublica.agregarIngrediente(ingredienteX);
		
		recetaPrivada = new Receta().setNombre("La privada");
		recetaPrivada.agregarIngrediente(ingredienteX);
		
		recetaFavorita = new Receta().setNombre("La favorita");
		recetaFavorita.agregarIngrediente(ingredienteX);
	}
	
	@Test
	public void usuarioQueEligioOpcionRecetasConsultadasTodasFavoritas(){
		assertTrue(usuarioFavorito.getOpcionConsultasFavoritas());
	}

	@Test
	public void consultaDeRecetasExternasConMonitorDeFavorito() {
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		Consulta consulta = new Consulta(filtro, new OrdenadorRecetaBase());
		consulta.consultar(usuarioFavorito);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(12, usuarioFavorito.getHistorialRecetas().size());
	}

	@Test
	public void consultaDeRecetasExternasConMonitorFavoritoAUsuarioQueNoEligioOpcionConsultasFav() {
		Usuario user = new BuilderUsuario().agregarCamposObligatorios(
				"Pedrito", new BigDecimal(78), new BigDecimal(1.98),
				LocalDate.of(2011, 04, 1),
				new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO)).compilar();
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		Consulta consulta = new Consulta(filtro, new OrdenadorRecetaBase());
		consulta.consultar(user);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(0, user.getHistorialRecetas().size());
	}

	@Test
	public void seConsulta2VecesPorLasRecetasExternasYLasRecetasFavoritasNoSeRepiten() {
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		Consulta consulta = new Consulta(filtro, new OrdenadorRecetaBase());
		consulta.consultar(usuarioFavorito);
		consulta.consultar(usuarioFavorito);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(12, usuarioFavorito.getHistorialRecetas().size());
	}

	@Test
	public void consultaARecetasExternasUsandoMonitorPorFavoritosConUsuarioConRecetasFavoritasDeAntes() {
		usuarioFavorito.agregarFavorito(recetaFavorita);
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		Consulta consulta = new Consulta(filtro, new OrdenadorRecetaBase());
		consulta.consultar(usuarioFavorito);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(13, usuarioFavorito.getHistorialRecetas().size());
	}

	@Test
	public void consultaARecetasUsandoMonitorFavoritoFiltrandoPorCondicionVegano() {
		 Usuario usuarioVegano = new BuilderUsuario()
				.agregarCamposObligatorios("Pedrito", new BigDecimal(78),
						new BigDecimal(1.98), LocalDate.of(2011, 04, 1),
						new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.selecionarOpcionConsultasFavoritas()
				.agregarCondicionMedica(new Vegano()).compilar();
		Filtro filtroCondiciones = new FiltroPorCondicionesPrexistentes(
				filtro);
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		Consulta consulta = new Consulta(filtroCondiciones, new OrdenadorRecetaBase());
		consulta.consultar(usuarioVegano);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(11, usuarioVegano.getHistorialRecetas().size());
	}
	
	@Test
	public void consultaARecetasUsandoMonitorFavoritoFiltrandoPorCondicionDiabetico() {
		 Usuario usuarioDiabetico = new BuilderUsuario()
				.agregarCamposObligatorios("Pedrito", new BigDecimal(78),
						new BigDecimal(1.98), LocalDate.of(2011, 04, 1),
						new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.selecionarOpcionConsultasFavoritas()
				.agregarSexo(Genero.MASCULINO)
				.agregarPreferencias("comida china")
				.agregarCondicionMedica(new Diabetico()).compilar();
		Filtro filtroCondiciones = new FiltroPorCondicionesPrexistentes(
				filtro);
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		Consulta consulta = new Consulta(filtroCondiciones, new OrdenadorRecetaBase());
		consulta.consultar(usuarioDiabetico);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(11, usuarioDiabetico.getHistorialRecetas().size());
	}
	
	@Test
	public void consultaARecetasUsandoMonitorFavoritoFiltrandoPorCondicionHipertenso() {
		 Usuario usuarioHipertenso = new BuilderUsuario()
				.agregarCamposObligatorios("Pedrito", new BigDecimal(78),
						new BigDecimal(1.98), LocalDate.of(2011, 04, 1),
						new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.selecionarOpcionConsultasFavoritas()
				.agregarPreferencias("comida china")
				.agregarCondicionMedica(new Hipertenso()).compilar();
		Filtro filtroCondiciones = new FiltroPorCondicionesPrexistentes(
				filtro);
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		Consulta consulta = new Consulta(filtroCondiciones, new OrdenadorRecetaBase());
		consulta.consultar(usuarioHipertenso);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(11, usuarioHipertenso.getHistorialRecetas().size());
	}

	@Test
	public void consultaARecetasRepoDelSistemaUsandoMonitorFavorito(){
		repoRecetas.guardarPublica(rececitaPublica);
		Consulta consulta = new Consulta(filtro, new OrdenadorRecetaBase());

		consulta.consultar(usuarioFavorito);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(1, usuarioFavorito.getHistorialRecetas().size());
	}
	
	@Test
	public void consultaRecetasPropiasMasPublicasYExternas(){
		usuarioFavorito.agregarReceta(recetaPrivada);
		
		repoRecetas.guardarPublica(rececitaPublica);
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		Consulta consulta = new Consulta(filtro, new OrdenadorRecetaBase());

		consulta.consultar(usuarioFavorito);
		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(14, usuarioFavorito.getHistorialRecetas().size());
	}
}
