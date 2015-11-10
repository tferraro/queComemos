package persistencia.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;
import persistencia.Recetario;

public enum RecetarioMock implements Recetario {
	INSTANCIA;

	private List<Receta> recetasPublicas = new ArrayList<Receta>();

	@Override
	public List<Receta> obtener() {
		return recetasPublicas.stream().map(p -> p.getClone())
				.collect(Collectors.toList());
	}

	@Override
	public void guardarPublica(Receta receta) {
		receta.volverPublica();
		guardar(receta);
	}

	@Override
	public void guardar(Receta receta) {
		receta.esValida();
		if(recetasPublicas.stream().anyMatch(r -> r.mismoNombre(receta)))
			recetasPublicas.remove(recetasPublicas
					.stream()
					.filter(r -> r.mismoNombre(receta))
					.collect(Collectors.toList())
					.get(0));
		recetasPublicas.add(receta);
	}

	@Override
	public void remover(Receta receta) {
		this.recetasPublicas.remove(receta);
	}

	public void removerTodasLasRecetas() {
		this.recetasPublicas.clear();
	}

	@Override
	public Receta obtenerSinDiscriminar(Receta unaReceta) {
		List<Receta> lista = recetasPublicas.stream()
				.filter(r -> r.mismoNombre(unaReceta)).map(p -> p.getClone())
				.collect(Collectors.toList());
		if (lista.isEmpty())
			return null;
		return lista.get(0);
	}

	@Override
	public List<Ingrediente> obtenerIngredientes() {
		return recetasPublicas.stream()
				.flatMap(r -> r.getIngredientesTotales().stream())
				.collect(Collectors.toList());
	}
}
