package businessModel.rutina;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import persistencia.hibernate.PersistentEntity;

@Entity
@Table(name = "Rutinas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TipoRutina")
public abstract class Rutina extends PersistentEntity {

	@Enumerated
	protected NivelDeRutina nivelEjercicio = NivelDeRutina.NADA;

	@Enumerated
	public abstract TipoDeRutina tipo();

	public abstract Integer tiempoEjercicio();

	public Boolean tipo(TipoDeRutina rutina) {
		return this.tipo().equals(rutina);
	}

	public Boolean nivel(NivelDeRutina nivel) {
		return this.nivelEjercicio.equals(nivel);
	}

	public Rutina setNivel(NivelDeRutina lvl) {
		this.nivelEjercicio = lvl;
		return this;
	}
	
	public abstract String name();

	public String lvlName() {
		if(nivelEjercicio != null)
			return nivelEjercicio.toString();
		return "";
	}
}
