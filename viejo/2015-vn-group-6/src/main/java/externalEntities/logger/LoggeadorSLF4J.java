package externalEntities.logger;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import consulta.monitorAsincronico.ConsultaAsincronica;

public class LoggeadorSLF4J implements Loggeador {

	Logger logger;

	public LoggeadorSLF4J() {
		logger = LoggerFactory.getLogger(LoggeadorSLF4J.class);
	}

	@Override
	public void loggearConsulta(ConsultaAsincronica consulta) {
		String usr = consulta.getUser().getNombre();
		consulta.getLista().stream().forEach(receta -> logger
				.info(obtenerFechaConFormato(consulta) + " " + usr + " consulto receta: " + receta.getNombre()));

	}

	private String obtenerFechaConFormato(ConsultaAsincronica consulta) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return consulta.getFecha().format(format);
	}
}
