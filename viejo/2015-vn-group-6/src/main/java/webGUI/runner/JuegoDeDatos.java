package webGUI.runner;

import persistencia.GestorCuentas;
import persistencia.Recetario;

public interface JuegoDeDatos {

	void crearDatos(Recetario recetario, GestorCuentas gestor);
}
