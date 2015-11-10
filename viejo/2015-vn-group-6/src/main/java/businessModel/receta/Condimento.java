package businessModel.receta;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import persistencia.hibernate.PersistentEntity;

@Entity
@Table(name = "Condimentos")
public class Condimento extends PersistentEntity {
	private Integer cantidad = 0;
	private String nombre = "";

	public Condimento getClone() {
		Condimento nuevoCond = new Condimento();
		nuevoCond.setCantidad(this.getCantidad());
		nuevoCond.setNombre(this.getNombre());
		return nuevoCond;
	}

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

	public Boolean soy(String condimento) {
		return this.nombre.matches("(?iu)(.*\\b)" + condimento + "(\\b.*)");
	}

	public Boolean soyConAlmenosCantidad(String string, Integer cant) {
		return this.soy(string) && (this.cantidad > 100);
	}

	public static Boolean stringEsPosibleCondimento(String cadena) {
		List<String> condimentos = new ArrayList<String>();
		condimentos.add("sal");
		condimentos.add("aceite");
		condimentos.add("tomillo");
		condimentos.add("azucar");
		condimentos.add("caldo");
		return condimentos.stream().anyMatch(cond -> cadena.matches("(?iu)(.*\\b)" + cond + "(\\b.*)"));
	}
}
