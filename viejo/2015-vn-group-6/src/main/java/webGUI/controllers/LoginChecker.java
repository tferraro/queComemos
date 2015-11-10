package webGUI.controllers;

import persistencia.GestorCuentas;
import spark.Request;
import spark.Response;
import webGUI.runner.WebRoot;
import businessModel.usuario.Usuario;

public interface LoginChecker {

	public default Usuario userIsLogged(Request request, Response response, GestorCuentas gestor) {
		String nombreSesion = request.session().attribute("nombre");
		String contraSesion = request.session().attribute("contrasenia");
		Usuario user = gestor.obtenerUsuarioSiEsValido(nombreSesion,
				contraSesion);
		if (user != null) 
			return user;
		response.redirect(WebRoot.INSTANCIA().root);
		request.session().invalidate();		
		return null;
	}
	
	public default Usuario userIsLoggedAndItsPage(Request request, Response response, GestorCuentas gestor) {
		String nombrePath = request.params(":nombre");
		String nombreSesion = request.session().attribute("nombre");
		if (nombrePath.equals(nombreSesion)) 
			return userIsLogged(request, response, gestor);
		response.redirect(WebRoot.INSTANCIA().root);
		request.session().invalidate();		
		return null;
	}
}
