package creacionales;

import java.util.ArrayList;
import java.util.List;

import businessModel.receta.Condimento;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;
import businessModel.receta.RecetaCompuesta;
import businessModel.temporada.Temporada;
import persistencia.Recetario;

public class BuilderReceta {

	private String nombre = null;
	private String descripcion = null;
	private List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
	private List<Condimento> condimentos = new ArrayList<Condimento>();
	private Temporada temporada = null;
	private DificultadDePreparacionReceta dificultad = DificultadDePreparacionReceta.NA;
	private Recetario repositorio = null;
	private Receta subreceta = null;

	public BuilderReceta agregarCamposObligatorios(String unNombre, String unaDescripcion, Temporada temporada,
			DificultadDePreparacionReceta dificultad, List<Ingrediente> unosIngredientes,
			List<Condimento> unosCondimentos) {
		this.setNombre(unNombre);
		this.setDescripcion(unaDescripcion);
		this.setTemporada(temporada);
		this.agregarDificultad(dificultad);
		this.setIngredientes(unosIngredientes);
		this.setCondimentos(unosCondimentos);
		return this;
	}

	public BuilderReceta agregarNombre(String unNombre) {
		this.nombre = unNombre;
		return this;
	}

	public BuilderReceta agregarDescripcion(String unaDescripcion) {
		this.descripcion = unaDescripcion;
		return this;
	}

	public BuilderReceta agregarTemporada(Temporada temporada) {
		this.temporada = temporada;
		return this;
	}

	public BuilderReceta agregarDificultad(DificultadDePreparacionReceta dificuldad) {
		this.dificultad = dificuldad;
		return this;
	}

	public BuilderReceta agregarIngrediente(String nombre, Integer cantidad, Integer calorias) {
		Ingrediente ingrediente = new Ingrediente();
		ingrediente.setNombre(nombre);
		ingrediente.setCantidad(cantidad);
		ingrediente.setCaloriasIndividuales(calorias);
		this.ingredientes.add(ingrediente);
		return this;
	}

	public BuilderReceta agregarCondimento(String nombre, Integer cantidad) {
		Condimento condimento = new Condimento();
		condimento.setNombre(nombre);
		condimento.setCantidad(cantidad);
		this.condimentos.add(condimento);
		return this;
	}

	public Receta compilar() {
		Receta receta;
		if (subreceta == null)
			receta = new Receta();
		else
			receta = new RecetaCompuesta().setSubReceta(subreceta);
		receta.setNombre(nombre);
		receta.setDescripcion(this.getDescripcion());
		receta.setTemporada(this.getTemporada());
		receta.setDificultad(this.getDificultad());
		this.getIngredientes().forEach(ingrediente -> receta.agregarIngrediente(ingrediente));
		this.getCondimento().forEach(condimento -> receta.agregarCondimento(condimento));
		receta.esValida();
		if (repositorio != null)
			repositorio.guardar(receta);
		return receta;
	}

	public void reset() {
		nombre = null;
		descripcion = null;
		temporada = null;
		dificultad = DificultadDePreparacionReceta.NA;
		ingredientes.clear();
		condimentos.clear();
	}

	public String getNombre() {
		return nombre;
	}

	public BuilderReceta setNombre(String nombre) {
		this.nombre = nombre;
		return this;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public BuilderReceta setDescripcion(String descripcion) {
		this.descripcion = descripcion;
		return this;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public BuilderReceta setTemporada(Temporada temporada) {
		this.temporada = temporada;
		return this;
	}

	public DificultadDePreparacionReceta getDificultad() {
		return dificultad;
	}

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public BuilderReceta setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
		return this;
	}

	public List<Condimento> getCondimento() {
		return condimentos;
	}

	public BuilderReceta setCondimentos(List<Condimento> condimentos) {
		this.condimentos = condimentos;
		return this;
	}

	public BuilderReceta setRepositorio(Recetario repoRecetas) {
		this.repositorio = repoRecetas;
		return this;
	}

	public BuilderReceta setSubReceta(Receta sub) {
		this.subreceta = sub;
		return this;
	}
}