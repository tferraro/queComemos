package webGUI.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import consulta.Consulta;
import consulta.filtro.Filtro;
import consulta.filtro.FiltroInterfaz;
import consulta.monitorSincronico.MonitorDiezRecetasMasConsultadas;
import consulta.ordenadores.OrdenadorReceta;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Receta;
import businessModel.temporada.Temporada;
import businessModel.usuario.Usuario;
import persistencia.GestorCuentas;
import persistencia.Recetario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import webGUI.runner.WebRoot;

public class ConsultaController implements LoginChecker,
		WithGlobalEntityManager, TransactionalOps {

	private GestorCuentas cuentas;
	private Recetario recetario;

	public ConsultaController(GestorCuentas cuentas, Recetario recetario) {
		this.cuentas = cuentas;
		this.recetario = recetario;
	}

	public ModelAndView ver(Request request, Response response) {
		Usuario user = userIsLogged(request, response, cuentas);
		if (user != null)
			return mostrarSinListarNada(user);
		return null;
	}

	private ModelAndView mostrarSinListarNada(Usuario user) {
		Map<String, Object> viewModel = new HashMap<String, Object>();

		rellenarViewModelConBase(viewModel, user);
		return new ModelAndView(viewModel, "consulta.hbs");
	}


	public ModelAndView consultar(Request request, Response response) {
		Usuario user = userIsLogged(request, response, cuentas);
		if (user != null)
			return generarConsulta(request, user);
		return null;
	}

	private ModelAndView generarConsulta(Request request, Usuario user) {
		Map<String, Object> viewModel = new HashMap<String, Object>();
		String nombre_filtro = request.queryParams("filtro_nombre");	
		String nombre_ordenador = request.queryParams("ordenador_nombre");
		
		String receta_nombre = request.queryParams("receta_nombre");
		String receta_dificultad = request.queryParams("receta_dificultad");
		String receta_temporada = request.queryParams("receta_temporada");	
		Integer receta_caloria_min;
		Integer receta_caloria_max;
		try {
			receta_caloria_min = Integer.parseInt(request.queryParams("receta_caloria_min"));
		} catch (NumberFormatException e) {
			receta_caloria_min = 0;
		}	
		try {
			receta_caloria_max = Integer.parseInt(request.queryParams("receta_caloria_max"));
		} catch (NumberFormatException e) {
			receta_caloria_max = 0;
		}			
		Filtro filtro = new FiltroInterfaz(
				Filtro.obtenerPor(nombre_filtro,recetario), 
				receta_nombre, 
				DificultadDePreparacionReceta.valueOf(receta_dificultad), 
				Temporada.nombre(receta_temporada), 
				receta_caloria_min, receta_caloria_max);	
		
		
		Consulta consulta = new Consulta(filtro, OrdenadorReceta.obtenerPor(nombre_ordenador));
		consulta.agregarMonitor(MonitorDiezRecetasMasConsultadas.INSTANCIA());
		withTransaction(() -> {
			List<Receta> recetas = consulta.consultar(user);
			viewModel.put("consultadas", recetas);
		});

		rellenarViewModelConBase(viewModel, user);
		return new ModelAndView(viewModel, "consulta.hbs");
	}
	

	private void rellenarViewModelConBase(Map<String, Object> viewModel,
			Usuario user) {
		viewModel.put("filtros", Filtro.obtenerNombres(recetario));
		viewModel.put("ordenadores", OrdenadorReceta.obtenerNombres());
		viewModel.put(
				"temporadas",
				Temporada.nombres().stream().map(t -> t.getNombre())
						.collect(Collectors.toList()));

		viewModel.put("linkUsuario",
				WebRoot.INSTANCIA().perfilUsuario(user.getNombre()));
		viewModel.put("linkConsultas", WebRoot.INSTANCIA().consulta());

	}

}
