package consulta.filtro;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import persistencia.Recetario;
import externalEntities.recetas.RepositorioExterno;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

public interface Filtro {

	List<Receta> consultar(Usuario user);

	void agregarFuenteExterna(RepositorioExterno repo, Receta receta);

	String getParametros();

	String getNombre();

	default Boolean soy(String nombre) {
		return nombre.equals(getNombre());
	}
	
	static List<String> obtenerNombres(Recetario recetario) {
		return obtener(recetario)
				.stream()
				.map(x -> x.getNombre())
				.collect(Collectors.toList());
	}
	
	static List<Filtro> obtener(Recetario recetario) {
		return Arrays.asList(new FiltroConsulta(recetario),
				new FiltroPorCalorias(new FiltroConsulta(recetario)),
				new FiltroPorFavorito(new FiltroConsulta(recetario)),
				new FiltroPorIngredientesBaratos(new FiltroConsulta(recetario)),
				new FiltroPorCondicionesPrexistentes(new FiltroConsulta(recetario)));		
	}
	
	static Filtro obtenerPor(String nombre, Recetario recetario) {
		List<Filtro> lista = obtener(recetario)
				.stream()
				.filter(f -> f.soy(nombre))
				.collect(Collectors.toList());
		return lista.isEmpty()? null : lista.get(0);
	}
}
