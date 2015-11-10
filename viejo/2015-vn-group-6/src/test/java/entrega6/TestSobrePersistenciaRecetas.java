package entrega6;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import persistencia.GestorCuentas;
import persistencia.hibernate.GestorCuentasHibernate;
import persistencia.hibernate.RecetarioHibernate;
import creacionales.BuilderReceta;
import creacionales.BuilderUsuario;
import businessModel.receta.Condimento;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Receta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.temporada.TodoElAnio;
import businessModel.usuario.Usuario;

public class TestSobrePersistenciaRecetas extends AbstractPersistenceTest implements WithGlobalEntityManager {

	private Receta recetaMonchola;
	private RecetarioHibernate repRecetas;
	private Usuario user;
	private GestorCuentas repoUsuarios;

	@Before
	public void setUp() {
		repRecetas = RecetarioHibernate.INSTANCIA();
		repoUsuarios = new GestorCuentasHibernate();
		recetaMonchola = new BuilderReceta().agregarNombre("Arroz con Leche").agregarDescripcion("Arroz con leche")
				.agregarTemporada(new TodoElAnio()).agregarDificultad(DificultadDePreparacionReceta.DIFICIL)
				.agregarIngrediente("Leche", 10, 60).agregarIngrediente("Arroz", 100, 1).agregarCondimento("Sal", 10)
				.compilar();
		user = new BuilderUsuario()
				.agregarCamposObligatorios("un nombre", new BigDecimal(75), new BigDecimal(1.75),
						LocalDate.of(1991, 05, 21), new RutinaActiva().setNivel(NivelDeRutina.MEDIANO))
				.agregarPreferencias("pollo").agregarMail("noName@gmail.com").agregarDisgustos("pescado").compilar();
	}

	@Test
	public void persistirUnaRecetaPublica() {
		repRecetas.guardarPublica(recetaMonchola);

		assertEquals(recetaMonchola.getNombre(), repRecetas.obtener(recetaMonchola).getNombre());
		assertEquals(recetaMonchola.getDescripcion(), repRecetas.obtener(recetaMonchola).getDescripcion());
		assertEquals(recetaMonchola.getDificultad(), repRecetas.obtener(recetaMonchola).getDificultad());
		assertEquals(recetaMonchola.getTemporada().getClass(),
				repRecetas.obtener(recetaMonchola).getTemporada().getClass());
		assertEquals(recetaMonchola.getIngredientes().get(0).getNombre(),
				repRecetas.obtener(recetaMonchola).getIngredientes().get(0).getNombre());
	}

	@Test
	public void persistirRecetaPrivadaConUsuarioChequeandoReferencia() {
		user.agregarReceta(new BuilderReceta().agregarNombre("Arroz con Leche").agregarDescripcion("Arroz con leche")
				.agregarTemporada(new TodoElAnio()).agregarDificultad(DificultadDePreparacionReceta.DIFICIL)
				.agregarIngrediente("Leche", 10, 60).agregarIngrediente("Arroz", 100, 1).agregarCondimento("Sal", 10)
				.setRepositorio(repRecetas).compilar());
		repoUsuarios.guardar(user);
		assertEquals(user.getRecetasPrivadas().get(0).getNombre(),
				repoUsuarios.obtener(user).getRecetasPrivadas().get(0).getNombre());
	}

	@Test
	public void persistirUnaRecetaYBuscarSusCondimentos() {
		Receta recetita = new BuilderReceta().agregarNombre("Arroz con Leche").agregarDescripcion("Arroz con leche")
				.agregarTemporada(new TodoElAnio()).agregarDificultad(DificultadDePreparacionReceta.DIFICIL)
				.agregarIngrediente("Leche", 10, 60).agregarIngrediente("Arroz", 100, 1).setRepositorio(repRecetas)
				.agregarCondimento("Sal", 10).compilar();
		repRecetas.guardarPublica(recetita);
		Condimento sal = recetita.getCondimentos().get(0);
		assertEquals(sal, repRecetas.obtener(recetita).getCondimentos().get(0));

	}

	@Test
	public void persistirRecetaCompuesta() {
		Receta supahReceta = new BuilderReceta().agregarNombre("Arroz con Leche Mk.2")
				.agregarDescripcion("Arroz con leche").agregarTemporada(new TodoElAnio())
				.agregarIngrediente("Leche", 10, 60).agregarIngrediente("Arroz", 100, 1)
				.agregarDificultad(DificultadDePreparacionReceta.DIFICIL).setSubReceta(recetaMonchola).compilar();
		repRecetas.guardarPublica(supahReceta);

		assertEquals(supahReceta.getSubReceta().getNombre(),
				repRecetas.obtener(supahReceta).getSubReceta().getNombre());
	}

	@Test
	public void obtenerRecetasPublicasPersistidas() {
		Receta supahReceta = new BuilderReceta().agregarNombre("Arroz con Leche Mk.2")
				.agregarDescripcion("Arroz con leche").agregarTemporada(new TodoElAnio())
				.agregarIngrediente("Leche", 10, 60).agregarIngrediente("Arroz", 100, 1)
				.agregarDificultad(DificultadDePreparacionReceta.DIFICIL).setSubReceta(recetaMonchola).compilar();
		repRecetas.guardarPublica(supahReceta);
		Receta supahReceta2 = new BuilderReceta().agregarNombre("Arroz con Leche Mk.3")
				.agregarDescripcion("Arroz con leche").agregarTemporada(new TodoElAnio())
				.agregarIngrediente("Leche", 10, 60).agregarIngrediente("Arroz", 100, 1)
				.agregarDificultad(DificultadDePreparacionReceta.DIFICIL).compilar();
		repRecetas.guardarPublica(supahReceta2);

		assertEquals(2, repRecetas.obtener().size());
	}

	@Test
	public void removerRecetaCompuesta() {
		Receta supahReceta = new BuilderReceta().agregarNombre("Arroz con Leche Mk.2")
				.agregarDescripcion("Arroz con leche").agregarTemporada(new TodoElAnio())
				.agregarIngrediente("Leche", 10, 60).agregarIngrediente("Arroz", 100, 1)
				.agregarDificultad(DificultadDePreparacionReceta.DIFICIL).setSubReceta(recetaMonchola).compilar();
		repRecetas.guardarPublica(supahReceta);
		assertEquals(supahReceta.getNombre(), repRecetas.obtener(supahReceta).getNombre());
		repRecetas.remover(supahReceta);
		assertEquals(null, repRecetas.obtener(supahReceta));

	}
}
