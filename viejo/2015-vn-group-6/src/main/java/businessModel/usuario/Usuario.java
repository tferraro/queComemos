package businessModel.usuario;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import persistencia.hibernate.PersistentEntity;
import exceptions.model.receta.*;
import exceptions.model.usuario.*;
import businessModel.condicionMedica.CondicionMedica;
import businessModel.condicionMedica.Vegano;
import businessModel.receta.Receta;
import businessModel.rutina.Rutina;

@Entity
@Table(name = "Usuarios")
public class Usuario extends PersistentEntity implements Recetable {

	@Column(name = "nombre", unique = true)
	private String nombre = null;
	private Genero sexo = Genero.NA;
	private LocalDate fechaNacimiento = null;
	private BigDecimal peso = new BigDecimal(0);
	private BigDecimal altura = new BigDecimal(0);
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Rutina rutina;

	@Transient
	private List<String> comidasQueGustan = new ArrayList<String>();
	private String comidasQueGustanJuntas = "";
	@Transient
	private List<String> comidasQueNoGustan = new ArrayList<String>();
	private String comidasQueNoGustanJuntas = "";
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<CondicionMedica> condicionesMedicas = new ArrayList<CondicionMedica>();
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "usuarioId")
	private List<Receta> recetasPrivadas = new ArrayList<Receta>();
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "Favoritas")
	private List<Receta> historialRecetas = new ArrayList<Receta>();
	@Transient
	private List<Grupo> grupos = new ArrayList<>();
	private Boolean opcionConsultasFavoritas;
	@Transient
	private InternetAddress mail = new InternetAddress();
	private String dirMail = this.getMail();
	@SuppressWarnings("unused")
	private Boolean pendiente = false;
	private String contrasenia;

	// Métodos publicos
	public Double indiceDeMasaCorporal() {
		return peso.divide(altura.pow(2), 2, RoundingMode.HALF_UP)
				.doubleValue();
	}

	public void esValido() {
		faltanCamposValidez();
		faltaLongitudNombre();
		estadoValidoCondicionesMedicas();
		fechaNacimientoValida();
		emailValido();
	}

	private void emailValido() {
		if (mail.getAddress() != null) {
			try {
				this.mail.validate();
			} catch (AddressException e) {
				throw new ErrorValidezUsuario("Email no válido: "
						+ e.getMessage());
			}
		}
	}

	private void fechaNacimientoValida() {
		if (!this.getFechaNacimiento().isBefore(LocalDate.now()))
			throw new ErrorValidezUsuario(
					"La fecha de nacimiento debe ser anterior a la fecha de hoy ");
	}

	private void estadoValidoCondicionesMedicas() {
		this.condicionesMedicas.forEach(cond -> cond.estadoValido(this));
	}

	private void faltaLongitudNombre() {
		if (this.nombre.length() <= 4)
			throw new ErrorValidezUsuario(
					"El nombre debe tener mas de 4 caracteres");
	}

	private void faltanCamposValidez() {
		if (this.nombre == null)
			throw new ErrorValidezUsuario("No indico nombre valido");
		if (this.peso.equals(new BigDecimal(0)))
			throw new ErrorValidezUsuario("No indico peso valido");
		if (this.altura.equals(new BigDecimal(0)))
			throw new ErrorValidezUsuario("No indico altura valida");
		if (this.fechaNacimiento == null)
			throw new ErrorValidezUsuario("No indico fecha valida");
		if (this.rutina == null)
			throw new ErrorValidezUsuario("No indico rutina valida");
	}

	public void sigueRutinaSaludable() {
		cumpleRangoIndiceMasaCorporal();
		condicionesMedicasSubsanadas();
	}

	private void cumpleRangoIndiceMasaCorporal() {
		if (this.indiceDeMasaCorporal() < 18)
			throw new ErrorUsuarioNoSigueRutinaSaludable(
					"Indice de Masa Corporal por debajo de 18");
		if (this.indiceDeMasaCorporal() > 30)
			throw new ErrorUsuarioNoSigueRutinaSaludable(
					"Indice de Masa Corporal por debajo de 18");
	}

	private void condicionesMedicasSubsanadas() {
		getCondicionesMedicas().forEach(cond -> cond.estaSubsanada(this));
	}

	public List<Receta> verRecetas(Usuario user) {
		this.puedeVerRecetasPrivadas(user);
		return recetasPrivadas.stream().map(Receta::getClone)
				.collect(Collectors.toList());
	}

	public void modificarRecetaPublica(Receta receta) {
		receta.agregarModificacionPor(getNombre());
		this.agregarReceta(receta);
	}

	public void modificarReceta(Receta receta, Usuario user) {
		puedeVerRecetasPrivadas(user);
		existeRecetaEnListaPrivada(receta);
		reemplazarRecetaNuevaEnLista(receta);
	}

	private void reemplazarRecetaNuevaEnLista(Receta receta) {
		for (Receta rec : recetasPrivadas) {
			if (rec.getNombre().equals(receta.getNombre())) {
				recetasPrivadas.remove(rec);
				recetasPrivadas.add(receta);
				return;
			}
		}
		throw new ErrorAlModificarReceta(
				"La receta no se pudo modificar correctamente");
	}

	private void existeRecetaEnListaPrivada(Receta receta) {
		if (recetasPrivadas.stream().noneMatch(
				r -> r.getNombre().equals(receta.getNombre())))
			throw new ErrorNoSePuedeVerOModificarReceta(
					"La receta no se encuentra en la lista");
	}

	public void puedeVerRecetasPrivadas(Usuario user) {
		// Chequea que no tengan ningun grupo en comun
		// Si es que no tiene grupos el usuario, lanza la excepcion
		// Si es el mismo usuario sin grupos, el noneMatch devuelve true apesar
		// de ser el mismo usuario de ahi el chequeo extra
		if (this.getGrupos().stream()
				.noneMatch((g -> user.getGrupos().contains(g)))
				&& (!this.mismoNombre(user)))
			throw new ErrorNoSePuedeVerOModificarReceta(
					"Recetas No visibles para el usuario: " + user.getNombre());
	}

	public void meEsAdecuada(Receta receta) {
		getCondicionesMedicas().forEach(
				cond -> cond.meEsAdecuadaLaReceta(receta));
	}

	@Override
	public void sugerir(Receta receta) {
		noContieneIngredientesNoPreferentes(receta);
		esAdecuadaParaTodasLasCondiciones(receta);
		agregarReceta(receta);
	}

	private void esAdecuadaParaTodasLasCondiciones(Receta receta) {
		try {
			this.meEsAdecuada(receta);
		} catch (ErrorRecetaNoSaludable exep) {
			throw new ErrorAlSugerirUnaReceta(
					"Condicion Preexistente no cumplida: " + exep.getMessage());
		}
	}

	private void noContieneIngredientesNoPreferentes(Receta receta) {
		if (this.getComidaQueNoGustan().stream()
				.anyMatch(comida -> receta.contieneIngrediente(comida)))
			throw new ErrorAlSugerirUnaReceta(
					"La receta contiene ingredientes que disgustan");
	}

	public Boolean tieneSobrepeso() {
		return this.indiceDeMasaCorporal() > 25;
	}

	// Getters & Setters
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Genero getSexo() {
		return sexo;
	}

	public void setSexo(Genero sexo) {
		this.sexo = sexo;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public BigDecimal getAltura() {
		return altura.setScale(2, RoundingMode.CEILING);
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public Rutina getRutina() {
		return rutina;
	}

	public void setRutina(Rutina rutina) {
		this.rutina = rutina;
	}

	public void agregarComidasQueGustan(String pref) {
		comidasQueGustan.add(pref);
	}

	public void removerComidasQueGustan(String pref) {
		comidasQueGustan.remove(pref);
	}

	public List<String> getComidasQueGustan() {
		return comidasQueGustan;
	}

	public void agregarComidaQueNoGustan(String comida) {
		comidasQueNoGustan.add(comida);
	}

	public void removerComidaQueNoGustan(String comida) {
		comidasQueNoGustan.remove(comida);
	}

	public List<String> getComidaQueNoGustan() {
		return comidasQueNoGustan;
	}

	public void agregarCondicionMedica(CondicionMedica cond) {
		condicionesMedicas.add(cond);
	}

	public void removerCondicionMedica(CondicionMedica cond) {
		condicionesMedicas.remove(cond);
	}

	public List<CondicionMedica> getCondicionesMedicas() {
		return condicionesMedicas;
	}

	public List<Receta> getRecetasPrivadas() {
		return recetasPrivadas;
	}

	public void setRecetasPrivadas(List<Receta> recetasPrivadas) {
		this.recetasPrivadas = recetasPrivadas;
	}

	public Boolean getOpcionConsultasFavoritas() {
		return this.opcionConsultasFavoritas;
	}

	public void setOpcionConsultasFavoritas(Boolean consultasFav) {
		this.opcionConsultasFavoritas = consultasFav;
	}

	public String getMail() {
		return mail.getAddress();
	}

	public void setMail(String direccion) {
		this.mail.setAddress(direccion);
	}

	public void agregarReceta(Receta receta) {
		receta.esValida();
		recetasPrivadas.add(receta);
	}

	public void removerReceta(Receta receta) {
		recetasPrivadas.remove(receta);
	}

	public List<Receta> getHistorialRecetas() {
		return historialRecetas;
	}

	public void agregarFavorito(Receta receta) {
		this.historialRecetas.add(receta);
	}

	public void removerFavorito(Receta receta) {
		this.historialRecetas.remove(receta);
	}

	public void limpiarHistorialRecetas() {
		this.historialRecetas.clear();
	}

	public void setHistorialRecetas(List<Receta> historialRecetas) {
		this.historialRecetas = historialRecetas;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void agregarGrupo(Grupo grupo) {
		this.grupos.add(grupo);
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public void removerGrupo(Grupo grupo) {
		this.grupos.remove(grupo);
	}

	public void setPassword(String password) {
		this.contrasenia = password;
	}

	public Integer compararPeso(Integer numero) {
		return this.peso.compareTo(new BigDecimal(numero));
	}

	public Boolean leGustaComida(String pref) {
		return comidasQueGustan.contains(pref);
	}

	public Boolean esRecetaHistorica(Receta receta) {
		return getHistorialRecetas().stream().anyMatch(
				fav -> fav.mismoNombre(receta));
	}

	public Boolean nombreContiene(Usuario user) {
		return nombre.matches("(.*)" + user.getNombre() + "(.*)");
	}

	public Boolean mismoNombre(Usuario user) {
		return nombre.equals(user.getNombre());
	}

	public void estaPendiente() {
		pendiente = true;
	}

	public void confirmar() {
		pendiente = false;
	}

	public void reemplazar(Usuario user) {
		this.altura = user.altura;
		this.comidasQueGustan = user.comidasQueGustan;
		this.comidasQueNoGustan = user.comidasQueNoGustan;
		this.condicionesMedicas = user.condicionesMedicas;
		this.fechaNacimiento = user.fechaNacimiento;
		this.grupos = user.grupos;
		this.historialRecetas = user.historialRecetas;
		this.peso = user.peso;
		this.recetasPrivadas = user.recetasPrivadas;
		this.rutina = user.rutina;
		this.sexo = user.sexo;
	}

	public Boolean poseeCondiciones(Usuario user) {
		return user.condicionesMedicas.stream().allMatch(
				condDada -> this.condicionesMedicas.stream().anyMatch(
						condUsuario -> condUsuario.mismaCondicion(condDada)));
	}

	public Boolean sosVegano() {
		Vegano vega = new Vegano();
		return condicionesMedicas.stream()
				.anyMatch(c -> c.mismaCondicion(vega));
	}

	@PrePersist
	private void antesDePersistir() {
		if (!this.getComidasQueGustan().isEmpty())
			this.comidasQueGustanJuntas = contatenarStrings(this
					.getComidasQueGustan());
		if (!this.getComidaQueNoGustan().isEmpty())
			this.comidasQueNoGustanJuntas = contatenarStrings(this
					.getComidaQueNoGustan());
	}
	@PostLoad
	@PostConstruct
	private void despuesDeConstruir() {
		desContatenarStrings(this.comidasQueGustanJuntas).forEach(
				comida -> this.agregarComidasQueGustan(comida));
		desContatenarStrings(this.comidasQueNoGustanJuntas).forEach(
				comida -> this.agregarComidaQueNoGustan(comida));
		this.mail = new InternetAddress();
		this.setMail(this.dirMail);
	}

	private String contatenarStrings(List<String> lista) {
		String base = "";
		for(String comida : lista) {
			base = base.concat(comida).concat(";_;");
		}
		return base;
	}

	private List<String> desContatenarStrings(String lista) {
		return Arrays.asList(lista.split(";_;"));
	}

	public Boolean mismoPassword(String contrasenia) {
		return this.contrasenia.equals(contrasenia);
	}

}
