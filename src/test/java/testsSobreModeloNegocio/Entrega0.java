package testsSobreModeloNegocio;

import static org.junit.Assert.*;

import org.junit.Test;

import modeloNegocio.*;

public class Entrega0 {

	@Test
	public void testIMCtomas() {
		
		Usuario user = new Usuario();
		user.setPeso(82.0); //Cualquier parecido a la realidad...es pura coincidencia (?)
		user.setAltura(1.86);
		assertTrue(user.IMC() == 23.70);
	}

}