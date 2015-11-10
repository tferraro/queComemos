package entrega4;

import static org.junit.Assert.*;

import org.junit.Test;

import consulta.filtro.Filtro;
import consulta.filtro.FiltroConsulta;
import consulta.filtro.FiltroPorCalorias;
import consulta.filtro.FiltroPorCondicionesPrexistentes;
import persistencia.mock.RecetarioMock;

public class TestSobreParametrosDeFiltros {

	@Test
	public void validarBusquedaSinParametros() {
		Filtro filtro = new FiltroConsulta(RecetarioMock.INSTANCIA);
		assertEquals("", filtro.getParametros());
	}

	@Test
	public void validarBusquedaConParametroCalorias() {
		Filtro filtro = new FiltroPorCalorias(new FiltroConsulta(
				RecetarioMock.INSTANCIA));
		assertEquals("Filtro Por Calorias: ", filtro.getParametros());
	}

	@Test
	public void validarBusquedaConParametroCaloriasYCondiciones() {
		Filtro filtro = new FiltroPorCondicionesPrexistentes(
				new FiltroPorCalorias(new FiltroConsulta(
						RecetarioMock.INSTANCIA)));
		assertEquals(
				"Filtro Por Condiciones Prexistentes: Filtro Por Calorias: ",
				filtro.getParametros());
	}
}
