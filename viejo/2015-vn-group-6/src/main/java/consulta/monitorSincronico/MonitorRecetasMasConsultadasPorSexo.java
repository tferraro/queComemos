package consulta.monitorSincronico;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import businessModel.receta.Receta;
import businessModel.usuario.Genero;
import businessModel.usuario.Usuario;

@Entity
@DiscriminatorValue("S")
public class MonitorRecetasMasConsultadasPorSexo extends MonitorSincronico {

	@Transient
	private static MonitorRecetasMasConsultadasPorSexo instancia = new MonitorRecetasMasConsultadasPorSexo();

	public static MonitorRecetasMasConsultadasPorSexo INSTANCIA() {
		return instancia;
	}

	private MonitorRecetasMasConsultadasPorSexo() {

	}

	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<RecetaConsultada> listaFem = new ArrayList<RecetaConsultada>();
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<RecetaConsultada> listaMas = new ArrayList<RecetaConsultada>();

	@Override
	public void avisar(List<Receta> lista, Usuario user) {
		lista.forEach(rec -> modRecetasPorSexo(rec, user));
	}

	public RecetaConsultada recetaMasConsultadaPorGenero(Genero sexo) {
		return RecetaConsultada.recetaMasConsultada(getListaGenero(sexo));
	}

	private void modRecetasPorSexo(Receta receta, Usuario user) {
		RecetaConsultada.agregarOSumarReceta(getListaGenero(user.getSexo()),
				receta.getNombre(), user.getNombre());
	}

	@Override
	public void reiniciarEstadistica() {
		listaFem.clear();
		listaMas.clear();
	}

	private List<RecetaConsultada> getListaGenero(Genero genero) {
		switch (genero) {
		case MASCULINO:
			return listaMas;
		case FEMENINO:
			return listaFem;
		default:
			return null;
		}
	}

}
