package persistencia;

import java.util.List;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;
import consulta.monitorSincronico.MonitorSincronico;

public interface GestorMonitorSincronico {

	void avisar(List<Receta> lista, Usuario user);

	void agregarMonitor(MonitorSincronico obs);

	void quitarMonitor(MonitorSincronico obs);

	List<MonitorSincronico> verMonitores();
}
