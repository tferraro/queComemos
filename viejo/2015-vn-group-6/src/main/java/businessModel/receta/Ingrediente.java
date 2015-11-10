package businessModel.receta;

import javax.persistence.Entity;
import javax.persistence.Table;

import persistencia.hibernate.PersistentEntity;

@Entity
@Table(name = "Ingredientes")
public class Ingrediente extends PersistentEntity {

	private Integer cantidad = 1;
	private String nombre = "";
	private Integer caloriasIndividuales = 0;

	public Integer cuantasCalorias() {
		return caloriasIndividuales * cantidad;
	}

	public Ingrediente getClone() {
		Ingrediente ingNuevo = new Ingrediente();
		ingNuevo.setNombre(this.getNombre());
		ingNuevo.setCantidad(this.getCantidad());
		ingNuevo.setCaloriasIndividuales(this.getCaloriasIndividuales());
		return ingNuevo;
	}

	// Getters & Setters
	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCaloriasIndividuales() {
		return caloriasIndividuales;
	}

	public void setCaloriasIndividuales(Integer caloriasIndividuales) {
		this.caloriasIndividuales = caloriasIndividuales;
	}

	public Boolean soy(String comida) {
		return this.getNombre().equals(comida);
	}
}
