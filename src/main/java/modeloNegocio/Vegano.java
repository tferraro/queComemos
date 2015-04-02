package modeloNegocio;

import java.util.ArrayList;
import java.util.List;

public class Vegano implements Condicion {

	private List<String> preferenciasProhibidas = new ArrayList<String>();
	public Vegano() {
		preferenciasProhibidas.add("Pollo");
		preferenciasProhibidas.add("Carne");
		preferenciasProhibidas.add("Chivito");
		preferenciasProhibidas.add("Chori");
		//Se toma como condicion que las preferencias comiensen con mayusculas
	}
	@Override
	public boolean estadoValido(Usuario usr) {
		for(String preferencia : usr.getPreferencias()) 
			for(String prefProhibida : preferenciasProhibidas)
				if(prefProhibida.matches(preferencia))
						return false;
		return true;
	}
	@Override
	public boolean estaSubsanada(Usuario usr) {
		for(String pref : usr.getPreferencias())
			if(pref.matches("Frutas"))
				return true;
		return false;
	}

}
