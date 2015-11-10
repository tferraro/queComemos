package consulta.filtro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import externalEntities.recetas.RepositorioExterno;
import persistencia.Recetario;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

public class FiltroConsulta implements Filtro {

	private Recetario repositorio;
	private List<FuenteExterna> externos = new ArrayList<>();

	public FiltroConsulta(Recetario repo) {
		repositorio = repo;
	}

	@Override
	public List<Receta> consultar(Usuario user) {
		List<Receta> lista = new ArrayList<>();
		lista.addAll(repositorio.obtener());
		externos.forEach(ext -> lista.addAll(ext.getRecetasExterna()));
		lista.addAll(user.verRecetas(user));
		user.getGrupos()
				.forEach(
						grupo -> lista.addAll(grupo.verRecetasUsuarios(user).stream()
								.filter(recUsr -> (lista.stream()
										.noneMatch(recConsulta -> recConsulta.mismoNombre(recUsr))))
				.collect(Collectors.toList())));
		return lista;
	}

	@Override
	public void agregarFuenteExterna(RepositorioExterno repo, Receta receta) {
		this.externos.add(new FuenteExterna(repo, receta));

	}

	@Override
	public String getParametros() {
		return "";
	}

	@Override
	public String getNombre() {
		return "Sin Filtro";
	}
}
