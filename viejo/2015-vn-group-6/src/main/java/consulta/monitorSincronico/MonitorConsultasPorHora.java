package consulta.monitorSincronico;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

@Entity
@DiscriminatorValue("H")
public class MonitorConsultasPorHora extends MonitorSincronico {

	@Transient
	private static MonitorConsultasPorHora instancia = new MonitorConsultasPorHora();
	private Integer[] horarios;

	private MonitorConsultasPorHora() {
		this.horarios = new Integer[24];
	}

	public static MonitorConsultasPorHora INSTANCIA() {
		return instancia;
	}

	public void avisar(List<Receta> lista, Usuario user) {
		horarios[LocalDateTime.now().getHour()]++;
	}

	public void reiniciarEstadistica() {
		for (Integer i = 0; i < 24; i++)
			this.horarios[i] = 0;
	}

	public Integer consultasPorHora(int hora) {
		return this.horarios[hora];
	}
}
