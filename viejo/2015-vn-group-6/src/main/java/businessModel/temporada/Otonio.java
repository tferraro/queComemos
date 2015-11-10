package businessModel.temporada;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("O")
public class Otonio extends Temporada {

	public Otonio() {
		nombre = "Otoño";
		fechaCelebracion = null;
	}
}
