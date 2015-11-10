package webGUI.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import businessModel.receta.Condimento;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;
import businessModel.temporada.Temporada;
import businessModel.usuario.Usuario;
import persistencia.GestorCuentas;
import persistencia.Recetario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import webGUI.runner.WebRoot;

public class RecetasController implements LoginChecker,
		WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

	private Recetario repo;
	private GestorCuentas cuentas;

	public RecetasController(Recetario repo, GestorCuentas cuentas) {
		this.repo = repo;
		this.cuentas = cuentas;
	}

	public ModelAndView elegirReceta(Request request, Response response) {
		String nombrePath = request.params(":nombre");
		Usuario user;
		if ((user = userIsLogged(request, response, cuentas)) != null)
			return mostrarReceta(user, nombrePath);
		return null;
	}

	public ModelAndView mostrarReceta(Usuario user, String nombreReceta) {
		HashMap<String, Object> viewModel = new HashMap<>();

		Receta receta = repo.obtenerSinDiscriminar(new Receta()
				.setNombre(nombreReceta.replace("_", " ")));
		List<String> todosIngredientes = repo.obtenerIngredientes().stream()
				.map(r -> r.getNombre()).collect(Collectors.toList());

		receta.actualizarCalorias();

		viewModel.put("nombre", receta.getNombre());
		viewModel.put("descripcion", receta.getDescripcion());
		viewModel.put("calorias", receta.calcularCalorias().intValue());
		viewModel.put("dificultad", receta.getDificultad());
		viewModel.put("tempo", receta.getTemporada().getNombre());
		viewModel.put(
				"temporadas",
				Temporada.nombres().stream().map(t -> t.getNombre())
						.collect(Collectors.toList()));
		viewModel.put("ingredientes", receta.getIngredientes());
		viewModel.put("condimentos", receta.getCondimentos());

		viewModel.put("todos_los_ingredientes", todosIngredientes);
		viewModel.put("linkVolver", WebRoot.INSTANCIA().consulta());
		viewModel.put("linkConsultas", WebRoot.INSTANCIA().consulta());
		viewModel.put("linkUsuario",
				WebRoot.INSTANCIA().perfilUsuario(user.getNombre()));
		if (user.getHistorialRecetas().stream()
				.anyMatch(r -> r.mismoNombre(receta))) {
			viewModel.put("unfavButton", "glyphicon glyphicon-star");
			viewModel.put("unfavName", "unfav");
		} else {
			viewModel.put("favButton", "glyphicon glyphicon-star-empty");
			viewModel.put("favName", "fav");
		}
		viewModel.put("linkRecetas", WebRoot.INSTANCIA().receta(nombreReceta));
		return new ModelAndView(viewModel, "receta.hbs");
	}

	public ModelAndView actualizarDificultad(Request request, Response response) {
		String nuevaDificultad = request.queryParams("receta_dificultad");
		Receta receta = obtenerRecetaConNombreParam(request.params(":nombre"));
		receta.setDificultad(DificultadDePreparacionReceta
				.valueOf(nuevaDificultad));
		guardarReceta(receta);
		return mostrarActualizarReceta();
	}

	public ModelAndView actualizarTemporada(Request request, Response response) {
		String nuevaTemporada = request.queryParams("receta_temporada");
		Receta receta = obtenerRecetaConNombreParam(request.params(":nombre"));
		receta.setTemporada(Temporada.nombre(nuevaTemporada));
		guardarReceta(receta);
		return mostrarActualizarReceta();
	}

	public ModelAndView agregarCondimento(Request request, Response response) {
		Receta receta = obtenerRecetaConNombreParam(request.params(":nombre"));
		Condimento nuevo = new Condimento();
		nuevo.setNombre(request.queryParams("nuevo_condimento_nombre"));
		nuevo.setCantidad(Integer.parseInt(request
				.queryParams("nuevo_condimento_cant")));
		receta.agregarCondimento(nuevo);
		guardarReceta(receta);
		return mostrarActualizarReceta();
	}

	public ModelAndView agregarIngrediente(Request request, Response response) {
		Receta receta = obtenerRecetaConNombreParam(request.params(":nombre"));
		Ingrediente nuevo = new Ingrediente();
		nuevo.setNombre(request.queryParams("ingrediente_nombre"));
		nuevo.setCantidad(Integer.parseInt(request
				.queryParams("ingrediente_cantidad")));
		nuevo.setCaloriasIndividuales(Integer.parseInt(request
				.queryParams("ingrediente_calorias")));
		receta.agregarIngrediente(nuevo);
		guardarReceta(receta);
		return mostrarActualizarReceta();
	}

	private Receta obtenerRecetaConNombreParam(String nombre) {
		return repo.obtenerSinDiscriminar(new Receta().setNombre(nombre
				.replace("_", " ")));
	}

	private ModelAndView mostrarActualizarReceta() {
		HashMap<String, Object> viewModel = new HashMap<>();
		viewModel.put("link", WebRoot.INSTANCIA().root);
		return new ModelAndView(viewModel, "actualizar_receta.hbs");
	}

	public void guardarReceta(Receta receta) {
		withTransaction(() -> {
			repo.guardar(receta);
			entityManager().flush();
		});
	}

	public void guardarUser(Usuario user) {
		withTransaction(() -> {
			cuentas.guardar(user);
			entityManager().flush();
			entityManager().refresh(user);
		});
	}

	public ModelAndView favoritear(Request request, Response response) {
		Receta receta = obtenerRecetaConNombreParam(request.params(":nombre"));
		Usuario user;
		if ((user = userIsLogged(request, response, cuentas)) != null) {
			if (!user.getHistorialRecetas().contains(receta))
				user.agregarFavorito(receta);
			guardarUser(user);
		}
		response.redirect(WebRoot.INSTANCIA().receta(receta.getNombre()));
		return null;
	}

	public ModelAndView desfavoritear(Request request, Response response) {
		Receta receta = obtenerRecetaConNombreParam(request.params(":nombre"));
		Usuario user;
		if ((user = userIsLogged(request, response, cuentas)) != null) {
			if (user.getHistorialRecetas().contains(receta))
				user.removerFavorito(receta);
			guardarUser(user);
			
		}
		response.redirect(WebRoot.INSTANCIA().receta(receta.getNombre()));
		return null;
	}
}
