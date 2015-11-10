package consulta.filtro;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.model.receta.ErrorConsulta;
import externalEntities.recetas.RepositorioExterno;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

public class FiltroPorCalorias extends FiltroDecorador {

	public FiltroPorCalorias(Filtro anterior) {
		super(anterior);
		if (anterior == null)
			throw new ErrorConsulta("El filtro elegido no existe.");
		this.parametro = "Filtro Por Calorias";
	}

	@Override
	public List<Receta> consultar(Usuario user) {
		if (user.tieneSobrepeso())
			return filtroAnterior
					.consultar(user)
					.stream()
					.filter(r -> r.calcularCalorias().compareTo(
							new BigDecimal(500)) != 1)
					.collect(Collectors.toList());
		else
			return filtroAnterior.consultar(user);
	}

	@Override
	public void agregarFuenteExterna(RepositorioExterno repo, Receta receta) {
		filtroAnterior.agregarFuenteExterna(repo, receta);
	}
}
