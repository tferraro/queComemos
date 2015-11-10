package webGUI.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import consulta.monitorSincronico.MonitorDiezRecetasMasConsultadas;
import consulta.monitorSincronico.RecetaConsultada;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;
import persistencia.GestorCuentas;
import persistencia.Recetario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import webGUI.runner.WebRoot;

public class UsuariosController implements LoginChecker,
		WithGlobalEntityManager, TransactionalOps {

	private GestorCuentas repo;
	private Recetario recetario;

	public UsuariosController(GestorCuentas repo, Recetario recetario) {
		this.repo = repo;
		this.recetario = recetario;
	}

	public ModelAndView perfilUsuario(Request request, Response response) {
		Usuario user;
		if ((user = userIsLoggedAndItsPage(request, response, repo)) != null)
			return mostrarPerfil(user);
		return null;
	}

	public ModelAndView home(Request request, Response response) {
		Usuario user;
		if ((user = userIsLoggedAndItsPage(request, response, repo)) != null)
			return verHome(user);
		return null;
	}

	private ModelAndView verHome(Usuario user) {
		HashMap<String, Object> viewModel = new HashMap<>();
		viewModel.put("linkUsuario",
				WebRoot.INSTANCIA().perfilUsuario(user.getNombre()));
		viewModel.put("linkConsultas", WebRoot.INSTANCIA().consulta());
		user.getHistorialRecetas().forEach(r -> r.actualizarCalorias());
		// Tuve que inventar un monitor para hacer esto, espero q
		viewModel.put("recetas", obtenerRecetasHome(user));
		return new ModelAndView(viewModel, "home.hbs");
	}

	private List<Receta> obtenerRecetasHome(Usuario user) {

		if (user.getHistorialRecetas().isEmpty())
			if (MonitorDiezRecetasMasConsultadas.INSTANCIA()
					.recetasMasConsultada(user).isEmpty())
				return parsearRecetaConsulta(MonitorDiezRecetasMasConsultadas
						.INSTANCIA().recetasMasConsultada());
			else
				return parsearRecetaConsulta(MonitorDiezRecetasMasConsultadas
						.INSTANCIA().recetasMasConsultada(user));
		else
			return user.getHistorialRecetas();
	}

	private List<Receta> parsearRecetaConsulta(List<RecetaConsultada> consulta) {
		return consulta
				.stream()
				.map(r -> recetario.obtenerSinDiscriminar(new Receta()
						.setNombre(r.getReceta())))
				.collect(Collectors.toList());
	}

	public ModelAndView mostrarPerfil(Usuario user) {
		HashMap<String, Object> viewModel = new HashMap<>();
		viewModel.put("nombre", user.getNombre());
		viewModel.put("fechaNacimiento", user.getFechaNacimiento());
		viewModel.put("genero", user.getSexo());
		viewModel.put("altura", user.getAltura());
		viewModel.put("peso", user.getPeso());
		viewModel.put("imc", user.indiceDeMasaCorporal());
		viewModel.put("preferencias", user.getComidasQueGustan());
		viewModel.put("disgustan", user.getComidaQueNoGustan());
		viewModel.put("condicionesPrex", user.getCondicionesMedicas().stream()
				.map(c -> c.name()).collect(Collectors.toList()));
		viewModel.put("rutinaNombre", user.getRutina().name());
		viewModel.put("rutinalvl", user.getRutina().lvlName());
		viewModel.put("favoritas", user.getHistorialRecetas());

		viewModel.put("linkConsultas", WebRoot.INSTANCIA().consulta());
		return new ModelAndView(viewModel, "perfil_usuario.hbs");
	}

}
