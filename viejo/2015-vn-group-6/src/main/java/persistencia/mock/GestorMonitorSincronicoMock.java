package persistencia.mock;

import java.util.ArrayList;
import java.util.List;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;
import consulta.monitorSincronico.MonitorSincronico;
import persistencia.GestorMonitorSincronico;

public class GestorMonitorSincronicoMock implements GestorMonitorSincronico {

	private List<MonitorSincronico> observadores = new ArrayList<MonitorSincronico>();

	@Override
	public void avisar(List<Receta> lista, Usuario user) {
		observadores.forEach(o -> o.avisar(lista, user));
	}

	@Override
	public void agregarMonitor(MonitorSincronico obs) {
		observadores.add(obs);
	}

	@Override
	public void quitarMonitor(MonitorSincronico obs) {
		observadores.remove(obs);
	}

	@Override
	public List<MonitorSincronico> verMonitores() {
		return observadores;
	}

}
