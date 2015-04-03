package testsSobreModeloNegocio;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import modeloNegocio.*;

import org.junit.Test;

public class Entrega1 {

	public Usuario tomas() throws ParseException {
		Usuario tom = new Usuario();
		tom.setAltura(1.86);
		tom.setFechaNacimiento(Calendar.getInstance());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		tom.getFechaNacimiento().setTime(sdf.parse("03/06/1993"));
		tom.setNombre("Tomas");
		tom.setPeso(83.0);
		tom.setRutina(new Sedentaria(Rutina.Level.NADA));
		tom.agregarCondicion(new Diabetico());
		tom.setSexo('M');	
		tom.agregarPreferencia("Rabas");
		return tom;
	}
	
	@Test
	public void test() throws ParseException {
		Usuario usr = tomas();
		assertTrue(usr.esValido());
	}

}
