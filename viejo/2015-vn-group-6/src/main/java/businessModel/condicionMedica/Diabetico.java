package businessModel.condicionMedica;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import exceptions.model.receta.ErrorRecetaNoSaludable;
import exceptions.model.usuario.ErrorUsuarioNoSigueRutinaSaludable;
import exceptions.model.usuario.ErrorValidezUsuario;
import businessModel.receta.Receta;
import businessModel.rutina.Rutina;
import businessModel.rutina.TipoDeRutina;
import businessModel.usuario.Genero;
import businessModel.usuario.Usuario;

@Entity
@DiscriminatorValue("D")
public class Diabetico extends CondicionMedica {

	@Override
	public void estadoValido(Usuario usr) {
		userTieneSexoDefinido(usr.getSexo());
		userTienePreferenciasValidas(usr.getComidasQueGustan());
	}

	@Override
	public void estaSubsanada(Usuario usr) {
		subsanaPeso(usr);
		userTieneRutinaActiva(usr.getRutina());
	}

	@Override
	public void meEsAdecuadaLaReceta(Receta receta) {
		if (receta.poseeCondimentoConAlMenosUnaCantidad("Azucar", 100))
			throw new ErrorRecetaNoSaludable("Diabetico: Tiene mas de 100g de Azucar");
	}

	private void userTienePreferenciasValidas(List<String> comidasQueGustan) {
		if (comidasQueGustan.isEmpty())
			throw new ErrorValidezUsuario("El usuario es diabetico y no indico ninguna preferencia");
	}

	private void userTieneSexoDefinido(Genero sexo) {
		if (Genero.NA.equals(sexo))
			throw new ErrorValidezUsuario("El usuario es diabetico y no indico sexo");
	}

	private void userTieneRutinaActiva(Rutina rutina) {
		if (rutina.equals(null))
			throw new ErrorUsuarioNoSigueRutinaSaludable("Diabetico que no tiene rutinas");
		if (!rutina.tipo(TipoDeRutina.ACTIVA))
			throw new ErrorUsuarioNoSigueRutinaSaludable("Diabetico no sigue rutina activa");
	}

	private void subsanaPeso(Usuario usr) {
		if (usr.compararPeso(70) > 0)
			throw new ErrorUsuarioNoSigueRutinaSaludable("Diabetico que pesa más de 70kg");
	}

	@Override
	public String name() {
		return "Diabético";
	}

}
