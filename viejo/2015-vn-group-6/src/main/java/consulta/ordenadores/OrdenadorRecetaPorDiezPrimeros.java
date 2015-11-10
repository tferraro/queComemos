package consulta.ordenadores;

import java.util.List;
import java.util.stream.Collectors;

import businessModel.receta.Receta;

public class OrdenadorRecetaPorDiezPrimeros extends OrdenadorRecetaCompuesto {

	public OrdenadorRecetaPorDiezPrimeros(OrdenadorReceta anterior) {
		super(anterior);
	}

	@Override
	public List<Receta> ordenar(List<Receta> consulta) {
		return criterioAnterior.ordenar(consulta).stream().limit(10)
				.collect(Collectors.toList());
	}

	@Override
	public String getNombre() {
		return "Solo los 10 primeros";
	}
}
