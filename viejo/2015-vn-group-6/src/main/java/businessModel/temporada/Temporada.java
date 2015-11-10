package businessModel.temporada;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import persistencia.hibernate.PersistentEntity;

@Entity
@Table(name = "Temporadas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TipoTemporada")
public abstract class Temporada extends PersistentEntity {

	public String nombre;
	public LocalDate fechaCelebracion;

	public void setNombre(String nom) {
		nombre = nom;
	}

	public String getNombre() {
		return nombre;
	}

	public void setFechaCelebracion(LocalDate fec) {
		fechaCelebracion = fec;
	}

	public LocalDate getFechaCelebracion() {
		return fechaCelebracion;
	}

	public static Temporada nombre(String nombre) {
		List<Temporada> temporadas = nombres()
				.stream()
				.filter(t -> t.getNombre().equals(nombre))
				.collect(Collectors.toList());
		return temporadas.isEmpty()? null : temporadas.get(0);
	}

	public static List<Temporada> nombres() {
		return Arrays.asList(
				new TodoElAnio(),
				new Invierno(),
				new Primavera(),
				new Verano(),
				new Otonio());
	}

}
