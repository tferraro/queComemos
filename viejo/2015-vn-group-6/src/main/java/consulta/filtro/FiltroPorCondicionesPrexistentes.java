package consulta.filtro;

import java.util.ArrayList;
import java.util.List;

import exceptions.model.receta.ErrorConsulta;
import exceptions.model.receta.ErrorRecetaNoSaludable;
import externalEntities.recetas.RepositorioExterno;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

public class FiltroPorCondicionesPrexistentes extends FiltroDecorador {

	public FiltroPorCondicionesPrexistentes(Filtro anterior) {
		super(anterior);
		if (anterior == null)
			throw new ErrorConsulta("El filtro elegido no existe.");
		this.parametro = "Filtro Por Condiciones Prexistentes";
	}

	@Override
	public List<Receta> consultar(Usuario user) {
		List<Receta> aux = new ArrayList<>();
		filtroAnterior.consultar(user).forEach(rec -> {
			try {
				user.meEsAdecuada(rec);
				aux.add(rec);
			} catch (ErrorRecetaNoSaludable e) {

			}
		});
		return aux;
	}

	@Override
	public void agregarFuenteExterna(RepositorioExterno repo, Receta receta) {
		filtroAnterior.agregarFuenteExterna(repo, receta);
	}
}
