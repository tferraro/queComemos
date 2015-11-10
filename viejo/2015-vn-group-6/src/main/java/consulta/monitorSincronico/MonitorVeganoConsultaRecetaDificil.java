package consulta.monitorSincronico;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

@Entity
@DiscriminatorValue("V")
public class MonitorVeganoConsultaRecetaDificil extends MonitorSincronico {

	@Transient
	private Set<String> listaVeganos = new LinkedHashSet<String>();
	private String listaVeganosJunta = "";
	@Transient
	private static MonitorVeganoConsultaRecetaDificil instancia = new MonitorVeganoConsultaRecetaDificil();

	private MonitorVeganoConsultaRecetaDificil() {

	}

	public static MonitorVeganoConsultaRecetaDificil INSTANCIA() {
		return instancia;
	}

	@Override
	public void avisar(List<Receta> lista, Usuario user) {
		if (user.sosVegano() && hayRecetasDificiles(lista)) {
			listaVeganos.add(user.getNombre().toLowerCase());
		}
	}

	private Boolean hayRecetasDificiles(List<Receta> lista) {
		return lista.stream().anyMatch(r -> r.sosDificil());
	}

	@Override
	public void reiniciarEstadistica() {
		listaVeganos.clear();
	}

	public Integer cuantosVeganosConsultaronRecetasDificiles() {
		return listaVeganos.size();
	}
	

	@PrePersist
	private void antesDePersistir() {
		if (!this.listaVeganos.isEmpty())
			this.listaVeganos.stream()
					.forEach(vegano -> this.listaVeganosJunta.concat(vegano).concat(";"));
	}

	@PostConstruct
	private void despuesDeConstruir() {
		int condicion = this.listaVeganosJunta.indexOf(";", 0);
		while (condicion != -1) {
			String vegano = this.listaVeganosJunta.substring(0, condicion);
			this.listaVeganosJunta = this.listaVeganosJunta.substring(condicion + 1,
					this.listaVeganosJunta.length());
			this.listaVeganos.add(vegano);
			condicion = this.listaVeganosJunta.indexOf(";", 0);
		}
	}

}
