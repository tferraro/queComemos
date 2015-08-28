package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Receta {	
	private String nombre;
	private List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void agregarIngrediente(Ingrediente ing) {
		this.ingredientes.add(ing);
	}
	
	public void removerIngrediente(Ingrediente ing) {
		this.ingredientes.remove(ing);
	}
	public List<Ingrediente> getIngredientesCon(String nombre) {
		return ingredientes.stream().filter(ing -> ing.mismoNombre(nombre)).collect(Collectors.toList());
	}
}
