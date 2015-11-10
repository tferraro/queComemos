package consulta.monitorAsincronico;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import persistencia.hibernate.PersistentEntity;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

@Entity
@Table(name = "Consultas")
public class ConsultaAsincronica extends PersistentEntity {

	private String parametros;
	@ManyToMany
	private List<Receta> lista;
	@ManyToOne
	private Usuario user;
	private LocalDateTime fecha;

	public ConsultaAsincronica(String parametros, List<Receta> lista, Usuario user, LocalDateTime fecha) {
		this.setParametros(parametros);
		this.setLista(lista);
		this.setUser(user);
		this.setFecha(fecha);
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public List<Receta> getLista() {
		return lista;
	}

	public void setLista(List<Receta> lista) {
		this.lista = lista;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public boolean opcionUsuarioConsultas() {
		return this.getUser().getOpcionConsultasFavoritas();
	}

	public boolean recetaFavoritaDeUsuario(Receta r) {
		return this.getUser().esRecetaHistorica(r);
	}

	public void agregarNuevaFavorita(Receta r) {
		this.getUser().agregarFavorito(r);
	}
}
