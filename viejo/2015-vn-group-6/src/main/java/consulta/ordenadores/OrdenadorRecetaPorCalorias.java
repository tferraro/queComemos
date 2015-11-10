package consulta.ordenadores;

import java.util.Collections;
import java.util.List;

import businessModel.receta.Receta;

public class OrdenadorRecetaPorCalorias extends OrdenadorRecetaCompuesto {

	public OrdenadorRecetaPorCalorias(OrdenadorReceta anterior) {
		super(anterior);
	}

	@Override
	public List<Receta> ordenar(List<Receta> consulta) {
		List<Receta> aux = criterioAnterior.ordenar(consulta);
		Collections.sort(aux, (receta1, receta2) -> receta1.calcularCalorias()
				.compareTo(receta2.calcularCalorias()));
		return aux;
	}

	@Override
	public String getNombre() {
		return "Por Calorias";
	}

}
