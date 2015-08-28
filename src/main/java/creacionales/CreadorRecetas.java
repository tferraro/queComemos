package creacionales;

import java.util.ArrayList;
import java.util.List;

import model.Ingrediente;
import model.Receta;

public class CreadorRecetas {

	private String nombre;
	private List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();

	public CreadorRecetas nombre(String nombre) {
		this.nombre = nombre;
		return this;
	}

	public CreadorRecetas ingrediente(String nombre, Integer cant) {
		this.ingredientes.add(new Ingrediente().setNombre(nombre).setCantidad(cant));
		return this;
	}

	public String getNombre() {
		return nombre;
	}

	public Receta crear() {
		Receta receta = new Receta();
		receta.setNombre(nombre);
		ingredientes.forEach(ing -> receta.agregarIngrediente(ing));
		return receta;
	}


}
