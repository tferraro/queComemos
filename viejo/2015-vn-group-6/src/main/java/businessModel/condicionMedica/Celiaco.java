package businessModel.condicionMedica;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

@Entity
@DiscriminatorValue("C")
public class Celiaco extends CondicionMedica {

	@Override
	public void estadoValido(Usuario usr) {
	}

	@Override
	public void estaSubsanada(Usuario usr) {
	}

	@Override
	public void meEsAdecuadaLaReceta(Receta receta) {
	}

	@Override
	public String name() {
		return "Celiaco";
	}

}
