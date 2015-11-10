package persistencia.hibernate;

import java.util.List;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import exceptions.model.usuario.ErrorRepoUsuario;
import persistencia.GestorCuentas;
import businessModel.usuario.Grupo;
import businessModel.usuario.Usuario;

public class GestorCuentasHibernate implements GestorCuentas, WithGlobalEntityManager {

	String query = "SELECT u FROM Usuario u WHERE pendiente = false";

	@Override
	public void guardar(Usuario usuario) {
		entityManager().persist(usuario);
	}

	@Override
	public void guardar(Grupo grupo) {
		entityManager().persist(grupo);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> obtenerTodosUsuarios() {
		return (List<Usuario>) entityManager().createQuery(query).getResultList();
	}

	@Override
	public Usuario obtener(Usuario usuario) {
		String query_name = query + " AND nombre ='" + usuario.getNombre() + "'";
		@SuppressWarnings("unchecked")
		List<Usuario> result = (List<Usuario>) entityManager().createQuery(query_name).getResultList();
		if (result.size() == 0)
			throw new ErrorRepoUsuario("getUsuario: No existe Usuario con dicho Nombre");
		if (result.size() != 1)
			throw new ErrorRepoUsuario("getUsuario: Existe más de un Usuario con dicho Nombre");
		return result.get(0);
	}

	@Override
	public Grupo obtener(Grupo grupo) {
		String query_name = "SELECT u FROM Grupo u WHERE nombre ='" + grupo.getNombre() + "'";
		@SuppressWarnings("unchecked")
		List<Grupo> result = (List<Grupo>) entityManager().createQuery(query_name).getResultList();
		if (result.size() == 0)
			throw new ErrorRepoUsuario("getGrupo: No existe Grupo con dicho Nombre");
		if (result.size() != 1)
			throw new ErrorRepoUsuario("getGrupo: Existe más de un Grupo con dicho Nombre");
		return result.get(0);
	}
	
	@Override
	public void remover(Usuario usuario) {
		entityManager().remove(usuario);
	}

	@Override
	public void remover(Grupo grupo) {
		entityManager().remove(grupo);
	}
}
