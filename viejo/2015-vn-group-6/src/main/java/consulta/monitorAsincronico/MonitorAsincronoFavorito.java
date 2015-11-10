package consulta.monitorAsincronico;

public class MonitorAsincronoFavorito implements MonitorAsincrono {

	@Override
	public void ejecutar(ConsultaAsincronica consulta) {
		if (consulta.opcionUsuarioConsultas()) {
			consulta.getLista().stream().filter(r -> !consulta.recetaFavoritaDeUsuario(r))
					.forEach(r -> consulta.agregarNuevaFavorita(r));
		}
	}
}
