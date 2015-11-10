package entrega2;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import exceptions.model.receta.ErrorNoSePuedeVerOModificarReceta;
import businessModel.receta.Condimento;
import businessModel.receta.Ingrediente;
import businessModel.receta.Receta;
import businessModel.rutina.NivelDeRutina;
import businessModel.rutina.RutinaActiva;
import businessModel.usuario.Grupo;
import businessModel.usuario.Usuario;
import creacionales.BuilderUsuario;

public class TestsSobreGruposYUsuarios {

	private BuilderUsuario builderUsuario;
	private Usuario user, userRolo;
	private Grupo grupoMaster;

	@Before
	public void setUp() throws Exception {
		builderUsuario = new BuilderUsuario();
		builderUsuario.agregarCamposObligatorios("MisterX", new BigDecimal(75), new BigDecimal(1.75),
				LocalDate.of(1991, 05, 21), new RutinaActiva().setNivel(NivelDeRutina.MEDIANO));
		builderUsuario.agregarPreferencias("pollo");
		builderUsuario.agregarDisgustos("pescado");
		user = builderUsuario.compilar();
		builderUsuario.agregarCamposObligatorios("Rolito", new BigDecimal(75), new BigDecimal(1.75),
				LocalDate.of(1992, 05, 21), new RutinaActiva().setNivel(NivelDeRutina.MEDIANO));
		userRolo = builderUsuario.compilar();

		grupoMaster = new Grupo();
		grupoMaster.agregarIntegrante(user);
		grupoMaster.agregarIntegrante(userRolo);

		Receta receta1 = new Receta().setNombre("Pure de Papas");
		Ingrediente papa = new Ingrediente();
		papa.setNombre("Papa");
		papa.setCaloriasIndividuales(200);
		papa.setCantidad(2);
		receta1.agregarIngrediente(papa);
		Ingrediente churrasco = new Ingrediente();
		churrasco.setNombre("Churrasco con Sal");
		churrasco.setCaloriasIndividuales(1000);
		churrasco.setCantidad(1);
		Condimento sal = new Condimento();
		sal.setNombre("Sal");
		sal.setCantidad(5);
		Receta receta2 = new Receta().setNombre("Churrasco a la plancha");
		receta2.agregarIngrediente(churrasco);
		receta2.agregarCondimento(sal);
		user.agregarReceta(receta1);
		userRolo.agregarReceta(receta2);
	}

	@Test
	public void testsSobreRecetasFavoritas() {
		Receta recetaRandom = new Receta().setNombre("Oh! Lala!");
		user.agregarFavorito(recetaRandom);
		assertEquals(1, user.getHistorialRecetas().size());
		user.limpiarHistorialRecetas();
		assertEquals(0, user.getHistorialRecetas().size());
	}

	@Test
	public void usuarioPuedeVerRecetasDeOtroDelMismoGrupo() {
		user.verRecetas(userRolo);
	}

	@Test(expected = ErrorNoSePuedeVerOModificarReceta.class)
	public void usuarioNoPuedeVerRecetasPorNoSerDelGrupo() {
		builderUsuario.agregarCamposObligatorios("Externo", new BigDecimal(75), new BigDecimal(1.75),
				LocalDate.of(1992, 05, 21), new RutinaActiva().setNivel(NivelDeRutina.INTENSIVO));
		Usuario userSinGrupo = builderUsuario.compilar();
		user.verRecetas(userSinGrupo);
	}

	@Test
	public void usuarioPuedeVerRecetasDeTodosLosUsuarioDelGrupo() {
		assertEquals(2, grupoMaster.verRecetasUsuarios(user).size());
	}

}
