package consulta.ordenadores;

import java.util.Collections;
import java.util.List;

import businessModel.receta.Receta;

public class OrdenadorRecetaPorAlfabetico extends OrdenadorRecetaCompuesto {

	public OrdenadorRecetaPorAlfabetico(OrdenadorReceta anterior) {
		super(anterior);
	}

	@Override
	public List<Receta> ordenar(List<Receta> consulta) {
		List<Receta> aux = criterioAnterior.ordenar(consulta);
		Collections.sort(aux, (receta1, receta2) -> receta1.getNombre()
				.compareTo(receta2.getNombre()));
		return aux;
	}

	@Override
	public String getNombre() {
		return "Por orden alfabético";
	}
}
