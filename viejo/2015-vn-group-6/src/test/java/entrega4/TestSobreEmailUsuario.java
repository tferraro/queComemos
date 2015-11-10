package entrega4;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;

import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import creacionales.BuilderUsuario;
import exceptions.model.usuario.ErrorValidezUsuario;

public class TestSobreEmailUsuario {

	@Test
	public void crearUsuarioConEmailValido() {
		new BuilderUsuario()
				.agregarCamposObligatorios("Pedrito", new BigDecimal(78), new BigDecimal(1.98),
						LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarMail("pedroFargo@gmail.com").compilar();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void crearUsuarioConEmailNoValido() {
		new BuilderUsuario()
				.agregarCamposObligatorios("Pedrito", new BigDecimal(78), new BigDecimal(1.98),
						LocalDate.of(2011, 04, 1), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarMail("<pedr!oFargo@gmail.com").compilar();
	}

}
