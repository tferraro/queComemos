package consulta.filtro;

import java.util.List;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

public abstract class FiltroDecorador implements Filtro {

	protected Filtro filtroAnterior;
	protected String parametro;

	public FiltroDecorador(Filtro anterior) {
		filtroAnterior = anterior;
	}

	public abstract List<Receta> consultar(Usuario user);

	@Override
	public String getParametros() {
		return this.parametro.concat(": " + filtroAnterior.getParametros());
	}
	
	@Override
	public String getNombre() {
		return parametro;
	}
}