package persistencia.hibernate;

import java.util.List;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import consulta.monitorAsincronico.ConsultaAsincronica;
import persistencia.ConsultasAsincronicas;

public class ConsultasAsincronicasHibernate implements WithGlobalEntityManager, ConsultasAsincronicas {

	String query = "SELECT c FROM ConsultaAsincronica c";

	@Override
	public void guardar(ConsultaAsincronica consultaAsincronica) {
		entityManager().persist(consultaAsincronica);
	}

	@Override
	public ConsultaAsincronica obtener(ConsultaAsincronica consulta) {
		return entityManager().find(ConsultaAsincronica.class, consulta.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaAsincronica> obtenerTodas() {
		return (List<ConsultaAsincronica>) entityManager().createQuery(query).getResultList();
	}

	@Override
	public void removerTodas() {
		obtenerTodas().forEach(c -> entityManager().remove(c));
	}

	@Override
	public Integer size() {
		return obtenerTodas().size();
	}

	@Override
	public void remover(ConsultaAsincronica consulta) {
		entityManager().remove(consulta);
	}
}
