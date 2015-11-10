package entrega1;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import exceptions.model.usuario.ErrorValidezUsuario;
import businessModel.condicionMedica.Celiaco;
import businessModel.condicionMedica.Hipertenso;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import creacionales.BuilderUsuario;

public class TestBuilderUsuario {

	private BuilderUsuario builder;

	@Before
	public void setUp() throws Exception {
		builder = new BuilderUsuario();
	}

	@Test
	public void creacionDeUsuarioValidaSinCondicionesMedicas() {
		builder.agregarCamposObligatorios("MisterX", new BigDecimal(75),
				new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
				new RutinaActiva().setNivel(NivelDeRutina.MEDIANO));
		builder.agregarPreferencias("pollo");
		builder.agregarDisgustos("pescado");
		builder.compilar();
	}

	@Test
	public void creacionDeUsuarioValidaConCondicionesMedicas() {
		builder.agregarCamposObligatorios("MisterX", new BigDecimal(75),
				new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
				new RutinaActiva().setNivel(NivelDeRutina.MEDIANO));
		builder.agregarCondicionMedica(new Celiaco());
		builder.compilar();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void creacionDeUsuarioInvalidaPorFaltaDeCamposObligatorios() {
		builder.agregarCamposObligatorios("", new BigDecimal(0),
				new BigDecimal(0), LocalDate.of(1991, 05, 21),
				new RutinaActiva().setNivel(NivelDeRutina.MEDIANO));
		builder.compilar();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void creacionDeUsuarioInvalidaPorCondicionesMedicas() {
		builder.agregarCamposObligatorios("MisterX", new BigDecimal(75),
				new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
				new RutinaActiva().setNivel(NivelDeRutina.MEDIANO));
		builder.agregarCondicionMedica(new Hipertenso());
		builder.compilar();
	}

}
