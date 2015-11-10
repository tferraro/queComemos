package persistencia;

import java.util.List;
import exceptions.model.usuario.ErrorRepoUsuario;
import businessModel.usuario.Grupo;
import businessModel.usuario.Usuario;

public interface GestorCuentas {

	void guardar(Usuario usuario);

	Usuario obtener(Usuario usuario);

	Grupo obtener(Grupo grupo);

	void guardar(Grupo grupo);

	List<Usuario> obtenerTodosUsuarios();
	
	void remover(Usuario usuario);

	void remover(Grupo grupo);

	default Usuario obtenerUsuarioSiEsValido(String nombre, String contrasenia) {
		Usuario user = new Usuario();
		user.setNombre(nombre);
		try {
			user = obtener(user);
			if(user.mismoPassword(contrasenia))
				return user;
		} catch (ErrorRepoUsuario e) {
		}
		return null;
	}
}
