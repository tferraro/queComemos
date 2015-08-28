package model;

public class Ingrediente {

	private String nombre;
	private Integer cantidad;

	public Ingrediente setNombre(String nombre) {
		this.nombre = nombre;
		return this;
	}

	public String getNombre() {
		return nombre;
	}

	public Ingrediente setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
		return this;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public Boolean mismoNombre(String nombrePrueba) {
		return this.getNombre().equals(nombre);
	}

}
