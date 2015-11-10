package entrega4;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import consulta.Consulta;
import consulta.filtro.Filtro;
import consulta.filtro.FiltroConsulta;
import consulta.monitorAsincronico.GestorMonitorAsincrono;
import consulta.monitorAsincronico.MonitorAsincronicoLogger;
import consulta.ordenadores.OrdenadorRecetaBase;
import creacionales.BuilderUsuario;
import businessModel.receta.Receta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import externalEntities.recetas.RepoRecetasExterno;
import persistencia.mock.RecetarioMock;
import externalEntities.logger.*;

public class TestSobreActualizacionesAsincronicasLogger {

	private Filtro filtro;
	private Consulta consulta;

	@Before
	public void setUp() {
		filtro = new FiltroConsulta(RecetarioMock.INSTANCIA);
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA,
				new Receta());
		consulta = new Consulta(filtro, new OrdenadorRecetaBase());
		GestorMonitorAsincrono.INSTANCIA.removerMonitores();
	}

	@Test
	public void validarQueLaConsultaDisparaUnLogSiSuperaLaCantidad() {
		LoggeadorMock logger = new LoggeadorMock();
		MonitorAsincronicoLogger monitorLogger = new MonitorAsincronicoLogger(
				logger);
		monitorLogger.setMinimaCantidadParaLoggear(10);

		GestorMonitorAsincrono.INSTANCIA
				.agregarMonitorAsincronico(monitorLogger);

		consulta.consultar(new BuilderUsuario().agregarCamposObligatorios(
				"Homero", new BigDecimal(78), new BigDecimal(1.98),
				LocalDate.of(2011, 04, 1),
				new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO)).compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();

		assertEquals("Estoy loggeando una consulta.", logger.getLoggeado());
	}

	@Test
	public void validarQueLaConsultaDisparaUnLogPeroNoLoggeaPorqueNoSuperaElMinimoDe100() {
		LoggeadorMock logger = new LoggeadorMock();
		MonitorAsincronicoLogger monitorLogger = new MonitorAsincronicoLogger(
				logger);

		GestorMonitorAsincrono.INSTANCIA
				.agregarMonitorAsincronico(monitorLogger);

		consulta.consultar(new BuilderUsuario().agregarCamposObligatorios(
				"Homero", new BigDecimal(78), new BigDecimal(1.98),
				LocalDate.of(2011, 04, 1),
				new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO)).compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();

		assertEquals("Vacio", logger.getLoggeado());
	}

	@Test
	public void validarQueLaConsultaDesapareceTrasEjecutar() {
		LoggeadorMock logger = new LoggeadorMock();
		MonitorAsincronicoLogger monitorLogger = new MonitorAsincronicoLogger(
				logger);
		monitorLogger.setMinimaCantidadParaLoggear(10);

		GestorMonitorAsincrono.INSTANCIA
				.agregarMonitorAsincronico(monitorLogger);

		consulta.consultar(new BuilderUsuario().agregarCamposObligatorios(
				"Homero", new BigDecimal(78), new BigDecimal(1.98),
				LocalDate.of(2011, 04, 1),
				new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO)).compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();

		assertEquals("Estoy loggeando una consulta.", logger.getLoggeado());
		assertEquals(0,
				GestorMonitorAsincrono.INSTANCIA.cantConsultasPendientes(), 0);
	}

	// @Test
	public void testDeLoggerPosta() {
		Loggeador logger = new LoggeadorSLF4J();
		MonitorAsincronicoLogger monitorLogger = new MonitorAsincronicoLogger(
				logger);
		monitorLogger.setMinimaCantidadParaLoggear(10);

		GestorMonitorAsincrono.INSTANCIA
				.agregarMonitorAsincronico(monitorLogger);

		consulta.consultar(new BuilderUsuario().agregarCamposObligatorios(
				"Homero", new BigDecimal(78), new BigDecimal(1.98),
				LocalDate.of(2011, 04, 1),
				new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO)).compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
	}
}
