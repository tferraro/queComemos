package businessModel.condicionMedica;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import exceptions.model.receta.ErrorRecetaNoSaludable;
import exceptions.model.usuario.ErrorUsuarioNoSigueRutinaSaludable;
import exceptions.model.usuario.ErrorValidezUsuario;
import businessModel.receta.Receta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.Rutina;
import businessModel.rutina.TipoDeRutina;
import businessModel.usuario.Usuario;

@Entity
@DiscriminatorValue("H")
public class Hipertenso extends CondicionMedica {

	@Transient
	private List<String> condimentosProhibidos = new ArrayList<String>();

	public Hipertenso() {
		condimentosProhibidos.add("Sal");
		condimentosProhibidos.add("Caldo");
	}

	@Override
	public void estadoValido(Usuario usr) {
		if (usr.getComidasQueGustan().isEmpty())
			throw new ErrorValidezUsuario("El usuario es hipertenso y no indico ninguna preferencia");
	}

	@Override
	public void estaSubsanada(Usuario usr) {
		userTieneRutinaActivaIntensiva(usr.getRutina());
	}

	@Override
	public void meEsAdecuadaLaReceta(Receta receta) {
		if (condimentosProhibidos.stream().anyMatch(cond -> receta.contieneCondimento(cond)))
			throw new ErrorRecetaNoSaludable("Hipertenso: Tiene condimentos prohibidos");
	}

	private void userTieneRutinaActivaIntensiva(Rutina rutina) {
		if (rutina.equals(null))
			throw new ErrorUsuarioNoSigueRutinaSaludable("Hipertenso que no tiene rutinas");
		if (!rutina.tipo(TipoDeRutina.ACTIVA) || !rutina.nivel(NivelDeRutina.INTENSIVO))
			throw new ErrorUsuarioNoSigueRutinaSaludable("Hipertenso no sigue una rutina activa intensiva");
	}

	@Override
	public String name() {
		return "Hipertenso";
	}

}
