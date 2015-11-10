package webGUI.runner;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import businessModel.condicionMedica.Diabetico;
import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Receta;
import businessModel.rutina.RutinaActiva;
import businessModel.temporada.Invierno;
import businessModel.usuario.Genero;
import creacionales.BuilderReceta;
import creacionales.BuilderUsuario;
import externalEntities.recetas.RepoRecetasExterno;
import persistencia.GestorCuentas;
import persistencia.Recetario;

public class JuegoDeDatosMock implements JuegoDeDatos, WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

	@Override
	public void crearDatos(Recetario recetario, GestorCuentas gestor) {

		withTransaction(() -> {
			RepoRecetasExterno.INSTANCIA.getRecetas(new Receta()).forEach(r -> recetario.guardarPublica(r));
		});
		withTransaction(() -> {
			Receta rece = new BuilderReceta().agregarNombre("Lemon Pie").agregarDescripcion("Torta de Limon")
					.agregarTemporada(new Invierno()).agregarDificultad(DificultadDePreparacionReceta.MEDIA)
					.agregarIngrediente("Limon", 2, 30).agregarIngrediente("rayadura de 1 Limon", 1, 5)
					.agregarIngrediente("huevo", 2, 30).agregarCondimento("azucar", 2).compilar();
			recetario.guardar(rece);
			gestor.guardar(new BuilderUsuario()
					.agregarCamposObligatorios("Pedro", new BigDecimal(87), new BigDecimal(1.86),
							LocalDate.of(1993, 06, 03), new RutinaActiva())
					.setSexo(Genero.MASCULINO).setPassword("1234").agregarPreferencias("Peceto")
					.agregarPreferencias("Colita de tu mama").agregarDisgustos("Humus")
					.agregarCondicionMedica(new Diabetico())//.agregarFavorito(recetario.obtener().get(0))
					//.agregarFavorito(recetario.obtenerSinDiscriminar(rece))
					.compilar());
			gestor.guardar(new BuilderUsuario()
					.agregarCamposObligatorios("Luisito", new BigDecimal(75), new BigDecimal(1.71),
							LocalDate.of(1992, 01, 12), new RutinaActiva())
					.setSexo(Genero.MASCULINO).setPassword("1234").compilar());
			gestor.guardar(new BuilderUsuario()
					.agregarCamposObligatorios("Lucia", new BigDecimal(63), new BigDecimal(1.61),
							LocalDate.of(1994, 2, 10), new RutinaActiva())
					.setSexo(Genero.FEMENINO).setPassword("1234a").compilar());
		});
	}

}
