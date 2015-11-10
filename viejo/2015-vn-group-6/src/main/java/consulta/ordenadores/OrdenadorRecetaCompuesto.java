package consulta.ordenadores;

public abstract class OrdenadorRecetaCompuesto implements OrdenadorReceta {

	final OrdenadorReceta criterioAnterior;

	public OrdenadorRecetaCompuesto(OrdenadorReceta anterior) {
		criterioAnterior = anterior;
	}
}
