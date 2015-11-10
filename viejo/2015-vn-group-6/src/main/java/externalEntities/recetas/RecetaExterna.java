package externalEntities.recetas;

import java.util.ArrayList;
import java.util.List;

import businessModel.receta.Condimento;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;

public class RecetaExterna {

	private String nombre = "";
	private List<String> ingredientes = new ArrayList<String>();
	private Integer tiempoPreparacion;
	private Integer totalCalorias;
	private String dificultadReceta;

	// private String autor;
	// private Integer anioReceta;

	public String getNombre() {
		return nombre;
	}

	public DificultadDePreparacionReceta getDificultadReceta() {
		return parsearDificultad(dificultadReceta);
	}

	public Receta pasearARecetaInterna() {
		Receta recetaParseada = new Receta().setNombre(this.nombre);
		recetaParseada.setDificultad(this.getDificultadReceta());
		recetaParseada.setDescripcion("Tiempo de preparacion: ".concat(this.tiempoPreparacion.toString()));
		agregarIngredientesYCondimentos(recetaParseada);
		return recetaParseada;
	}

	private void agregarIngredientesYCondimentos(Receta recetaParseada) {
		recetaParseada.setIngredientes(this.getIngredientes());
		recetaParseada.setCondimentos(this.getCondimentos());
		// Agregamos las calorias para el calculo
		if (recetaParseada.getIngredientes().size() > 0)
			recetaParseada.getIngredientes().get(0).setCaloriasIndividuales(this.totalCalorias);
	}

	private DificultadDePreparacionReceta parsearDificultad(String dificultad) {
		switch (dificultad) {
		case "DIFICIL":
			return DificultadDePreparacionReceta.DIFICIL;
		case "FACIL":
			return DificultadDePreparacionReceta.FACIL;
		case "MEDIANA":
			return DificultadDePreparacionReceta.MEDIA;
		default:
			return null;
		}
	}

	private Condimento parsearCondimento(String ing) {
		Condimento condimentoModelo = new Condimento();
		condimentoModelo.setNombre(ing);
		condimentoModelo.setCantidad(condimentoModelo.soy("azucar") ? 101 : 1);
		return condimentoModelo;
	}

	private Ingrediente parsearIngrediente(String ing) {
		Ingrediente ingredienteModelo = new Ingrediente();
		ingredienteModelo.setNombre(ing);
		ingredienteModelo.setCantidad(1);
		return ingredienteModelo;
	}

	private List<Ingrediente> getIngredientes() {
		List<Ingrediente> ingredientesPosta = new ArrayList<Ingrediente>();
		ingredientes.stream().filter(ing -> !Condimento.stringEsPosibleCondimento(ing))
				.forEach(ing -> ingredientesPosta.add(parsearIngrediente(ing)));
		return ingredientesPosta;
	}

	private List<Condimento> getCondimentos() {
		List<Condimento> condimentos = new ArrayList<Condimento>();
		ingredientes.stream().filter(ing -> Condimento.stringEsPosibleCondimento(ing))
				.forEach(ing -> condimentos.add(parsearCondimento(ing)));
		return condimentos;
	}
}
