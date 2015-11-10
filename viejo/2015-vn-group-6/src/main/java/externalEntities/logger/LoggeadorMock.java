package externalEntities.logger;

import consulta.monitorAsincronico.ConsultaAsincronica;

public class LoggeadorMock implements Loggeador {

	String loggeado = "Vacio";

	public void loggearConsulta(ConsultaAsincronica consulta) {
		this.loggeado = "Estoy loggeando una consulta.";
	}

	public String getLoggeado() {
		return loggeado;
	}
}
