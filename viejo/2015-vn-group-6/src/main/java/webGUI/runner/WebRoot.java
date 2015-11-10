package webGUI.runner;

public class WebRoot {

	private static WebRoot instancia = new WebRoot("/");

	public static WebRoot INSTANCIA() {
		return instancia;
	}

	public String root = "/";
	private String pathUsuarios = "usuarios";
	private String pathRecetas = "recetas";
	private String pathLogin = "login";
	private String pathConsultas = "consulta";
	private String pathHome = "home";
	private String pathPerfil = "perfil";

	private WebRoot(String root) {
		this.root = root;
	}

	public String perfilUsuario(String nombre) {
		return root + pathUsuarios + "/" + nombre + "/" + pathPerfil;
	}

	public String receta(String receta) {
		return root + pathRecetas + "/" + receta;
	}

	public String login() {
		return root + pathLogin;
	}

	public String consulta() {
		return root + pathConsultas;
	}

	public String home(String usuario) {
		return root + pathUsuarios + "/" + usuario + "/" + pathHome;
	}

	public String nuevo_ingrediente(String receta) {
		return root + pathRecetas + "/" + receta + "/nuevo_ingrediente";
	}

	public String cambia_dificultad(String receta) {
		return root + pathRecetas + "/" + receta + "/nueva_dificultad";
	}

	public String cambia_temporada(String receta) {
		return root + pathRecetas + "/" + receta + "/nueva_temporada";
	}

	public String nuevo_condimento(String receta) {
		return root + pathRecetas + "/" + receta + "/nuevo_condimento";
	}
}
