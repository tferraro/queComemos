package persistencia;

import java.util.List;

import businessModel.receta.Receta;
import businessModel.receta.Ingrediente;

public interface Recetario {

	List<Receta> obtener();
	
	public Receta obtenerSinDiscriminar(Receta unaReceta);

	void guardarPublica(Receta receta);

	void guardar(Receta receta);
	
	

	void remover(Receta receta);
	
	List<Ingrediente> obtenerIngredientes();
}