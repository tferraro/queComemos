package businessModel.temporada;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("P")
public class Primavera extends Temporada {

	public Primavera() {
		nombre = "Primavera";
		fechaCelebracion = null;
	}
}
