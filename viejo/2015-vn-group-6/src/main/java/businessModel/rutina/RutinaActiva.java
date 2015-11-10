package businessModel.rutina;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class RutinaActiva extends Rutina {

	@Override
	public Integer tiempoEjercicio() {
		return 30;
	}

	@Override
	public TipoDeRutina tipo() {
		return TipoDeRutina.ACTIVA;
	}

	@Override
	public String name() {
		return "Activa";
	}

}
