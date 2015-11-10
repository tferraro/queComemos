package entrega1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import businessModel.condicionMedica.*;
import businessModel.rutina.*;
import businessModel.usuario.*;
import exceptions.model.usuario.ErrorUsuarioNoSigueRutinaSaludable;

public class TestSobreRutinaDeUnUsuario {

	private Usuario userSaludable;

	@Before
	public void setUp() {
		userSaludable = new Usuario();
		userSaludable.setFechaNacimiento(LocalDate.of(1993, 06, 03));
		userSaludable.setNombre("Tomas");
		userSaludable.setSexo(Genero.MASCULINO);
		userSaludable.setAltura(new BigDecimal(1.86));
		userSaludable.getAltura().setScale(2, BigDecimal.ROUND_HALF_UP);
		userSaludable.setPeso(new BigDecimal(82.00));
		userSaludable.getPeso().setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	@Test
	public void usuarioSigueRutinaSaludableSinCondicionesPrex() {
		// No hay Condiciones
		assertEquals(23.7, userSaludable.indiceDeMasaCorporal(), 0);
		userSaludable.sigueRutinaSaludable();
	}

	@Test
	public void usuarioSigueRutinaSaludableConCondicionesPrex() {
		// No hace falta nada
		userSaludable.agregarCondicionMedica(new Celiaco());
		// Tiene una rutina Activa o no pesa mas de 70
		userSaludable.agregarCondicionMedica(new Diabetico());
		userSaludable.setPeso(new BigDecimal(69));
		userSaludable.setRutina(new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO));
		// Rutina Activa con ejercicio adicional
		userSaludable.agregarCondicionMedica(new Hipertenso());
		// Si le gustan las frutas
		userSaludable.agregarCondicionMedica(new Vegano());
		userSaludable.agregarComidasQueGustan("Frutas");
		userSaludable.sigueRutinaSaludable();
	}

	@Test(expected = ErrorUsuarioNoSigueRutinaSaludable.class)
	public void usuarioNoSigueRutinaSaludablePorDiabetico() {
		// Tiene una rutina Activa o no pesa mas de 70
		userSaludable.agregarCondicionMedica(new Diabetico());
		userSaludable.sigueRutinaSaludable();
		userSaludable.setRutina(new RutinaSedentaria().setNivel(NivelDeRutina.NADA));
		userSaludable.sigueRutinaSaludable();
		// Chequear que al pesar menos de 70 ya sea saludable
		userSaludable.setPeso(new BigDecimal(69));
		userSaludable.sigueRutinaSaludable();
	}

	@Test(expected = ErrorUsuarioNoSigueRutinaSaludable.class)
	public void usuarioNoSigueRutinaSaludablePorHipertenso() {
		userSaludable.agregarCondicionMedica(new Hipertenso());
		userSaludable.setRutina(new RutinaActiva().setNivel(NivelDeRutina.NADA));
		userSaludable.sigueRutinaSaludable();
	}

	@Test(expected = ErrorUsuarioNoSigueRutinaSaludable.class)
	public void usuarioNoSigueRutinaSaludablePorVegano() {
		userSaludable.agregarCondicionMedica(new Vegano());
		userSaludable.agregarComidasQueGustan("Chori");
		userSaludable.sigueRutinaSaludable();
	}

	@Test(expected = ErrorUsuarioNoSigueRutinaSaludable.class)
	public void usuarioVeganoDiabeticoNoSigueRutinaSaludableParaDiabetico() {
		userSaludable.agregarCondicionMedica(new Vegano());
		userSaludable.agregarCondicionMedica(new Diabetico());
		userSaludable.agregarComidasQueGustan("Frutas");
		userSaludable.sigueRutinaSaludable();
	}

	@Test(expected = ErrorUsuarioNoSigueRutinaSaludable.class)
	public void usuarioVeganoDiabeticoNoSigueRutinaSaludablePorSusCondiciones() {
		userSaludable.agregarCondicionMedica(new Vegano());
		userSaludable.agregarCondicionMedica(new Diabetico());
		userSaludable.sigueRutinaSaludable();
	}

	@Test
	public void usuarioVeganoDiabeticoSigueRutinaSaludable() {
		userSaludable.agregarCondicionMedica(new Vegano());
		userSaludable.agregarCondicionMedica(new Diabetico());
		userSaludable.setPeso(new BigDecimal(69));
		userSaludable.agregarComidasQueGustan("Frutas");
		userSaludable.setRutina(new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO));
		userSaludable.sigueRutinaSaludable();
	}

	@Test
	public void usuarioDiabeticoHipertensoSigueRutinaSaludable() {
		userSaludable.agregarCondicionMedica(new Diabetico());
		userSaludable.setPeso(new BigDecimal(69));
		userSaludable.agregarCondicionMedica(new Hipertenso());
		userSaludable.agregarComidasQueGustan("Frutas");
		userSaludable.setRutina(new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO));
		userSaludable.sigueRutinaSaludable();
	}
}
