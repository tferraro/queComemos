package persistencia.mock;

import java.util.ArrayList;
import java.util.List;

import consulta.monitorAsincronico.ConsultaAsincronica;
import persistencia.ConsultasAsincronicas;

public class ConsultasAsincronicasMock implements ConsultasAsincronicas {

	private List<ConsultaAsincronica> consultas = new ArrayList<ConsultaAsincronica>();

	@Override
	public void guardar(ConsultaAsincronica consultaAsincronica) {
		consultas.add(consultaAsincronica);
	}

	@Override
	public ConsultaAsincronica obtener(ConsultaAsincronica consulta) {
		return consultas.get(consultas.indexOf(consulta));
	}

	@Override
	public List<ConsultaAsincronica> obtenerTodas() {
		return consultas;
	}

	@Override
	public void removerTodas() {
		consultas.clear();
	}

	@Override
	public Integer size() {
		return consultas.size();
	}

	@Override
	public void remover(ConsultaAsincronica consulta) {
		consultas.remove(consulta);
	}

}
