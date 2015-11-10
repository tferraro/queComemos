package entrega1;

import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import businessModel.condicionMedica.*;
import businessModel.rutina.*;
import businessModel.usuario.*;
import exceptions.model.usuario.*;

public class TestSobreValidezUsuario {

	private Usuario userValido;
	private Usuario userInvalido;
	private Rutina rutinaMock;
	private CondicionMedica condicionMock;

	@Before
	public void setUp() {
		userValido = new Usuario();
		userValido.setAltura(new BigDecimal(1.86));
		userValido.getAltura().setScale(2, BigDecimal.ROUND_HALF_UP);
		userValido.setFechaNacimiento(LocalDate.of(1993, 06, 03));
		userValido.setNombre("Tomas");
		userValido.setPeso(new BigDecimal(82.0));
		userValido.getPeso().setScale(2, BigDecimal.ROUND_HALF_UP);
		userValido.setSexo(Genero.MASCULINO);

		userInvalido = new Usuario();
		userInvalido.setAltura(new BigDecimal(1.86));
		userInvalido.getAltura().setScale(2, BigDecimal.ROUND_HALF_UP);
		userInvalido.setPeso(new BigDecimal(82.0));
		userInvalido.getPeso().setScale(2, BigDecimal.ROUND_HALF_UP);
		userInvalido.setNombre("Tomas");
		userInvalido.setSexo(Genero.MASCULINO);
		userInvalido.setFechaNacimiento(LocalDate.of(1993, 06, 03));

		rutinaMock = Mockito.mock(Rutina.class);
		condicionMock = Mockito.mock(CondicionMedica.class);
	}

	@Test
	public void usuarioEsValidoConMockCondicionMedica() {
		userValido.setRutina(rutinaMock);
		userValido.agregarCondicionMedica(condicionMock);
		userValido.esValido();
	}

	@Test
	public void usuarioEsValidoSinMock() {
		userValido.setRutina(new RutinaSedentaria().setNivel(NivelDeRutina.NADA));
		userValido.agregarCondicionMedica(new Diabetico());
		userValido.agregarCondicionMedica(new Hipertenso());
		userValido.agregarCondicionMedica(new Vegano());
		userValido.agregarComidasQueGustan("Fruta");
		userValido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioEsInvalidoPorFaltaDeCampoObligatorio() {
		userInvalido.setPeso(new BigDecimal(0));
		userInvalido.setRutina(rutinaMock);
		userInvalido.agregarCondicionMedica(condicionMock);
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioEsInvalidoPorNombreNoMayorDe4Caracteres() {
		userInvalido.setNombre("Tom");
		userInvalido.setRutina(rutinaMock);
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioEsInvalidoPorFechaInvalida() {
		userInvalido.setFechaNacimiento(LocalDate.of(2089, 06, 03));
		userInvalido.setRutina(rutinaMock);
		userInvalido.agregarCondicionMedica(condicionMock);
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioEsInvalidoPorFaltaDeRutina() {
		userInvalido.agregarCondicionMedica(condicionMock);
		userInvalido.esValido();
	}

	@Test
	public void usuarioEsInvalidoPorCondicionMedicaMockInvalida() {
		userInvalido.setRutina(rutinaMock);
		userInvalido.agregarCondicionMedica(condicionMock);
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioEsInvalidoPorDiabeticoPorSexo() {
		userInvalido.agregarCondicionMedica(new Diabetico());
		userInvalido.setSexo(Genero.NA);
		userInvalido.setRutina(rutinaMock);
		userInvalido.agregarComidasQueGustan("Fruta");
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioEsInvalidoPorDiabeticoPorPreferencia() {
		userInvalido.agregarCondicionMedica(new Diabetico());
		userInvalido.setRutina(rutinaMock);
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioEsInvalidoPorHipertenso() {
		userInvalido.agregarCondicionMedica(new Hipertenso());
		userInvalido.setRutina(rutinaMock);
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioEsInvalidoPorVegano() {
		userInvalido.agregarCondicionMedica(new Vegano());
		userInvalido.agregarComidasQueGustan("Chori");
		userInvalido.setRutina(rutinaMock);
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioDiabeticoHipertensoEsInvalidoPorSuCondicionDeDiabeticoPorNoIndicarSexo() {
		userInvalido.agregarCondicionMedica(new Hipertenso());
		userInvalido.agregarCondicionMedica(new Diabetico());
		userInvalido.setRutina(rutinaMock);
		userInvalido.agregarComidasQueGustan("comida");
		userInvalido.setSexo(Genero.NA);
		userInvalido.esValido();
	}

	@Test(expected = ErrorValidezUsuario.class)
	public void usuarioCeliacoHipertensoEsInvalidoPorSuCondicionDeHipertensoPorNoTenerComidasQueGustan() {
		userInvalido.agregarCondicionMedica(new Hipertenso());
		userInvalido.agregarCondicionMedica(new Celiaco());
		userInvalido.setRutina(rutinaMock);
		userInvalido.esValido();
	}

}
