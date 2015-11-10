package consulta.monitorAsincronico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import persistencia.ConsultasAsincronicas;
import persistencia.mock.ConsultasAsincronicasMock;

public enum GestorMonitorAsincrono {
	INSTANCIA;

	private List<MonitorAsincrono> monitoresAsincronos = new ArrayList<MonitorAsincrono>();
	private ConsultasAsincronicas repo = new ConsultasAsincronicasMock();

	public void agregarConsulta(ConsultaAsincronica consultaAsincronica) {
		repo.guardar(consultaAsincronica);
	}

	public void ejecutarMonitores() {
		Iterator<ConsultaAsincronica> it = getConsultas().iterator();
		
		while (it.hasNext()) {
			ConsultaAsincronica consulta = it.next();
			it.remove();
			repo.remover(consulta);
			this.monitoresAsincronos.forEach(mon -> mon.ejecutar(consulta));
		}
	}

	public void agregarMonitorAsincronico(MonitorAsincrono monitor) {
		this.monitoresAsincronos.add(monitor);
	}

	public void removerMonitores() {
		this.monitoresAsincronos.clear();
	}

	public void removerConsultas() {
		this.repo.removerTodas();
	}

	public Integer cantConsultasPendientes() {
		return repo.size();
	}

	public ConsultasAsincronicas getRepo() {
		return repo;
	}

	public void setRepo(ConsultasAsincronicas repo) {
		this.repo = repo;
	}

	public List<ConsultaAsincronica> getConsultas() {
		return repo.obtenerTodas();
	}

}
