package entrega0;

import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigDecimal;

import businessModel.usuario.Usuario;

public class TestSobreIndiceMasaCorporal {

	@Test
	public void testIMCtomas() {
		Usuario tom = new Usuario();
		tom.setPeso(new BigDecimal(82));
		tom.setAltura(new BigDecimal(1.86));
		assertEquals(23.7, tom.indiceDeMasaCorporal(), 0);
	}

	@Test
	public void testIMCGuido() {
		Usuario guido = new Usuario();
		guido.setPeso(new BigDecimal(84));
		guido.setAltura(new BigDecimal(1.71));
		assertEquals(28.73, guido.indiceDeMasaCorporal(), 0);
	}

	@Test
	public void testIMCSeba() {
		Usuario seba = new Usuario();
		seba.setPeso(new BigDecimal(76));
		seba.setAltura(new BigDecimal(1.73));
		assertEquals(25.39, seba.indiceDeMasaCorporal(), 0);
	}

	@Test
	public void testIMCLeo() {
		Usuario leo = new Usuario();
		leo.setPeso(new BigDecimal(60));
		leo.setAltura(new BigDecimal(1.68));
		assertEquals(21.26, leo.indiceDeMasaCorporal(), 0);
	}

	@Test
	public void testIMCLean() {
		Usuario lean = new Usuario();
		lean.setPeso(new BigDecimal(82));
		lean.setAltura(new BigDecimal(1.86));
		assertEquals(23.70, lean.indiceDeMasaCorporal(), 0);
	}

}
