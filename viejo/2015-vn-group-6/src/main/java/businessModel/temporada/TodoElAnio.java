package businessModel.temporada;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("T")
public class TodoElAnio extends Temporada {

	public TodoElAnio() {
		this.nombre = "Todo el Año";
		this.fechaCelebracion = null;
	}
}
