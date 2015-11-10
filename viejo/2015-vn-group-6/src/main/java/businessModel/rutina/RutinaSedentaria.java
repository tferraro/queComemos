package businessModel.rutina;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
public class RutinaSedentaria extends Rutina {

	@Override
	public Integer tiempoEjercicio() {
		if (this.nivelEjercicio == NivelDeRutina.LEVE)
			return -30;
		if (this.nivelEjercicio == NivelDeRutina.MEDIANO)
			return 30;
		return 0;
	}

	@Override
	public TipoDeRutina tipo() {
		return TipoDeRutina.SEDENTARIA;
	}

	@Override
	public String name() {
		return "Sedentaria";
	}

}
