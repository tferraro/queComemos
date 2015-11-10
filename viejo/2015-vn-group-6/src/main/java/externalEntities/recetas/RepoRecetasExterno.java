package externalEntities.recetas;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import queComemos.entrega3.dominio.Dificultad;
import queComemos.entrega3.repositorio.BusquedaRecetas;
import queComemos.entrega3.repositorio.RepoRecetas;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Receta;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public enum RepoRecetasExterno implements RepositorioExterno {
	INSTANCIA;

	private RepoRecetas repo = new RepoRecetas();

	public List<Receta> getRecetas(Receta receta) {
		return obtenerRecetasParseadasSegun(parsearRecetaABusqueda(receta));
	}

	private BusquedaRecetas parsearRecetaABusqueda(Receta receta) {
		BusquedaRecetas busqueda = new BusquedaRecetas();
		busqueda.setNombre(receta.getNombre());
		busqueda.setDificultad(parsearDificultad(receta.getDificultad()));
		receta.getIngredientesTotales().forEach(ing -> busqueda.agregarPalabraClave(ing.getNombre()));
		receta.getCondimentosTotales().forEach(cond -> busqueda.agregarPalabraClave(cond.getNombre()));
		return busqueda;
	}

	private Dificultad parsearDificultad(DificultadDePreparacionReceta dificultad) {
		switch (dificultad) {
		case DIFICIL:
			return Dificultad.DIFICIL;
		case FACIL:
			return Dificultad.FACIL;
		case MEDIA:
			return Dificultad.MEDIANA;
		default:
			return null;
		}
	}

	private List<Receta> obtenerRecetasParseadasSegun(BusquedaRecetas busqueda) {
		List<Receta> parseadas = new ArrayList<Receta>();
		obtenerRecetas(busqueda).forEach(r0 -> parseadas.add(r0.pasearARecetaInterna()));
		return parseadas;
	}

	private List<RecetaExterna> obtenerRecetas(BusquedaRecetas busqueda) {
		Type listType = new TypeToken<ArrayList<RecetaExterna>>() {
		}.getType();
		return (new Gson().fromJson(repo.getRecetas(busqueda), listType));

	}
}
