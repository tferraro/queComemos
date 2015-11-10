package entrega6;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import creacionales.BuilderReceta;
import creacionales.BuilderUsuario;
import exceptions.model.usuario.ErrorRepoUsuario;
import businessModel.condicionMedica.Celiaco;
import businessModel.condicionMedica.Diabetico;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Receta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.temporada.TodoElAnio;
import businessModel.usuario.Genero;
import businessModel.usuario.Grupo;
import businessModel.usuario.Usuario;
import persistencia.GestorCuentas;
import persistencia.hibernate.GestorCuentasHibernate;
import persistencia.hibernate.RecetarioHibernate;
import persistencia.mock.GestorCuentasMock;

public class TestSobrePersistenciaUsuario extends AbstractPersistenceTest
		implements WithGlobalEntityManager {

	private GestorCuentas repoUsuarios;
	private GestorCuentas repoUsuariosMock;
	private Usuario gigolo;
	private Grupo gigoloGroup;
	private Receta favorita;

	@Before
	public void setUp() {
		repoUsuarios = new GestorCuentasHibernate();
		repoUsuariosMock = GestorCuentasMock.INSTANCIA;

		favorita = new BuilderReceta().agregarNombre("Arroz con Leche")
				.agregarDescripcion("Arroz con leche")
				.agregarTemporada(new TodoElAnio())
				.agregarDificultad(DificultadDePreparacionReceta.DIFICIL)
				.agregarIngrediente("Leche", 10, 60)
				.agregarIngrediente("Arroz", 100, 1)
				.agregarCondimento("Sal", 10).compilar();

		gigolo = new BuilderUsuario()
				.agregarCamposObligatorios("gigolo", new BigDecimal(75),
						new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarPreferencias("papas")
				.agregarMail("allthenigth@gmail.com")
				.agregarDisgustos("pescado").compilar();
		gigolo.agregarFavorito(favorita);

		repoUsuarios.guardar(gigolo);

		gigoloGroup = new Grupo();
		gigoloGroup.setNombre("El grupete del gigolo");
		gigoloGroup.agregarIntegrante(gigolo);

		repoUsuarios.guardar(gigoloGroup);
	}

	@Test
	public void persistenciaMockDeUsuario() {
		Usuario juan = new BuilderUsuario()
				.agregarCamposObligatorios("juanF", new BigDecimal(75),
						new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarMail("juanF@gmail.com")
				.agregarDisgustos("pescado").setRepositorio(repoUsuariosMock)
				.compilar();
		assertEquals(juan, repoUsuariosMock.obtener(juan));
	}

	@Test
	public void persistirPostaAlUsuario() {
		Usuario juan = new BuilderUsuario()
				.agregarCamposObligatorios("juanF", new BigDecimal(75),
						new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarMail("juanF@gmail.com")
				.agregarDisgustos("pescado").setRepositorio(repoUsuarios)
				.compilar();
		assertEquals(juan, repoUsuarios.obtener(juan));
	}

	@Test
	public void persistirUsuarioConCondicionesMedicas() {
		Usuario userConCondiciones = new BuilderUsuario()
				.agregarCamposObligatorios("Guido", new BigDecimal(75),
						new BigDecimal(1.72), LocalDate.of(1993, 06, 04),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarSexo(Genero.MASCULINO)
				.agregarMail("gggg@gmail.com").agregarDisgustos("pescado")
				.agregarCondicionMedica(new Celiaco())
				.agregarCondicionMedica(new Diabetico())
				.setRepositorio(repoUsuarios).compilar();

		assertEquals(2, repoUsuarios.obtener(userConCondiciones)
				.getCondicionesMedicas().size());
	}

	@Test
	public void persistenciaVerificandoAtributosDelUsuarioConElPersistido() {
		Usuario userOriginal = new BuilderUsuario()
				.agregarCamposObligatorios("Pedrito", new BigDecimal(78),
						new BigDecimal(1.98), LocalDate.of(2011, 04, 1),
						new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO))
				.agregarMail("pedroFargo@gmail.com")
				.agregarPreferencias("enchilada")
				.agregarPreferencias("chorizo").agregarSexo(Genero.MASCULINO)
				.agregarCondicionMedica(new Diabetico())
				.setRepositorio(repoUsuarios).compilar();

		Usuario userPersistido = repoUsuarios.obtener(userOriginal);

		assertEquals(userOriginal.getNombre(), userPersistido.getNombre());
		assertEquals(userOriginal.getSexo(), userPersistido.getSexo());
		assertEquals(userOriginal.getFechaNacimiento(),
				userPersistido.getFechaNacimiento());
		assertEquals(userOriginal.getPeso().doubleValue(), userPersistido
				.getPeso().doubleValue(), 0.000003);
		assertEquals(userOriginal.getAltura().doubleValue(), userPersistido
				.getAltura().doubleValue(), 0.000003);
		assertEquals(userOriginal.getComidasQueGustan(),
				userPersistido.getComidasQueGustan());
		assertEquals(userOriginal.getComidaQueNoGustan(),
				userPersistido.getComidaQueNoGustan());
		assertEquals(userOriginal.getMail(), userPersistido.getMail());
	}

	@Test
	public void actualizarAlGigoloYPersistirlo() {
		gigolo.agregarComidaQueNoGustan("mortadela");
		gigolo.agregarComidasQueGustan("asado");

		repoUsuarios.guardar(gigolo);

		Usuario gigoloPersistido = repoUsuarios.obtener(gigolo);

		assertEquals(gigolo, gigoloPersistido);
		assertEquals(gigolo.getSexo(), gigoloPersistido.getSexo());
		assertEquals(gigolo.getFechaNacimiento(),
				gigoloPersistido.getFechaNacimiento());
		assertEquals(gigolo.getPeso().doubleValue(), gigoloPersistido.getPeso()
				.doubleValue(), 0.000003);
		assertEquals(gigolo.getAltura().doubleValue(), gigoloPersistido
				.getAltura().doubleValue(), 0.000003);
		assertEquals(gigolo.getComidasQueGustan(),
				gigoloPersistido.getComidasQueGustan());
		assertEquals(gigolo.getComidaQueNoGustan(),
				gigoloPersistido.getComidaQueNoGustan());
		assertEquals(gigolo.getMail(), gigoloPersistido.getMail());
	}

	@Test
	public void persistirRecetaFavoritaDelGigolo() {
		RecetarioHibernate recetas = RecetarioHibernate.INSTANCIA();
		recetas.guardarPublica(favorita);

		assertEquals(recetas.obtener(favorita), repoUsuarios.obtener(gigolo)
				.getHistorialRecetas().get(0));
	}

	@Test
	public void persistirRecetaFavoritaDelGigoloYQuitarDeFavoritos() {
		RecetarioHibernate recetas = RecetarioHibernate.INSTANCIA();
		recetas.guardarPublica(favorita);

		repoUsuarios.obtener(gigolo).removerFavorito(favorita);

		assertTrue(gigolo.getHistorialRecetas().isEmpty());
	}

	@Test
	public void persistirUnGrupoConSusUsuariosPersistidos() {
		Usuario pepe = new BuilderUsuario()
				.agregarCamposObligatorios("MisterX", new BigDecimal(75),
						new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarMail("mixterX@gmail.com")
				.agregarDisgustos("pescado").setRepositorio(repoUsuarios)
				.compilar();
		Usuario juan = new BuilderUsuario()
				.agregarCamposObligatorios("juanF", new BigDecimal(75),
						new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarMail("juanF@gmail.com")
				.agregarDisgustos("pescado").setRepositorio(repoUsuarios)
				.compilar();
		Grupo grupin = new Grupo();
		grupin.setNombre("El Grupin");
		grupin.agregarIntegrante(pepe);
		grupin.agregarIntegrante(juan);
		grupin.agregarInteres("stuff");
		grupin.agregarInteres("Comida");
		repoUsuarios.guardar(grupin);
		assertEquals(2, repoUsuarios.obtener(grupin).getIntegrantes().size());
		assertEquals(pepe, repoUsuarios.obtener(grupin).getIntegrantes().get(0));
		assertEquals(juan, repoUsuarios.obtener(grupin).getIntegrantes().get(1));
	}

	@Test
	public void grupoPersistidoObtenerloYActualizarlo() {
		gigoloGroup.agregarInteres("polo");
		gigoloGroup.agregarInteres("dinero");

		assertEquals(2, repoUsuarios.obtener(gigoloGroup).getIntereses().size());
	}

	@Test
	public void obtenerRecetaDelGigoloDesdeSuGrupo() throws Exception {
		gigolo.agregarReceta(new BuilderReceta().agregarNombre("Gigolo")
				.agregarDescripcion("Arroz con leche")
				.agregarTemporada(new TodoElAnio())
				.agregarDificultad(DificultadDePreparacionReceta.DIFICIL)
				.agregarIngrediente("Leche", 10, 60)
				.agregarIngrediente("Arroz", 100, 1)
				.setRepositorio(RecetarioHibernate.INSTANCIA())
				.agregarCondimento("Sal", 10).compilar());

		assertEquals(gigolo.getRecetasPrivadas().get(0),
				repoUsuarios.obtener(gigoloGroup).getIntegrantes().get(0)
						.getRecetasPrivadas().get(0));

	}

	@Test
	public void obtenerFavoritasDelGigoloDesDeElGrupo() {
		RecetarioHibernate recetas = RecetarioHibernate.INSTANCIA();
		recetas.guardarPublica(favorita);

		assertEquals(gigolo.getHistorialRecetas().get(0),
				repoUsuarios.obtener(gigoloGroup).getIntegrantes().get(0)
						.getHistorialRecetas().get(0));
	}

	@Test
	public void removerUsuarioPersistido() {
		Usuario gigoloRemovido = new BuilderUsuario()
				.agregarCamposObligatorios("gigolo4", new BigDecimal(75),
						new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarPreferencias("papas")
				.agregarMail("allthenigth@gmail.com")
				.agregarDisgustos("pescado").setRepositorio(repoUsuarios)
				.compilar();
		assertEquals(gigoloRemovido.getNombre(),
				repoUsuarios.obtener(gigoloRemovido).getNombre());
		repoUsuarios.remover(gigoloRemovido);
		try {
			repoUsuarios.obtener(gigoloRemovido);
			fail("No Exception Throwed");
		} catch (ErrorRepoUsuario e) {
			assertEquals("getUsuario: No existe Usuario con dicho Nombre",
					e.getMessage());
		}
	}

	@Test
	public void removerGrupoPersistido() {
		Usuario pepe = new BuilderUsuario()
				.agregarCamposObligatorios("MisterX", new BigDecimal(75),
						new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarMail("mixterX@gmail.com")
				.agregarDisgustos("pescado").setRepositorio(repoUsuarios)
				.compilar();
		Usuario juan = new BuilderUsuario()
				.agregarCamposObligatorios("juanF", new BigDecimal(75),
						new BigDecimal(1.75), LocalDate.of(1991, 05, 21),
						new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarMail("juanF@gmail.com")
				.agregarDisgustos("pescado").setRepositorio(repoUsuarios)
				.compilar();
		Grupo grupin = new Grupo();
		grupin.setNombre("El Grupeton");
		grupin.agregarIntegrante(pepe);
		grupin.agregarIntegrante(juan);
		grupin.agregarInteres("stuff");
		grupin.agregarInteres("Comida");
		repoUsuarios.guardar(grupin);
		assertEquals(grupin.getNombre(), repoUsuarios.obtener(grupin)
				.getNombre());
		repoUsuarios.remover(grupin);
		try {
			repoUsuarios.obtener(grupin);
			fail("No tiro una excepcion");
		} catch (ErrorRepoUsuario e) {
			assertEquals("getGrupo: No existe Grupo con dicho Nombre", e.getMessage());
		}
	}

}
