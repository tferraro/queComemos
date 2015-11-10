package externalEntities.recetas;

import java.util.List;

import businessModel.receta.Receta;

public interface RepositorioExterno {
	public List<Receta> getRecetas(Receta receta);
}
