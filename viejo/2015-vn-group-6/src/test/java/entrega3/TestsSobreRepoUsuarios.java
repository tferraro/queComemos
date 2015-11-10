package entrega3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import exceptions.model.usuario.ErrorRepoUsuario;
import persistencia.mock.GestorCuentasMock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import businessModel.condicionMedica.CondicionMedica;
import businessModel.condicionMedica.Diabetico;
import businessModel.condicionMedica.Hipertenso;
import businessModel.condicionMedica.Vegano;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.Rutina;
import businessModel.rutina.RutinaActiva;
import businessModel.rutina.RutinaSedentaria;
import businessModel.usuario.*;
import creacionales.BuilderUsuario;

public class TestsSobreRepoUsuarios {

	private BuilderUsuario builder = new BuilderUsuario();
	private GestorCuentasMock repo = GestorCuentasMock.INSTANCIA;

	@Before
	public void setUp() {
		repo.removeAll();
	}

	@Test
	public void agregarUsuarioAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		assertEquals(user, repo.obtener(user));
	}

	@Test(expected = ErrorRepoUsuario.class)
	public void tratarDeObtenerUsuarioNoExistente() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.obtener(user);
	}

	@Test(expected = ErrorRepoUsuario.class)
	public void agregarDosUsuariosConElMismoAlRepoFallando() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		Usuario user2 = builder.compilar();
		repo.guardar(user);
		repo.guardar(user2);
	}

	@Test(expected = ErrorRepoUsuario.class)
	public void removerUsuarioAgregadoAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		repo.remove(user);
		repo.obtener(user);
	}

	@Test(expected = ErrorRepoUsuario.class)
	public void removerUsuarioNoExistenteEnElRepoFallando() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.remove(user);
	}

	@Test
	public void agregarDosUsuariosAlRepoYFiltrarPorNombreDeUno() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		builder.agregarCamposObligatorios("Rita Guarpe", new BigDecimal(65),
				new BigDecimal(1.62), LocalDate.of(1989, 11, 07),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user2 = builder.compilar();
		repo.guardar(user);
		repo.guardar(user2);
		assertEquals(1, repo.list(user).size());
	}

	@Test
	public void agregarDosUsuariosAlRepoYFiltrarPorNombreEnComun() {
		builder.agregarCamposObligatorios("Pedro", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		builder.agregarCamposObligatorios("Pedro Lozano", new BigDecimal(65),
				new BigDecimal(1.62), LocalDate.of(1989, 11, 07),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user2 = builder.compilar();
		repo.guardar(user);
		repo.guardar(user2);
		assertEquals(2, repo.list(user).size());
	}

	@Test
	public void updateUsuarioDePreferenciaAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.agregarPreferencias("Mariscos");
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(1, repo.obtener(userMod).getComidasQueGustan().size());
	}

	@Test
	public void updateUsuarioDeAlturaAlRepo() {
		BigDecimal nuevaAltura = new BigDecimal(1.69).setScale(2, RoundingMode.CEILING);
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.setAltura(nuevaAltura);
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(nuevaAltura, repo.obtener(userMod).getAltura());
	}

	@Test
	public void updateUsuarioDeDisgustosAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.agregarDisgustos("Mariscos");
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(1, repo.obtener(userMod).getComidaQueNoGustan().size());
	}

	@Test
	public void updateUsuarioDeCondicionMedicaAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.agregarCondicionMedica(new Vegano());
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(1, repo.obtener(userMod).getCondicionesMedicas().size());
	}

	@Test
	public void updateUsuarioDeFechaNacimientoAlRepo() {
		LocalDate fechaNueva = LocalDate.of(1993, 06, 04);
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.setFechaNacimiento(fechaNueva);
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(fechaNueva, repo.obtener(userMod).getFechaNacimiento());
	}

	@Test
	public void updateUsuarioDeGruposAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		Usuario userMod = builder.compilar();
		Grupo grupito = new Grupo();
		grupito.agregarIntegrante(userMod);
		repo.update(userMod);
		assertEquals(1, repo.obtener(userMod).getGrupos().size());
	}

	@Test
	public void updateUsuarioDeFavoritosAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.agregarFavorito(new Receta().setNombre("Receta Prueba"));
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(1, repo.obtener(userMod).getHistorialRecetas().size());
	}

	@Test
	public void updateUsuarioDePesoAlRepo() {
		BigDecimal nuevoPeso = new BigDecimal(78);
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.setPeso(nuevoPeso);
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(nuevoPeso, repo.obtener(userMod).getPeso());
	}

	@Test
	public void updateUsuarioDeRecetasAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		Usuario userMod = builder.compilar();
		Receta receta = new Receta().setNombre("Receta Prueba");
		Ingrediente ing = new Ingrediente();
		ing.setNombre("Ingrediente X");
		ing.setCaloriasIndividuales(100);
		ing.setCantidad(1);
		receta.agregarIngrediente(ing);
		userMod.agregarReceta(receta);
		repo.update(userMod);
		assertEquals(1, repo.obtener(userMod).verRecetas(userMod).size());
	}

	@Test
	public void updateUsuarioDeRutinaAlRepo() {
		Rutina nuevaRutina = new RutinaSedentaria().setNivel(NivelDeRutina.LEVE);
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.setRutina(nuevaRutina);
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(nuevaRutina, repo.obtener(userMod).getRutina());
	}

	@Test
	public void updateUsuarioDeSexoAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.setSexo(Genero.MASCULINO);
		Usuario userMod = builder.compilar();
		repo.update(userMod);
		assertEquals(Genero.MASCULINO, repo.obtener(userMod).getSexo());
	}

	@Test(expected = ErrorRepoUsuario.class)
	public void updateErroneoPorDiferenciaDeNombreAlRepo() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		Usuario user = builder.compilar();
		repo.guardar(user);
		builder.agregarCamposObligatorios("Pedro", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		builder.agregarPreferencias("Mariscos");
		Usuario userMod = builder.compilar();
		repo.update(userMod);
	}

	@Test
	public void testSobreIgualdadDeCondicionesIguales() {
		CondicionMedica condicion1 = new Vegano();
		CondicionMedica condicion2 = new Vegano();
		assertEquals(true, condicion1.mismaCondicion(condicion2));
	}

	@Test
	public void testSobreIgualdadDeCondicionesFallaPorDIferentes() {
		CondicionMedica condicion1 = new Vegano();
		CondicionMedica condicion2 = new Diabetico();
		assertEquals(false, condicion1.mismaCondicion(condicion2));
	}

	@Test
	public void testSobreChequeoDeCondicionesIgualesEntreUsuarios() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		builder.agregarCondicionMedica(new Vegano());
		Usuario user = builder.compilar();
		Usuario testUser = new Usuario();
		testUser.setNombre("Pedro");
		testUser.agregarCondicionMedica(new Vegano());
		assertEquals(true, user.poseeCondiciones(testUser));
	}

	@Test
	public void testSobreChequeoDeCondicionesIgualesEntreUsuariosFallaPorNoSerIguales() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		builder.agregarCondicionMedica(new Vegano());
		Usuario user = builder.compilar();
		Usuario testUser = new Usuario();
		testUser.setNombre("Pedro");
		testUser.agregarCondicionMedica(new Diabetico());
		assertEquals(false, user.poseeCondiciones(testUser));
	}

	public void listarPorNombreYCondicionMedicaUnica() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		builder.agregarCondicionMedica(new Vegano());
		repo.guardar(builder.compilar());
		builder.reset();
		builder.agregarCamposObligatorios("Pedro Calvo", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		builder.agregarCondicionMedica(new Hipertenso());
		builder.agregarPreferencias("Mariscos");
		repo.guardar(builder.compilar());

		Usuario testUser = new Usuario();
		testUser.setNombre("Pedro");
		// Test de MismoNombre sin Condiciones
		assertEquals(2, repo.list(testUser).size());
		// Buscar por Hipertenso
		testUser.agregarCondicionMedica(new Hipertenso());
		assertEquals(1, repo.list(testUser).size());
	}

	public void listarPorNombreYCondicionMedicaMultiple() {
		builder.agregarCamposObligatorios("Pedro Alfonso", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		builder.agregarCondicionMedica(new Vegano());
		builder.agregarCondicionMedica(new Hipertenso());
		builder.agregarPreferencias("Mariscos");
		repo.guardar(builder.compilar());
		builder.reset();
		builder.agregarCamposObligatorios("Pedro Calvo", new BigDecimal(79),
				new BigDecimal(1.79), LocalDate.of(1993, 06, 03),
				new RutinaActiva().setNivel(NivelDeRutina.LEVE));
		builder.agregarCondicionMedica(new Hipertenso());
		builder.agregarPreferencias("Mariscos");
		repo.guardar(builder.compilar());

		Usuario testUser = new Usuario();
		testUser.setNombre("P");
		// Buscar por Hipertenso
		testUser.agregarCondicionMedica(new Hipertenso());
		assertEquals(2, repo.list(testUser).size());
		// Buscar por Hipertenso y Vegano
		testUser.agregarCondicionMedica(new Vegano());
		assertEquals(1, repo.list(testUser).size());
	}
}
