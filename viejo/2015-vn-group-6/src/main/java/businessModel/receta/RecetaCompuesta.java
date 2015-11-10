package businessModel.receta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("C")
public class RecetaCompuesta extends Receta {
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Receta subReceta;

	public RecetaCompuesta setSubReceta(Receta sub) {
		subReceta = sub;
		return this;
	}

	public BigDecimal calcularCalorias() {
		return super.calcularCalorias().add(subReceta.calcularCalorias());
	}

	public Receta getClone() {
		Receta recetaClonada = super.getClone();
		RecetaCompuesta recetaFinal = (RecetaCompuesta) new RecetaCompuesta().setSubReceta(subReceta)
				.setNombre(recetaClonada.getNombre());
		// Asignamos los atributos a la RecetaCompuesta con la misma subreceta
		recetaFinal.setDescripcion(recetaClonada.getDescripcion());
		recetaFinal.setIngredientes(recetaClonada.getIngredientes());
		recetaFinal.setCondimentos(recetaClonada.getCondimentos());
		recetaFinal.setTemporada(recetaClonada.getTemporada());
		recetaFinal.setDificultad(recetaClonada.getDificultad());
		return recetaFinal;
	}

	public List<Ingrediente> getIngredientesTotales() {
		List<Ingrediente> listaAux = new ArrayList<Ingrediente>();
		listaAux.addAll(this.getIngredientes());
		listaAux.addAll(subReceta.getIngredientes());
		return listaAux;
	}

	public List<Condimento> getCondimentosTotales() {
		List<Condimento> listaAux = new ArrayList<Condimento>();
		listaAux.addAll(this.getCondimentos());
		listaAux.addAll(subReceta.getCondimentos());
		return listaAux;
	}

	public Receta getSubReceta() {
		return subReceta;
	}
}
