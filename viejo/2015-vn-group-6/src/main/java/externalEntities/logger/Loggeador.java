package externalEntities.logger;

import consulta.monitorAsincronico.ConsultaAsincronica;

public interface Loggeador {

	public void loggearConsulta(ConsultaAsincronica consulta);
}
