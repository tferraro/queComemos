package consulta.ordenadores;

import java.util.List;

import businessModel.receta.Receta;

public class OrdenadorRecetaBase implements OrdenadorReceta {

	@Override
	public List<Receta> ordenar(List<Receta> consulta) {
		return consulta;
	}

	@Override
	public String getNombre() {
		return "Sin Orden";
	}
}
