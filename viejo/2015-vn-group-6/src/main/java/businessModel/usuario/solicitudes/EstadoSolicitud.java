package businessModel.usuario.solicitudes;

import java.time.LocalDate;

public enum EstadoSolicitud {
	PENDIENTE, APROBADA, RECHAZADA;

	private LocalDate fecha;
	private String razon = null;

	public EstadoSolicitud setParameters() {
		this.fecha = LocalDate.now();
		return this;
	}

	public EstadoSolicitud setParameters(String text) {
		this.fecha = LocalDate.now();
		this.razon = text;
		return this;
	}

	public LocalDate getFecha() {
		return this.fecha;
	}

	public String getRazon() {
		return razon;
	}
}
