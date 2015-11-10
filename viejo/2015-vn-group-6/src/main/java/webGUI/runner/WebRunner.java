package webGUI.runner;

import static spark.Spark.*;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

import persistencia.Recetario;
import persistencia.GestorCuentas;
import persistencia.hibernate.GestorCuentasHibernate;
import persistencia.hibernate.RecetarioHibernate;
import spark.template.handlebars.HandlebarsTemplateEngine;
import webGUI.controllers.*;

public class WebRunner {

	private static HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
	private static WebRoot root = WebRoot.INSTANCIA();;
	private static GestorCuentas repoUsuarios = new GestorCuentasHibernate();
	private static Recetario repoRecetas = RecetarioHibernate.INSTANCIA();
	private static RecetasController recetas = new RecetasController(
			repoRecetas, repoUsuarios);
	private static UsuariosController usuarios = new UsuariosController(
			repoUsuarios, repoRecetas);
	private static LoginController login = new LoginController(repoUsuarios);
	private static ConsultaController consulta = new ConsultaController(
			repoUsuarios, repoRecetas);

	public static void main(String[] args) {
		staticFileLocation("/public");
		port(8080);
		new JuegoDeDatosMock().crearDatos(repoRecetas, repoUsuarios);

		after((request, response) -> { 
			   PerThreadEntityManagers.getEntityManager(); 
			   PerThreadEntityManagers.closeEntityManager();
			 });
		get(root.root, login::loginRedirect);
		get("/index.html", login::loginRedirect);
		get(root.login(), login::getLogin, engine);
		post(root.login(), login::login, engine);
		get(root.perfilUsuario(":nombre"), usuarios::perfilUsuario, engine);
		// TODO: Terminar home
		get(root.home(":nombre"), usuarios::home, engine);
		get(root.consulta(), consulta::ver, engine);
		post(root.consulta(), consulta::consultar, engine);
		get(root.receta(":nombre"), recetas::elegirReceta, engine);
		post(root.receta(":nombre") + "/fav", recetas::favoritear, engine);
		post(root.receta(":nombre") + "/unfav", recetas::desfavoritear, engine);
		post(root.nuevo_ingrediente(":nombre"), recetas::agregarIngrediente,
				engine);
		post(root.cambia_dificultad(":nombre"), recetas::actualizarDificultad,
				engine);
		post(root.cambia_temporada(":nombre"), recetas::actualizarTemporada,
				engine);
		post(root.nuevo_condimento(":nombre"), recetas::agregarCondimento,
				engine);
	}
}

/*
 * ESTRUCTURA DE DIRECTORIOS 
 * ->/
 * ->/index.html
 * ->/consulta
 * ->/login
 * ->/receta/:nombre
 * ->/receta/:nombre/nuevo_ingrediente (POST)
 * ->/receta/:nombre/nuevo_condimento (POST)
 * ->/receta/:nombre/cambia_dificultad(POST)
 * ->/receta/:nombre/cambia_temporada(POST)
 * ->usuarios/:nombreUser/home
 * ->usuarios/:nombreUser/perfil
 */