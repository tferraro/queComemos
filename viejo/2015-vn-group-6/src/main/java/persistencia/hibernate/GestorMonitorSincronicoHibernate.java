package persistencia.hibernate;

import java.util.List;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;
import consulta.monitorSincronico.MonitorSincronico;
import persistencia.GestorMonitorSincronico;

public class GestorMonitorSincronicoHibernate implements GestorMonitorSincronico, WithGlobalEntityManager {

	@Override
	public void avisar(List<Receta> lista, Usuario user) {
		List<MonitorSincronico> monitores = verMonitores();
		monitores.forEach((monitor) -> {
			monitor.avisar(lista, user);
			entityManager().persist(monitor);
		});
	}

	@Override
	public void agregarMonitor(MonitorSincronico obs) {
		entityManager().persist(obs);
	}

	@Override
	public void quitarMonitor(MonitorSincronico obs) {
		entityManager().remove(obs);
		// Obligamos a que no existe mas
		obs.setId(null);
	}

	String queryAll = "SELECT ms FROM MonitorSincronico ms";

	@SuppressWarnings("unchecked")
	@Override
	public List<MonitorSincronico> verMonitores() {
		return (List<MonitorSincronico>) entityManager().createQuery(queryAll).getResultList();
	}
}
