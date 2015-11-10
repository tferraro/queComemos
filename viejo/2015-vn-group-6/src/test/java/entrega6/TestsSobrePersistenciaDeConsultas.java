package entrega6;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import businessModel.condicionMedica.Vegano;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.temporada.TodoElAnio;
import businessModel.usuario.Genero;
import businessModel.usuario.Usuario;
import consulta.Consulta;
import consulta.filtro.Filtro;
import consulta.filtro.FiltroConsulta;
import consulta.monitorAsincronico.GestorMonitorAsincrono;
import consulta.monitorAsincronico.MonitorAsincronicoLogger;
import consulta.monitorSincronico.MonitorConsultasPorHora;
import consulta.monitorSincronico.MonitorRecetasMasConsultadas;
import consulta.monitorSincronico.MonitorRecetasMasConsultadasPorSexo;
import consulta.monitorSincronico.MonitorVeganoConsultaRecetaDificil;
import consulta.ordenadores.OrdenadorRecetaBase;
import creacionales.BuilderReceta;
import creacionales.BuilderUsuario;
import externalEntities.logger.LoggeadorMock;
import persistencia.GestorCuentas;
import persistencia.GestorMonitorSincronico;
import persistencia.Recetario;
import persistencia.hibernate.GestorCuentasHibernate;
import persistencia.hibernate.GestorMonitorSincronicoHibernate;
import persistencia.hibernate.RecetarioHibernate;
import persistencia.mock.GestorMonitorSincronicoMock;

public class TestsSobrePersistenciaDeConsultas extends AbstractPersistenceTest implements WithGlobalEntityManager {

	private Filtro filtro;
	private Consulta consulta;
	private GestorMonitorAsincrono gestor = GestorMonitorAsincrono.INSTANCIA;
	private Usuario pedro;

	@Before
	public void setUp() {
		Recetario repoRecetas = RecetarioHibernate.INSTANCIA();
		GestorCuentas usuarios = new GestorCuentasHibernate();
		for (Integer i = 0; i < 10; i++)
			repoRecetas.guardarPublica(new BuilderReceta().agregarNombre("Arroz con Leche " + i.toString())
					.agregarDescripcion("Arroz con leche").agregarTemporada(new TodoElAnio())
					.agregarDificultad(DificultadDePreparacionReceta.FACIL).agregarIngrediente("Leche", 10, 60)
					.agregarIngrediente("Arroz", 100, 1).agregarCondimento("Sal", 10).compilar());
		pedro = new BuilderUsuario()
				.agregarCamposObligatorios("Pedro", new BigDecimal(95), new BigDecimal(1.86), LocalDate.of(1993, 3, 6),
						new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarSexo(Genero.MASCULINO).agregarCondicionMedica(new Vegano()).compilar();
		pedro.agregarReceta(new BuilderReceta().agregarNombre("Arroz con Leche Sexual")
				.agregarDescripcion("Arroz con leche").agregarTemporada(new TodoElAnio())
				.agregarDificultad(DificultadDePreparacionReceta.DIFICIL).agregarIngrediente("Leche", 10, 60)
				.agregarIngrediente("Arroz", 100, 1).agregarCondimento("Sal", 10).compilar());
		usuarios.guardar(pedro);
		filtro = new FiltroConsulta(repoRecetas);
		consulta = new Consulta(filtro, new OrdenadorRecetaBase());
		gestor.removerMonitores();
		gestor.removerConsultas();
	}

	@Test
	public void persistirConsultaAsincronicaChequeandoElLoggerMock() {
		LoggeadorMock logger = new LoggeadorMock();
		MonitorAsincronicoLogger monitorLogger = new MonitorAsincronicoLogger(logger);
		monitorLogger.setMinimaCantidadParaLoggear(-1);
		gestor.agregarMonitorAsincronico(monitorLogger);
		consulta.consultar(
				new BuilderUsuario()
						.agregarCamposObligatorios("Homero", new BigDecimal(78), new BigDecimal(1.98),
								LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
						.compilar());
		assertEquals(1, gestor.getConsultas().size());
		gestor.ejecutarMonitores();
		assertEquals(0, gestor.getConsultas().size());
		assertEquals("Estoy loggeando una consulta.", logger.getLoggeado());
	}

	@Test
	public void chequearPersistenciaMonitorConsultaHora() {
		GestorMonitorSincronico gestor = new GestorMonitorSincronicoHibernate();
		gestor.agregarMonitor(MonitorConsultasPorHora.INSTANCIA());
		assertEquals(1, gestor.verMonitores().size());
		assertEquals(MonitorConsultasPorHora.INSTANCIA(), gestor.verMonitores().get(0));
		gestor.quitarMonitor(MonitorConsultasPorHora.INSTANCIA());
		assertEquals(0, gestor.verMonitores().size());
	}

	@Test
	public void chequearPersistenciaMonitorMasRecetasConsultadas() {
		GestorMonitorSincronico gestor = new GestorMonitorSincronicoHibernate();
		gestor.agregarMonitor(MonitorRecetasMasConsultadas.INSTANCIA());
		assertEquals(1, gestor.verMonitores().size());
		assertEquals(MonitorRecetasMasConsultadas.INSTANCIA(), gestor.verMonitores().get(0));
		gestor.quitarMonitor(MonitorRecetasMasConsultadas.INSTANCIA());
		assertEquals(0, gestor.verMonitores().size());
	}

	@Test
	public void chequearPersistenciaMonitorMasRecetasConsultadasPorSexo() {
		GestorMonitorSincronico gestor = new GestorMonitorSincronicoHibernate();
		gestor.agregarMonitor(MonitorRecetasMasConsultadasPorSexo.INSTANCIA());
		assertEquals(1, gestor.verMonitores().size());
		assertEquals(MonitorRecetasMasConsultadasPorSexo.INSTANCIA(), gestor.verMonitores().get(0));
		gestor.quitarMonitor(MonitorRecetasMasConsultadasPorSexo.INSTANCIA());
		assertEquals(0, gestor.verMonitores().size());
	}

	@Test
	public void chequearPersistenciaMonitorVeganos() {
		GestorMonitorSincronico gestor = new GestorMonitorSincronicoHibernate();
		gestor.agregarMonitor(MonitorVeganoConsultaRecetaDificil.INSTANCIA());
		assertEquals(1, gestor.verMonitores().size());
		assertEquals(MonitorVeganoConsultaRecetaDificil.INSTANCIA(), gestor.verMonitores().get(0));
		gestor.quitarMonitor(MonitorVeganoConsultaRecetaDificil.INSTANCIA());
		assertEquals(0, gestor.verMonitores().size());
	}

	@Test
	public void probarPersistenciaConUnVegano() {
		GestorMonitorSincronico gestor = new GestorMonitorSincronicoHibernate();
		consulta.setGestorSincronicos(gestor);
		consulta.agregarMonitor(MonitorVeganoConsultaRecetaDificil.INSTANCIA());
		consulta.consultar(pedro);
		consulta.consultar(pedro);
		MonitorVeganoConsultaRecetaDificil monitor = (MonitorVeganoConsultaRecetaDificil) gestor.verMonitores().get(0);
		assertEquals(1, monitor.cuantosVeganosConsultaronRecetasDificiles(), 0);
		consulta.setGestorSincronicos(new GestorMonitorSincronicoMock());
	}

}
