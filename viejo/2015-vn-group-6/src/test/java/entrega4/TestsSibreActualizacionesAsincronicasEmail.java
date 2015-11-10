package entrega4;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import consulta.Consulta;
import consulta.filtro.Filtro;
import consulta.filtro.FiltroConsulta;
import consulta.monitorAsincronico.GestorMonitorAsincrono;
import consulta.monitorAsincronico.MonitorAsincronoMail;
import consulta.ordenadores.OrdenadorRecetaBase;
import creacionales.BuilderUsuario;
import exceptions.model.ErrorMonitorAsincronico;
import externalEntities.mail.MailSenderGmail;
import externalEntities.mail.MailSenderMock;
import externalEntities.recetas.RepoRecetasExterno;
import persistencia.mock.RecetarioMock;
import businessModel.receta.Receta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Usuario;

public class TestsSibreActualizacionesAsincronicasEmail {

	private Filtro filtro;
	private Consulta consulta;

	@Before
	public void setUp() {
		filtro = new FiltroConsulta(RecetarioMock.INSTANCIA);
		filtro.agregarFuenteExterna(RepoRecetasExterno.INSTANCIA, new Receta());
		consulta = new Consulta(filtro, new OrdenadorRecetaBase());
		GestorMonitorAsincrono.INSTANCIA.removerMonitores();
	}

	@Test
	public void validarQueLaConsultaDeJContardoDisparoMail() {
		MailSenderMock mailMock = new MailSenderMock();
		GestorMonitorAsincrono.INSTANCIA.agregarMonitorAsincronico(new MonitorAsincronoMail(mailMock));

		consulta.consultar(
				new BuilderUsuario()
						.agregarCamposObligatorios("jcontardo", new BigDecimal(78), new BigDecimal(1.98),
								LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
						.compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(
				"Este es un email enviado al usuario jcontardo por la realizacion de una consulta.\nFiltros <>\nCantidad de Resultados: 12",
				mailMock.getMensaje());
	}

	@Test
	public void validarQueLaConsultaDesapareceTrasEjecutar() {
		MailSenderMock mailMock = new MailSenderMock();
		GestorMonitorAsincrono.INSTANCIA.agregarMonitorAsincronico(new MonitorAsincronoMail(mailMock));

		consulta.consultar(
				new BuilderUsuario()
						.agregarCamposObligatorios("jcontardo", new BigDecimal(78), new BigDecimal(1.98),
								LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
						.compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals(0, GestorMonitorAsincrono.INSTANCIA.cantConsultasPendientes(), 0);
	}

	@Test
	public void validarQueLaConsultaDeXNoDisparoMail() {
		MailSenderMock mailMock = new MailSenderMock();
		GestorMonitorAsincrono.INSTANCIA.agregarMonitorAsincronico(new MonitorAsincronoMail(mailMock));

		consulta.consultar(
				new BuilderUsuario()
						.agregarCamposObligatorios("Gerardo", new BigDecimal(78), new BigDecimal(1.98),
								LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
						.compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		assertEquals("", mailMock.getMensaje());
	}

	@Test
	public void errorPorEmailInexistente() {
		GestorMonitorAsincrono.INSTANCIA.agregarMonitorAsincronico(new MonitorAsincronoMail(MailSenderGmail.INSTANCIA));
		consulta.consultar(
				new BuilderUsuario()
						.agregarCamposObligatorios("jcontardo", new BigDecimal(78), new BigDecimal(1.98),
								LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
						.compilar());
		try {
			GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
		} catch (ErrorMonitorAsincronico e) {
			assertEquals("Error al Enviar el Mail. Alguno de los parametros del email es null", e.getMessage());
			return;
		}

		fail("No se lanzo ninguna excepcion");
	}

	@Test(expected = ErrorMonitorAsincronico.class)
	public void errorPorEmailErroneoYUserInvalido() {
		GestorMonitorAsincrono.INSTANCIA.agregarMonitorAsincronico(new MonitorAsincronoMail(MailSenderGmail.INSTANCIA));
		Usuario userConMalMail = new BuilderUsuario().agregarCamposObligatorios("jcontardo", new BigDecimal(78),
				new BigDecimal(1.98), LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.compilar();
		userConMalMail.setMail("dds.utn.group6g.gmail.com");
		consulta.consultar(userConMalMail);

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
	}

	@Test
	public void pruebaRealConUserNoEnviableSobreEmailVerdadero() {
		GestorMonitorAsincrono.INSTANCIA.agregarMonitorAsincronico(new MonitorAsincronoMail(MailSenderGmail.INSTANCIA));
		consulta.consultar(new BuilderUsuario()
				.agregarCamposObligatorios("Gerardo", new BigDecimal(78), new BigDecimal(1.98),
						LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarMail("dds.utn.group6@gmail.com").compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
	}

	// @Test
	public void pruebaDeFuegoSobreEmailVerdadero() {
		GestorMonitorAsincrono.INSTANCIA.agregarMonitorAsincronico(new MonitorAsincronoMail(MailSenderGmail.INSTANCIA));
		consulta.consultar(new BuilderUsuario()
				.agregarCamposObligatorios("jcontardo", new BigDecimal(78), new BigDecimal(1.98),
						LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarMail("dds.utn.group6@gmail.com").compilar());

		GestorMonitorAsincrono.INSTANCIA.ejecutarMonitores();
	}
}
