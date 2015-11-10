package businessModel.condicionMedica;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;
import exceptions.model.receta.ErrorRecetaNoSaludable;
import exceptions.model.usuario.ErrorUsuarioNoSigueRutinaSaludable;
import exceptions.model.usuario.ErrorValidezUsuario;

@Entity
@DiscriminatorValue("V")
public class Vegano extends CondicionMedica {

	@Transient
	private List<String> preferenciasProhibidas = new ArrayList<String>();

	public Vegano() {
		preferenciasProhibidas.add("Pollo");
		preferenciasProhibidas.add("Carne");
		preferenciasProhibidas.add("Chivito");
		preferenciasProhibidas.add("Chori");
		preferenciasProhibidas.add("pollo");
		// Se toma como condicion que las preferencias comiencen con mayusculas
	}

	@Override
	public void estadoValido(Usuario usr) {
		if (preferenciasProhibidas.stream().anyMatch(pref -> usr.leGustaComida(pref)))
			throw new ErrorValidezUsuario("No es valido es vegano y tiene como preferencias comidas prohibidas");
	}

	@Override
	public void estaSubsanada(Usuario usr) {
		if (!usr.leGustaComida("Frutas"))
			throw new ErrorUsuarioNoSigueRutinaSaludable("Vegano que no le gustan las frutas");
	}

	@Override
	public void meEsAdecuadaLaReceta(Receta receta) {
		if (preferenciasProhibidas.stream().anyMatch(pref -> receta.contieneIngrediente(pref)))
			throw new ErrorRecetaNoSaludable("Vegano: Tiene ingredientes prohibidos");
	}

	@Override
	public String name() {
		return "Vegano";
	}
}
