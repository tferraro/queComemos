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
		tom.setPeso(82.0);
		tom.setRutina(new Activa(Rutina.Level.LEVE));
		tom.agregarCondicion(new Diabetico());
		tom.setSexo('M');	
		tom.agregarPreferencia("Rabas");
		return tom;
	}
	//Punto1
	@Test
	public void creacionUsuarioValido() throws ParseException {
		Usuario usr = tomas();
		assertTrue(usr.esValido());
	}
	@Test
	public void creacionUsuarioInvalido() throws ParseException {
		Usuario usr = tomas();
		usr.setSexo('\0'); //Le sacamos el valor del sexo para que no sea valido
		assertFalse(usr.esValido());
	}
	//Punto2
	@Test
	public void chequearIMC() throws ParseException {
		Usuario user = this.tomas();
		assertTrue(user.IMC() == 23.70);
	}
	@Test
	public void chequearRutinaSaludable() throws ParseException {
		Usuario user = this.tomas();
		assertTrue(user.sigueRutinaSaludable());
	}
	

}
