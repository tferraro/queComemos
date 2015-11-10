package consulta.monitorSincronico;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Table;
import persistencia.hibernate.PersistentEntity;

@Entity
@Table(name = "Recetas_Consultadas_Sincronicas")
public class RecetaConsultada extends PersistentEntity{

	public String receta = null;
	public Integer repeticiones = 0;
	public String user;

	public RecetaConsultada(String nombre, int repeticiones, String user) {
		this.repeticiones = repeticiones;
		this.receta = nombre;
		this.user = user;
	}

	public String getReceta() {
		return receta;
	}

	public Integer getRepeticiones() {
		return repeticiones;
	}

	public void aumentarRepeticion() {
		this.repeticiones++;
	}

	public static void agregarOSumarReceta(List<RecetaConsultada> lista, String name, String user) {
		if (lista.stream().anyMatch(recetaConsultada -> recetaConsultada.getReceta().equals(name) && recetaConsultada.user.equals(user)))
			repetirReceta(lista, name);
		else
			lista.add(new RecetaConsultada(name, 1, user));
	}

	public static RecetaConsultada recetaMasConsultada(List<RecetaConsultada> lista) {
		List<RecetaConsultada> recetasOrdenadas = lista.stream()
				.sorted((rep1, rep2) -> Integer.compare(rep2.getRepeticiones(), rep1.getRepeticiones()))
				.collect(Collectors.toList());
		return (recetasOrdenadas.isEmpty()) ? null : recetasOrdenadas.get(0);
	}

	private static void repetirReceta(List<RecetaConsultada> lista, String recetaName) {

		List<RecetaConsultada> receta = lista.stream().filter(recConsul -> recConsul.getReceta().equals(recetaName))
				.collect(Collectors.toList());
		receta.get(0).aumentarRepeticion();
	}
}
