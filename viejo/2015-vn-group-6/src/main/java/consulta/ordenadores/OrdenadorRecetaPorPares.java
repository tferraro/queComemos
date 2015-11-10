package consulta.ordenadores;

import java.util.ArrayList;
import java.util.List;

import businessModel.receta.Receta;

public class OrdenadorRecetaPorPares extends OrdenadorRecetaCompuesto {

	public OrdenadorRecetaPorPares(OrdenadorReceta anterior) {
		super(anterior);
	}

	@Override
	public List<Receta> ordenar(List<Receta> consulta) {
		List<Receta> sinOrdenar = criterioAnterior.ordenar(consulta);
		List<Receta> pares = new ArrayList<>();
		for (Integer i = 0; i < sinOrdenar.size(); i++)
			if ((i % 2) != 0) // Empiezan por 0
				pares.add(sinOrdenar.get(i));
		return pares;
	}

	@Override
	public String getNombre() {
		return "Solo los pares";
	}

}
