package consulta;

import java.time.LocalDateTime;
import java.util.List;

import consulta.filtro.Filtro;
import consulta.monitorAsincronico.ConsultaAsincronica;
import consulta.monitorAsincronico.GestorMonitorAsincrono;
import consulta.monitorSincronico.MonitorSincronico;
import consulta.ordenadores.OrdenadorReceta;
import persistencia.GestorMonitorSincronico;
import persistencia.mock.GestorMonitorSincronicoMock;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

public class Consulta {

	private Filtro filtro;
	private OrdenadorReceta ordenador;
	private GestorMonitorSincronico gestorMonitoresSincronicos = new GestorMonitorSincronicoMock();
	private GestorMonitorAsincrono gestorMonitoresAsincronicos = GestorMonitorAsincrono.INSTANCIA;

	public Consulta(Filtro filtro, OrdenadorReceta ordenadorReceta) {
		this.filtro = filtro;
		this.ordenador = ordenadorReceta;
	}

	public List<Receta> consultar(Usuario user) {
		List<Receta> lista = ordenador.ordenar(filtro.consultar(user));
		this.avisarMonitoresSincronicos(lista, user);
		this.avisarMonitoresAsincronicos(filtro.getParametros(), lista, user);
		return lista;
	}

	private void avisarMonitoresAsincronicos(String parametros, List<Receta> lista, Usuario user) {
		this.gestorMonitoresAsincronicos
				.agregarConsulta(new ConsultaAsincronica(parametros, lista, user, LocalDateTime.now()));
	}

	private void avisarMonitoresSincronicos(List<Receta> lista, Usuario user) {
		gestorMonitoresSincronicos.avisar(lista, user);
	}

	public void agregarMonitor(MonitorSincronico obs) {
		gestorMonitoresSincronicos.agregarMonitor(obs);
	}

	public void quitarMonitor(MonitorSincronico obs) {
		gestorMonitoresSincronicos.quitarMonitor(obs);
		obs.reiniciarEstadistica();
	}

	public void setGestorSincronicos(GestorMonitorSincronico gestor) {
		gestorMonitoresSincronicos = gestor;
	}
}
