package persistencia.hibernate;

import java.util.List;
import java.util.stream.Collectors;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import persistencia.Recetario;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;

public class RecetarioHibernate implements Recetario, WithGlobalEntityManager {

	private static RecetarioHibernate INSTANCIA = new RecetarioHibernate();

	public static RecetarioHibernate INSTANCIA() {
		return INSTANCIA;
	}

	private RecetarioHibernate() {

	}

	String query = "SELECT r FROM Receta r WHERE publica = true";

	@SuppressWarnings("unchecked")
	@Override
	public List<Receta> obtener() {
		return (List<Receta>) entityManager().createQuery(query)
				.getResultList();
	}

	public Receta obtener(Receta unaReceta) {
		String query_name = query + " AND nombre ='" + unaReceta.getNombre()
				+ "'";
		@SuppressWarnings("unchecked")
		List<Receta> result = (List<Receta>) entityManager().createQuery(
				query_name).getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public void guardarPublica(Receta receta) {
		receta.volverPublica();
		entityManager().persist(receta);
	}

	@Override
	public void guardar(Receta receta) {
		entityManager().persist(receta);
	}
	
	@Override
	public void remover(Receta receta) {
		entityManager().remove(receta);
	}

	public Receta obtenerSinDiscriminar(Receta unaReceta) {
		String query_name = "SELECT r FROM Receta r WHERE nombre ='"
				+ unaReceta.getNombre() + "'";
		@SuppressWarnings("unchecked")
		List<Receta> result = (List<Receta>) entityManager().createQuery(
				query_name).getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	//@SuppressWarnings("unchecked")
	@Override
	public List<Ingrediente> obtenerIngredientes() {
		List<Receta> result = obtener();
		List<Ingrediente> lista = result.stream().flatMap(r -> r.getIngredientesTotales().stream()).collect(Collectors.toList());
		return lista;
		//return (List<Ingrediente>) entityManager().createQuery("SELECT i FROM Ingrediente i").getResultList();
	}
}
