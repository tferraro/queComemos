package businessModel.usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import persistencia.hibernate.PersistentEntity;
import exceptions.model.receta.ErrorRecetaNoSaludable;
import exceptions.model.usuario.ErrorAlSugerirUnaReceta;
import businessModel.receta.Receta;

@Entity
@Table(name = "Grupos")
public class Grupo extends PersistentEntity implements Recetable {

	@Column(name = "nombre", unique = true)
	private String nombre;
	@Transient
	private List<String> intereses = new ArrayList<String>();
	private String interesesJuntos = "";
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<Usuario> integrantes = new ArrayList<Usuario>();

	@Override
	public void sugerir(Receta receta) {
		recetaEsDeInteresParaGrupo(receta);
		esAdecuadaParaLosUsuarios(receta);
	}

	private void esAdecuadaParaLosUsuarios(Receta receta) {
		try {
			this.getIntegrantes().stream()
					.forEach(integrante -> integrante.meEsAdecuada(receta));
		} catch (ErrorRecetaNoSaludable exep) {
			throw new ErrorAlSugerirUnaReceta(
					"Condicion Preexistente no cumplida: " + exep.getMessage()
							+ "para un usuario.");
		}
	}

	private void recetaEsDeInteresParaGrupo(Receta receta) {
		if (this.getIntereses().stream()
				.noneMatch(interes -> receta.nombreMatcheaConInteres(interes)))
			throw new ErrorAlSugerirUnaReceta(
					"La receta no es de interes para el grupo");
	}

	public List<Receta> verRecetasUsuarios(Usuario user) {
		List<Receta> aux = new ArrayList<>();
		this.getIntegrantes().forEach(usr -> aux.addAll(usr.verRecetas(user)));
		return aux;
	}

	// Getters & Setters
	public List<String> getIntereses() {
		return intereses;
	}

	public void setIntereses(List<String> intereses) {
		this.intereses = intereses;
	}

	public List<Usuario> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(List<Usuario> integrantes) {
		this.integrantes = integrantes;
	}

	public String getNombre() {
		return nombre;
	}

	public Grupo setNombre(String nombre) {
		this.nombre = nombre;
		return this;
	}

	public void agregarIntegrante(Usuario usr) {
		integrantes.add(usr);
		usr.agregarGrupo(this);
	}

	public void eliminarIntegrante(Usuario usr) {
		integrantes.remove(usr);
		usr.removerGrupo(this);
	}

	public void agregarInteres(String str) {
		intereses.add(str);
	}

	public void eliminarInteres(String str) {
		intereses.remove(str);
	}

	public Boolean mismoNombre(Grupo grupo) {
		return this.nombre.equals(grupo.nombre);
	}

	@PrePersist
	private void antesDePersistir() {
		if (!this.getIntereses().isEmpty())
			this.interesesJuntos = contatenarStrings(this.getIntereses());
	}

	@PostConstruct
	private void despuesDeConstruir() {
		desContatenarStrings(this.interesesJuntos).forEach(
				comida -> this.agregarInteres(comida));
		this.getIntegrantes().forEach(
				integrante -> integrante.agregarGrupo(this));
	}

	private String contatenarStrings(List<String> lista) {
		String base = "";
		for (String comida : lista) {
			base = base.concat(comida).concat(";_;");
		}
		return base;
	}

	private List<String> desContatenarStrings(String lista) {
		return Arrays.asList(lista.split(";_;"));
	}
}
