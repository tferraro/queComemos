package persistencia.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import businessModel.usuario.Grupo;
import businessModel.usuario.Usuario;
import exceptions.model.usuario.ErrorRepoUsuario;
import persistencia.GestorCuentas;

public enum GestorCuentasMock implements GestorCuentas {
	INSTANCIA;

	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private List<Grupo> grupos = new ArrayList<Grupo>();

	@Override
	public void guardar(Usuario user) {
		chequearSiElUsuarioPuedeAgregarse(user);
		this.usuarios.add(user);
	}

	public void remove(Usuario user) {
		if (!usuarios.remove(user))
			throw new ErrorRepoUsuario("remove: No existe Usuario a eliminar");
	}

	public void update(Usuario user) {
		Usuario buscado;
		try {
			buscado = obtener(user);
		} catch (ErrorRepoUsuario e) {
			throw new ErrorRepoUsuario("update: " + e.getMessage());
		}
		buscado.reemplazar(user);
	}

	@Override
	public Usuario obtener(Usuario user) {
		List<Usuario> consulta = usuarios.stream().filter(u -> u.mismoNombre(user)).collect(Collectors.toList());
		if (consulta.size() == 0)
			throw new ErrorRepoUsuario("getUsuario: No existe Usuario con dicho Nombre");
		if (consulta.size() != 1)
			throw new ErrorRepoUsuario("getUsuario: Existe más de un Usuario con dicho Nombre");
		return consulta.get(0);
	}

	public List<Usuario> list(Usuario user) {
		return usuarios.stream().filter(u -> u.nombreContiene(user)).filter(u -> u.poseeCondiciones(user))
				.collect(Collectors.toList());
	}

	private void chequearSiElUsuarioPuedeAgregarse(Usuario user) {
		user.esValido();
		if (usuarios.stream().anyMatch(u -> u.mismoNombre(user)))
			throw new ErrorRepoUsuario("add: Usuario de nombre ya existente");
	}

	public void removeAll() {
		usuarios.clear();
	}

	@Override
	public Grupo obtener(Grupo grupo) {
		return grupos.stream().anyMatch(g -> g.mismoNombre(grupo))
				? grupos.stream().filter(g -> g.mismoNombre(grupo)).collect(Collectors.toList()).get(0) : null;
	}

	@Override
	public void guardar(Grupo grupo) {
		if (grupos.contains(grupo))
			grupos = grupos.stream().filter(user -> !user.mismoNombre(grupo)).collect(Collectors.toList());
		grupos.add(grupo);
	}

	@Override
	public List<Usuario> obtenerTodosUsuarios() {
		return usuarios;
	}

	@Override
	public void remover(Usuario usuario) {
		usuarios.remove(usuario);
	}

	@Override
	public void remover(Grupo grupo) {
		grupos.remove(grupo);
	}
}
