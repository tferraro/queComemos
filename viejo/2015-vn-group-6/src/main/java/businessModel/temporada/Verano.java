package businessModel.temporada;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("V")
public class Verano extends Temporada {

	public Verano() {
		nombre = "Verano";
		fechaCelebracion = null;
	}
}
