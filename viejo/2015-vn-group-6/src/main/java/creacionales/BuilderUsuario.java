package creacionales;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import businessModel.condicionMedica.CondicionMedica;
import businessModel.receta.Receta;
import businessModel.rutina.Rutina;
import businessModel.usuario.Genero;
import businessModel.usuario.Usuario;
import persistencia.GestorCuentas;

public class BuilderUsuario {

	private String nombre = null;
	private Genero sexo = Genero.NA;
	private LocalDate fechaNacimiento = null;
	private BigDecimal peso = new BigDecimal(0);
	private BigDecimal altura = new BigDecimal(0);
	private Rutina rutina;
	private Boolean consultasFav = false;
	private List<String> comidasQueGustan = new ArrayList<String>();
	private List<String> comidasQueNoGustan = new ArrayList<String>();
	private List<CondicionMedica> condicionesMedicas = new ArrayList<CondicionMedica>();
	private List<Receta> historialRecetas = new ArrayList<Receta>();
	private String email = null;
	private String password = null;
	private GestorCuentas repo = null;

	public BuilderUsuario agregarCamposObligatorios(String unNombre,
			BigDecimal peso, BigDecimal altura, LocalDate unaFecha,
			Rutina unaRutina) {
		this.setNombre(unNombre);
		this.setPeso(peso);
		this.setAltura(altura);
		this.setFechaNacimiento(unaFecha);
		this.setRutina(unaRutina);
		return this;
	}

	public BuilderUsuario agregarSexo(Genero genero) {
		this.setSexo(genero);
		return this;
	}

	public BuilderUsuario agregarCondicionMedica(CondicionMedica unaCondicion) {
		this.getCondicionesMedicas().add(unaCondicion);
		return this;
	}

	public BuilderUsuario agregarPreferencias(String comida) {
		this.getComidasQueGustan().add(comida);
		return this;
	}

	public BuilderUsuario agregarDisgustos(String comida) {
		this.getComidasQueNoGustan().add(comida);
		return this;
	}

	public Usuario compilar() {
		Usuario user = new Usuario();
		user.setNombre(this.getNombre());
		user.setPeso(this.getPeso());
		user.setAltura(this.getAltura());
		user.setFechaNacimiento(this.getFechaNacimiento());
		user.setRutina(this.getRutina());
		user.setSexo(this.getSexo());
		user.setOpcionConsultasFavoritas(this.getConsultasFav());
		user.setMail(email);
		this.getComidasQueGustan().forEach(
				comida -> user.agregarComidasQueGustan(comida));
		this.getComidasQueNoGustan().forEach(
				comida -> user.agregarComidaQueNoGustan(comida));
		this.getCondicionesMedicas().forEach(
				condicion -> user.agregarCondicionMedica(condicion));
		this.getHistorialRecetas().forEach(
				receta -> user.agregarFavorito(receta));
		user.setPassword(password);
		user.esValido();
		if (repo != null)
			repo.guardar(user);
		return user;
	}

	public BuilderUsuario selecionarOpcionConsultasFavoritas() {
		this.setConsultasFav(true);
		return this;
	}

	public BuilderUsuario agregarMail(String mail) {
		this.email = mail;
		return this;
	}

	public Rutina getRutina() {
		return rutina;
	}

	public BuilderUsuario setRutina(Rutina rutina) {
		this.rutina = rutina;
		return this;
	}

	public String getNombre() {
		return nombre;
	}

	public BuilderUsuario setNombre(String nombre) {
		this.nombre = nombre;
		return this;
	}

	public Genero getSexo() {
		return sexo;
	}

	public BuilderUsuario setSexo(Genero sexo) {
		this.sexo = sexo;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public BuilderUsuario setPassword(String password) {
		this.password = password;
		return this;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public BuilderUsuario setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		return this;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public BuilderUsuario setPeso(BigDecimal peso) {
		this.peso = peso;
		return this;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public BuilderUsuario setAltura(BigDecimal altura) {
		this.altura = altura;
		return this;
	}

	public List<String> getComidasQueGustan() {
		return comidasQueGustan;
	}

	public BuilderUsuario setComidasQueGustan(List<String> comidasQueGustan) {
		this.comidasQueGustan = comidasQueGustan;
		return this;
	}

	public List<String> getComidasQueNoGustan() {
		return comidasQueNoGustan;
	}

	public BuilderUsuario setComidasQueNoGustan(List<String> comidasQueNoGustan) {
		this.comidasQueNoGustan = comidasQueNoGustan;
		return this;
	}

	public List<CondicionMedica> getCondicionesMedicas() {
		return condicionesMedicas;
	}

	public BuilderUsuario setCondicionesMedicas(
			List<CondicionMedica> condicionesMedicas) {
		this.condicionesMedicas = condicionesMedicas;
		return this;
	}

	public List<Receta> getHistorialRecetas() {
		return historialRecetas;
	}

	public BuilderUsuario agregarFavorito(Receta receta) {
		this.historialRecetas.add(receta);
		return this;
	}

	public BuilderUsuario removerFavorito(Receta receta) {
		this.historialRecetas.remove(receta);
		return this;
	}

	public void limpiarHistorialRecetas() {
		this.historialRecetas.clear();
	}

	public void reset() {
		nombre = null;
		sexo = Genero.NA;
		fechaNacimiento = null;
		peso = new BigDecimal(0);
		altura = new BigDecimal(0);
		rutina = null;
		consultasFav = false;
		email = null;
		password = null;
		comidasQueGustan.clear();
		comidasQueNoGustan.clear();
		condicionesMedicas.clear();
		historialRecetas.clear();

	}

	public Boolean getConsultasFav() {
		return consultasFav;
	}

	public void setConsultasFav(Boolean fav) {
		this.consultasFav = fav;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BuilderUsuario setRepositorio(GestorCuentas repo) {
		this.repo = repo;
		return this;
	}

}