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
@DiscriminatorValue("I")
public class MonitorDiezRecetasMasConsultadas extends MonitorSincronico {
	@Transient
	private static MonitorDiezRecetasMasConsultadas instancia = new MonitorDiezRecetasMasConsultadas();
	@ManyToMany(cascade = CascadeType.PERSIST)
	List<RecetaConsultada> listaConsultas = new ArrayList<RecetaConsultada>();

	private MonitorDiezRecetasMasConsultadas() {
	}

	public static MonitorDiezRecetasMasConsultadas INSTANCIA() {
		return instancia;
	}

	public void avisar(List<Receta> lista, Usuario user) {
		lista.forEach(rec -> RecetaConsultada.agregarOSumarReceta(
				listaConsultas, rec.getNombre(), user.getNombre()));
	}

	public List<RecetaConsultada> recetasMasConsultada() {
		return this.listaConsultas
				.stream()
				.sorted((rep1, rep2) -> Integer.compare(rep2.getRepeticiones(),
						rep1.getRepeticiones())).limit(10)
				.collect(Collectors.toList());
	}

	public List<RecetaConsultada> recetasMasConsultada(Usuario user) {
		return this.listaConsultas
				.stream()
				.sorted((rep1, rep2) -> Integer.compare(rep2.getRepeticiones(),
						rep1.getRepeticiones()))
				.filter(r -> {
					Usuario u = new Usuario();
					u.setNombre(r.user);
					return user.mismoNombre(u);
				}).limit(10).collect(Collectors.toList());
	}

	public void reiniciarEstadistica() {
		listaConsultas.clear();
	}
}