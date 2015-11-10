package consulta.monitorSincronico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

@Entity
@DiscriminatorValue("M")
public class MonitorRecetasMasConsultadas extends MonitorSincronico {
	@Transient
	private static MonitorRecetasMasConsultadas instancia = new MonitorRecetasMasConsultadas();
	@ManyToMany(cascade = CascadeType.PERSIST)
	List<RecetaConsultada> listaConsultas = new ArrayList<RecetaConsultada>();

	private MonitorRecetasMasConsultadas() {
	}

	public static MonitorRecetasMasConsultadas INSTANCIA() {
		return instancia;
	}

	public void avisar(List<Receta> lista, Usuario user) {
		lista.forEach(rec -> RecetaConsultada.agregarOSumarReceta(listaConsultas, rec.getNombre(), user.getNombre()));
	}

	public RecetaConsultada recetaMasConsultada() {
		List<RecetaConsultada> recetasOrdenadas = this.listaConsultas.stream()
				.sorted((rep1, rep2) -> Integer.compare(rep2.getRepeticiones(), rep1.getRepeticiones()))
				.collect(Collectors.toList());
		return (recetasOrdenadas.isEmpty()) ? null : recetasOrdenadas.get(0);
	}

	public void reiniciarEstadistica() {
		listaConsultas.clear();
	}
}
