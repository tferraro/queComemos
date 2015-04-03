package modeloNegocio;

import java.util.ArrayList;
import java.util.List;

public class Receta {

	public static enum Dificultad {LEVE, MEDIA};
	
	private String nombre;
	private List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
	private List<Condimento> condimentos = new ArrayList<Condimento>();
	private List<Condicion> condicionesProhibidas = new ArrayList<Condicion>();
	
	private Dificultad dificultad;
	public long calorias() {
		//TODO: NotImplementedYet :P
		return 0;
	}
}
