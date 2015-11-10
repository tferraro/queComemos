package webGUI.controllers;

import java.util.Arrays;
import java.util.HashMap;

import persistencia.GestorCuentas;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import webGUI.runner.WebRoot;

public class LoginController {

	private GestorCuentas repo;

	public LoginController(GestorCuentas repo) {
		this.repo = repo;
	}

	public ModelAndView getLogin(Request request, Response response) {
		if (request.session(false) == null)
			return mostrarLoginPage("");
		response.redirect(WebRoot.INSTANCIA().home(request.session().attribute("nombre")));
		return null;
	}


	public ModelAndView mostrarLoginPage(String mensaje) {
		HashMap<String, Object> viewModel = new HashMap<>();
		viewModel.put("mensaje", mensaje);
		return new ModelAndView(viewModel, "login.hbs");
	}

	public ModelAndView login(Request request, Response response) {
		String stringData = request.body();
		LoginData data = parseLoginData(stringData);
		if (request.session(false) != null && stringData.equals("")) {
			request.session().invalidate();
			return mostrarLoginPage("");
		}
		if (repo.obtenerUsuarioSiEsValido(data.userName, data.userPassword) != null) {
			Session sesion = request.session(true);
			sesion.attribute("nombre", data.userName);
			sesion.attribute("contrasenia", data.userPassword);
			response.redirect(WebRoot.INSTANCIA().home(data.userName));
			return null;
		}
		return mostrarLoginPage("Usuario o Contraseña Invalidas");
	}

	public LoginData parseLoginData(String stringData) {
		LoginData login = new LoginData();
		Arrays.asList(stringData.split("&")).forEach(s -> {
			String[] keyValue = s.split("=");
			if (keyValue[0].equals("userName") && keyValue.length == 2)
				login.userName = keyValue[1];
			if (keyValue[0].equals("userPassword") && keyValue.length == 2)
				login.userPassword = keyValue[1];
		});
		return login;
	}
	
	public ModelAndView loginRedirect(Request request, Response response) {
		response.redirect(WebRoot.INSTANCIA().login());
		return null;
	}
}
