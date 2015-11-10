package consulta.monitorAsincronico;

import externalEntities.logger.Loggeador;

public class MonitorAsincronicoLogger implements MonitorAsincrono {

	private Loggeador logger;
	public Integer minimaCantidadParaLoggear = 100;

	public MonitorAsincronicoLogger(Loggeador logger) {
		this.logger = logger;
	}

	@Override
	public void ejecutar(ConsultaAsincronica consulta) {
		if (consulta.getLista().size() > minimaCantidadParaLoggear)
			logger.loggearConsulta(consulta);
	}

	public void setMinimaCantidadParaLoggear(Integer minimaCantidadParaLoggear) {
		this.minimaCantidadParaLoggear = minimaCantidadParaLoggear;
	}

	public Integer getMinimaCantidadParaLoggear() {
		return minimaCantidadParaLoggear;
	}

}
