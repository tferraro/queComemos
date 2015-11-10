package entrega3;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import consulta.Consulta;
import consulta.filtro.Filtro;
import consulta.filtro.FiltroConsulta;
import consulta.monitorSincronico.MonitorConsultasPorHora;
import consulta.monitorSincronico.MonitorRecetasMasConsultadas;
import consulta.monitorSincronico.MonitorRecetasMasConsultadasPorSexo;
import consulta.monitorSincronico.MonitorVeganoConsultaRecetaDificil;
import consulta.ordenadores.OrdenadorRecetaBase;
import creacionales.BuilderUsuario;
import externalEntities.recetas.RepoRecetasExterno;
import externalEntities.recetas.RepositorioExterno;
import persistencia.mock.RecetarioMock;
import businessModel.condicionMedica.Vegano;
import businessModel.receta.*;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Genero;
import businessModel.usuario.Usuario;

public class TestSobreEstadisticas {

	private RepositorioExterno repoExterno = RepoRecetasExterno.INSTANCIA;
	private Consulta consulta;
	private Usuario userPedro;
	private Receta recPure, recHigado;
	private RecetarioMock repo = RecetarioMock.INSTANCIA;
	private Filtro filtro;
	private BuilderUsuario builder;
	private Usuario cristina;
	private MonitorConsultasPorHora obsConsultasPorHora;
	private MonitorRecetasMasConsultadas obsRecetasMasConsultadas;
	private MonitorRecetasMasConsultadasPorSexo obsRecetasMasConsultadasPorSexo;
	private MonitorVeganoConsultaRecetaDificil obsVeganoConsultaRecetaDificil;

	@Before
	public void setUp() {
		repo.removerTodasLasRecetas();
		recPure = new Receta().setNombre("Pure de Papas");
		Ingrediente papa = new Ingrediente();
		papa.setNombre("Papa");
		papa.setCaloriasIndividuales(200);
		papa.setCantidad(2);
		recPure.agregarIngrediente(papa);
		recHigado = new Receta().setNombre("Higado encebollado");
		Ingrediente cebolla = new Ingrediente();
		cebolla.setNombre("Cebolla");
		cebolla.setCaloriasIndividuales(200);
		cebolla.setCantidad(2);
		recHigado.agregarIngrediente(cebolla);
		recHigado.setDificultad(DificultadDePreparacionReceta.DIFICIL);

		userPedro = (builder = new BuilderUsuario().agregarCamposObligatorios("Pedro", new BigDecimal(95),
				new BigDecimal(1.86), LocalDate.of(1993, 3, 6), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarSexo(Genero.MASCULINO)).compilar();

		cristina = builder.setNombre("Cristina").agregarSexo(Genero.FEMENINO).compilar();

		cristina.agregarCondicionMedica(new Vegano());

		userPedro.agregarReceta(recPure);
		cristina.agregarReceta(recHigado);

		filtro = new FiltroConsulta(repo);
		consulta = new Consulta(filtro, new OrdenadorRecetaBase());

		// OBSERVADORES
		obsConsultasPorHora = MonitorConsultasPorHora.INSTANCIA();
		obsConsultasPorHora.reiniciarEstadistica();
		obsRecetasMasConsultadas = MonitorRecetasMasConsultadas.INSTANCIA();
		obsRecetasMasConsultadas.reiniciarEstadistica();
		obsRecetasMasConsultadasPorSexo = MonitorRecetasMasConsultadasPorSexo.INSTANCIA();
		obsRecetasMasConsultadasPorSexo.reiniciarEstadistica();
		obsVeganoConsultaRecetaDificil = MonitorVeganoConsultaRecetaDificil.INSTANCIA();
		obsVeganoConsultaRecetaDificil.reiniciarEstadistica();

		consulta.agregarMonitor(obsConsultasPorHora);
		consulta.agregarMonitor(obsRecetasMasConsultadas);
		consulta.agregarMonitor(obsRecetasMasConsultadasPorSexo);
		consulta.agregarMonitor(obsVeganoConsultaRecetaDificil);
	}

	@Test
	public void cantidadDeConsultasPorHora() {
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		assertEquals(3, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);

		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		assertEquals(5, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);

		obsConsultasPorHora.reiniciarEstadistica();
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		assertEquals(4, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);
	}

	@Test
	public void cantidadDeConsultasPorHoraPorDosConsultas() {
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		Consulta consulta2 = new Consulta(filtro, new OrdenadorRecetaBase());
		consulta2.agregarMonitor(MonitorConsultasPorHora.INSTANCIA());
		consulta2.consultar(userPedro);
		consulta2.consultar(userPedro);
		consulta2.consultar(userPedro);
		assertEquals(6, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);
	}

	@Test
	public void estadisticaRecetasMasConsultadas() {
		consulta.consultar(userPedro);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		assertEquals("Pure de Papas", obsRecetasMasConsultadas.recetaMasConsultada().getReceta());
	}

	@Test
	public void estadisticaRecetasMasConsultadasSinConsultas() {
		assertEquals(null, obsRecetasMasConsultadas.recetaMasConsultada());
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorMasculinosSinConsultas() {
		assertEquals(null, obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.MASCULINO));
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorFemeninoSinConsultas() {
		assertEquals(null, obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.FEMENINO));
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorHombres() {
		consulta.consultar(userPedro);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals("Pure de Papas",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.MASCULINO).getReceta());
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorHombresYPorHora() {
		consulta.consultar(userPedro);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals("Pure de Papas",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.MASCULINO).getReceta());
		assertEquals(3, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorMujeres() {
		consulta.consultar(cristina);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals("Higado encebollado",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.FEMENINO).getReceta());
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorMujeresPorHora() {
		consulta.consultar(cristina);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		consulta.consultar(cristina);
		assertEquals("Higado encebollado",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.FEMENINO).getReceta());
		assertEquals(7, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorMujeresYPorHombres() {
		consulta.consultar(cristina);
		consulta.consultar(userPedro);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals("Higado encebollado",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.FEMENINO).getReceta());
		assertEquals("Pure de Papas",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.MASCULINO).getReceta());
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorMujeresYPorHombresMasPorHora() {
		consulta.consultar(cristina);
		consulta.consultar(userPedro);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals("Higado encebollado",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.FEMENINO).getReceta());
		assertEquals("Pure de Papas",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.MASCULINO).getReceta());
		assertEquals(6, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorMujeresYPorHombresMasPorHoraMasRecetaMasConsultada() {
		consulta.consultar(cristina);
		consulta.consultar(userPedro);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals("Higado encebollado",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.FEMENINO).getReceta());
		assertEquals("Pure de Papas",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.MASCULINO).getReceta());
		assertEquals(6, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);
		assertEquals("Pure de Papas", obsRecetasMasConsultadas.recetaMasConsultada().getReceta());
	}

	@Test
	public void cuantosVeganosConsultan() {
		consulta.consultar(cristina);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals(1, obsVeganoConsultaRecetaDificil.cuantosVeganosConsultaronRecetasDificiles(), 0);
	}

	@Test
	public void cambiazoDeVeganoPrevioAUnaConsulta() {
		consulta.consultar(cristina);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		userPedro.agregarCondicionMedica(new Vegano());
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals(2, obsVeganoConsultaRecetaDificil.cuantosVeganosConsultaronRecetasDificiles(), 0);
	}

	@Test
	public void cambiazoDeVeganoPostConsultaYNoLoReconoce() {
		consulta.consultar(cristina);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		userPedro.agregarCondicionMedica(new Vegano());
		assertEquals(1, obsVeganoConsultaRecetaDificil.cuantosVeganosConsultaronRecetasDificiles(), 0);
	}

	@Test
	public void estadisticaRecetasMasConsultadasPorMujeresYPorHombresMasPorHoraMasRecetaMasConsultadaConVegano() {
		consulta.consultar(cristina);
		consulta.consultar(userPedro);
		filtro.agregarFuenteExterna(repoExterno, new Receta());
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(userPedro);
		consulta.consultar(cristina);
		assertEquals("Higado encebollado",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.FEMENINO).getReceta());
		assertEquals("Pure de Papas",
				obsRecetasMasConsultadasPorSexo.recetaMasConsultadaPorGenero(Genero.MASCULINO).getReceta());
		assertEquals(6, obsConsultasPorHora.consultasPorHora(LocalDateTime.now().getHour()), 0);
		assertEquals("Pure de Papas", obsRecetasMasConsultadas.recetaMasConsultada().getReceta());
		assertEquals(1, obsVeganoConsultaRecetaDificil.cuantosVeganosConsultaronRecetasDificiles(), 0);
	}

}
