package persistencia;

import java.util.List;

import consulta.monitorAsincronico.ConsultaAsincronica;

public interface ConsultasAsincronicas {

	void guardar(ConsultaAsincronica consultaAsincronica);

	ConsultaAsincronica obtener(ConsultaAsincronica consulta);

	List<ConsultaAsincronica> obtenerTodas();

	void removerTodas();

	Integer size();

	void remover(ConsultaAsincronica consulta);

}