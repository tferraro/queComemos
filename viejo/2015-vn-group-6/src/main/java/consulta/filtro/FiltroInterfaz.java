package consulta.filtro;

import java.util.List;
import java.util.stream.Collectors;

import businessModel.receta.DificultadDePreparacionReceta;
import businessModel.receta.Receta;
import businessModel.temporada.Temporada;
import businessModel.usuario.Usuario;
import externalEntities.recetas.RepositorioExterno;

public class FiltroInterfaz extends FiltroDecorador {

	private Filtro anterior;
	private String nombre;
	private DificultadDePreparacionReceta dificultad;
	private Temporada temporada;
	private Integer minCal;
	private Integer maxCal;

	public FiltroInterfaz(Filtro anterior, String nombre,
			DificultadDePreparacionReceta dificultad, Temporada temporada,
			Integer minCal, Integer maxCal) {
		super(anterior);
		this.anterior = anterior;
		this.nombre = nombre;
		this.dificultad = dificultad;
		this.temporada = temporada;
		this.minCal = minCal;
		this.maxCal = maxCal;
		this.parametro = "Filtro de Interfaz";
	}

	@Override
	public void agregarFuenteExterna(RepositorioExterno repo, Receta receta) {
		filtroAnterior.agregarFuenteExterna(repo, receta);
	}

	@Override
	public List<Receta> consultar(Usuario user) {
		return filtrarPorCalorias(filtrarPorTemporada(filtrarPorDificultad(filtrarPorNombre(anterior
				.consultar(user)))));
	}

	private List<Receta> filtrarPorCalorias(List<Receta> consulta) {
		return consulta
				.stream()
				.filter(r -> caloriasEstaEnElIntervalo(r.calcularCalorias()
						.intValueExact()))
				.collect(Collectors.toList());
	}

	private Boolean caloriasEstaEnElIntervalo(Integer calorias) {
		if (minCal != 0 && maxCal == 0 && minCal <= maxCal)
			return minCal <= calorias;
		if (minCal == 0 && maxCal != 0 && minCal <= maxCal)
			return maxCal >= calorias;
		if (minCal != 0 && maxCal != 0 && minCal <= maxCal)
			return minCal <= calorias && maxCal >= calorias;
		return true;
	}

	private List<Receta> filtrarPorTemporada(List<Receta> consulta) {
		if (temporada != null)
			return consulta
					.stream()
					.filter(r -> r.getTemporada().getNombre()
							.equals(temporada.getNombre()))
					.collect(Collectors.toList());
		return consulta;
	}

	private List<Receta> filtrarPorDificultad(List<Receta> consulta) {
		if (dificultad != DificultadDePreparacionReceta.NA)
			return consulta.stream()
					.filter(r -> r.getDificultad().equals(dificultad))
					.collect(Collectors.toList());
		return consulta;
	}

	private List<Receta> filtrarPorNombre(List<Receta> consulta) {
		if (nombre != null)
			return consulta
					.stream()
					.filter(r -> r.nombreContiene(new Receta()
							.setNombre(nombre))).collect(Collectors.toList());
		return consulta;
	}

}
