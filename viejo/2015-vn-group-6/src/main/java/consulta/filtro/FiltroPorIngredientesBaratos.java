package consulta.filtro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.model.receta.ErrorConsulta;
import externalEntities.recetas.RepositorioExterno;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

public class FiltroPorIngredientesBaratos extends FiltroDecorador {

	private List<String> ingredientesCaros = new ArrayList<>();

	public FiltroPorIngredientesBaratos(Filtro anterior) {
		super(anterior);
		if (anterior == null)
			throw new ErrorConsulta("El filtro elegido no existe.");
		ingredientesCaros.add("Lechon");
		ingredientesCaros.add("Lomo");
		ingredientesCaros.add("Salmon");
		ingredientesCaros.add("Alcaparras");
		this.parametro = "Filtro Por Ingredientes Caros";
	}

	public List<Receta> consultar(Usuario user) {
		return filtroAnterior
				.consultar(user)
				.stream()
				.filter(receta -> ingredientesCaros.stream().noneMatch(
						ing -> receta.contieneIngrediente(ing)))
				.collect(Collectors.toList());
	}

	@Override
	public void agregarFuenteExterna(RepositorioExterno repo, Receta receta) {
		filtroAnterior.agregarFuenteExterna(repo, receta);
	}
}
