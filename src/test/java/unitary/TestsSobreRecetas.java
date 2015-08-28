package unitary;

import static org.junit.Assert.*;

import org.junit.Test;

import creacionales.CreadorRecetas;
import model.Receta;

public class TestsSobreRecetas {

	@Test
	public void probarBuilderRecetaSencillo() {
		Receta nuevaReceta = new CreadorRecetas().nombre("Receta Prueba").ingrediente("Sal", 200).crear();
		assertEquals("Receta Prueba", nuevaReceta.getNombre());
		assertEquals(200, nuevaReceta.getIngredientesCon("Sal").get(0).getCantidad(), 0);
	}

}
