package consulta.filtro;

import java.util.ArrayList;
import java.util.List;

import externalEntities.recetas.RepositorioExterno;
import businessModel.receta.Receta;

public class FuenteExterna {

	private RepositorioExterno fuente;
	private Receta parametro;
	private List<Receta> recetasExterna = new ArrayList<Receta>();

	public FuenteExterna(RepositorioExterno repo, Receta receta) {
		this.fuente = repo;
		this.parametro = receta;
		recetasExterna.addAll(repo.getRecetas(receta));
	}

	public RepositorioExterno getRepo() {
		return fuente;
	}

	public Receta getReceta() {
		return parametro;
	}

	public List<Receta> getRecetasExterna() {
		return recetasExterna;
	}

	public void setRecetasExterna(List<Receta> recetasExterna) {
		this.recetasExterna = recetasExterna;
	}

}
