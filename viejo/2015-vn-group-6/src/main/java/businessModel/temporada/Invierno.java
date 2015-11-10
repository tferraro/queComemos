package businessModel.temporada;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("I")
public class Invierno extends Temporada {

	public Invierno() {
		nombre = "Invierno";
		fechaCelebracion = null;
	}

}
