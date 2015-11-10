package consulta.ordenadores;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import businessModel.receta.Receta;

public interface OrdenadorReceta {

	List<Receta> ordenar(List<Receta> consulta);
	
	String getNombre();
	
	default Boolean soy(String nombre) {
		return nombre.equals(getNombre());
	}
	
	static List<String> obtenerNombres() {
		return obtener()
				.stream()
				.map(x -> x.getNombre())
				.collect(Collectors.toList());
	}
	
	static List<OrdenadorReceta> obtener() {
		return Arrays.asList(new OrdenadorRecetaBase(), 
				new OrdenadorRecetaPorAlfabetico(new OrdenadorRecetaBase()),
				new OrdenadorRecetaPorCalorias(new OrdenadorRecetaBase()), 
				new OrdenadorRecetaPorDiezPrimeros(new OrdenadorRecetaBase()), 
				new OrdenadorRecetaPorPares(new OrdenadorRecetaBase()));
	}
	
	static OrdenadorReceta obtenerPor(String nombre) {
		List<OrdenadorReceta> lista = obtener()
				.stream()
				.filter(f -> f.soy(nombre))
				.collect(Collectors.toList());
		return lista.isEmpty()? null : lista.get(0);
	}
}
