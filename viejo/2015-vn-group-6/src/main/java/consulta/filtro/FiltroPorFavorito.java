package consulta.filtro;

import java.util.List;
import java.util.stream.Collectors;

import exceptions.model.receta.ErrorConsulta;
import externalEntities.recetas.RepositorioExterno;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

public class FiltroPorFavorito extends FiltroDecorador {

	public FiltroPorFavorito(Filtro anterior) {
		super(anterior);
		if (anterior == null)
			throw new ErrorConsulta("El filtro elegido no existe.");
		this.parametro = "Filtro Por Favoritos";
	}

	public List<Receta> consultar(Usuario user) {
		return filtroAnterior.consultar(user).stream()
				.filter(rec -> user.esRecetaHistorica(rec))
				.collect(Collectors.toList());
	}

	@Override
	public void agregarFuenteExterna(RepositorioExterno repo, Receta receta) {
		filtroAnterior.agregarFuenteExterna(repo, receta);
	}
}
