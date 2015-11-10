package businessModel.receta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import persistencia.hibernate.PersistentEntity;
import exceptions.model.receta.*;
import businessModel.temporada.Temporada;
import businessModel.temporada.TodoElAnio;

@Entity
@Table(name = "Recetas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@DiscriminatorValue("N")
public class Receta extends PersistentEntity {

	private String nombre = null;
	private String descripcion = null;
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "recetaID")
	private List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "recetaID")
	private List<Condimento> condimentos = new ArrayList<Condimento>();
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Temporada temporada = new TodoElAnio();
	@Enumerated
	private DificultadDePreparacionReceta dificultad = DificultadDePreparacionReceta.NA;
	@Transient
	private String calorias_string;
	private Boolean publica = false;
	
	public Receta setNombre(String nombre) {
		this.nombre = nombre;
		return this;
	}

	public BigDecimal calcularCalorias() {
		BigDecimal sumaIngredientes = new BigDecimal(
				ingredientes.stream().mapToInt(Ingrediente::cuantasCalorias).sum());
		return sumaIngredientes.pow(coefCocion() * coefTiempo());
	}

	public void actualizarCalorias() {
		this.calorias_string = this.calcularCalorias().toString();
	}
	
	public Receta getClone() {
		Receta nuevaReceta = new Receta().setNombre(this.getNombre());
		nuevaReceta.setDescripcion(descripcion);
		nuevaReceta.getIngredientes()
				.addAll(ingredientes.stream().map(Ingrediente::getClone).collect(Collectors.toList()));
		nuevaReceta.getCondimentos()
				.addAll(condimentos.stream().map(Condimento::getClone).collect(Collectors.toList()));
		nuevaReceta.setTemporada(temporada);
		nuevaReceta.setDificultad(dificultad);
		return nuevaReceta;
	}

	private int coefCocion() {
		return 1;
	}

	private int coefTiempo() {
		return 1;
	}

	public void esValida() {
		tengoIngredientes();
		tengoCaloriasEnRangoValido();
	}

	private void tengoCaloriasEnRangoValido() {
		if (this.calcularCalorias().compareTo(new BigDecimal(10)) == -1
				|| this.calcularCalorias().compareTo(new BigDecimal(5000)) == 1) {
			throw new ErrorRecetaNoValida("No es valida la receta las calorias no se encuentran entre 10 a 5000");
		}
	}

	private void tengoIngredientes() {
		if (this.getIngredientes().isEmpty()) {
			throw new ErrorRecetaNoValida("No es valida la receta por que no tiene ningun ingrediente");
		}
	}

	public Boolean contieneIngrediente(String comida) {
		return this.getIngredientesTotales().stream().anyMatch(ing -> ing.soy(comida));
	}

	public Boolean contieneCondimento(String nombre) {
		return getCondimentosTotales().stream().anyMatch(cond -> cond.soy(nombre));
	}

	public Boolean nombreMatcheaConInteres(String interes) {
		return this.nombre.contains(interes);
	}

	public void agregarModificacionPor(String nombreUsuario) {
		this.nombre = nombre + "modificado Por" + nombreUsuario;
	}

	// Getters & Setters
	public String getNombre() {
		return nombre;
	}

	public List<Condimento> getCondimentos() {
		return condimentos;
	}

	public List<Condimento> getCondimentosTotales() {
		return getCondimentos();
	}

	public void setCondimentos(List<Condimento> condimentos) {
		this.condimentos = condimentos;
	}

	public void agregarCondimento(Condimento condimento) {
		this.condimentos.add(condimento);
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public List<Ingrediente> getIngredientesTotales() {
		return getIngredientes();
	}

	public void setIngredientes(List<Ingrediente> ing) {
		this.ingredientes = ing;
	}

	public void agregarIngrediente(Ingrediente ingrediente) {
		this.ingredientes.add(ingrediente);
	}

	public void removerIngrediente(Ingrediente ingrediente) {
		this.ingredientes.remove(ingrediente);
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public DificultadDePreparacionReceta getDificultad() {
		return dificultad;
	}

	public void setDificultad(DificultadDePreparacionReceta dificultad) {
		this.dificultad = dificultad;
	}

	public Receta getSubReceta() {
		return null;
	}

	public Boolean mismoNombre(Receta receta) {
		return nombre.matches(receta.getNombre());
	}

	public Boolean nombreContiene(Receta receta) {
		return nombre.matches("(.*)" + receta.getNombre() + "(.*)");
	}

	public Boolean poseeCondimentoConAlMenosUnaCantidad(String string, Integer cant) {
		return this.getCondimentosTotales().stream().anyMatch(c -> c.soyConAlmenosCantidad(string, cant));
	}

	public Boolean sosDificil() {
		return dificultad.equals(DificultadDePreparacionReceta.DIFICIL);
	}

	public void volverPublica() {
		this.publica = true;
	}

	public Boolean esPublica() {
		return this.publica;
	}
}
