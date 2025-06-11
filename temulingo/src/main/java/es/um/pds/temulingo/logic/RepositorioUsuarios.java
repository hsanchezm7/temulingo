package es.um.pds.temulingo.logic;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;

/**
 * Repositorio para la entidad Usuario que sigue el patrón Singleton. Mantiene
 * una copia en memoria de los usuarios para un acceso rápido.
 */
public class RepositorioUsuarios {

	private final Map<Long, Usuario> usuarios = new HashMap<>();

	private final Map<String, Usuario> usuariosPorEmail = new HashMap<>();

	private final Map<String, Usuario> usuariosPorUsername = new HashMap<>();

	private static RepositorioUsuarios instancia;

	private final Dao<Usuario> usuarioDao;

	/**
	 * Constructor privado para garantizar una única instancia (Singleton).
	 */
	private RepositorioUsuarios() {
		this.usuarioDao = FactoriaDao.getDaoFactory().getUsuarioDao();
		inicializarRepositorio();
	}

	/**
	 * Devuelve la instancia única del repositorio.
	 *
	 * @return La instancia de {@code RepositorioUsuarios}.
	 */
	public static RepositorioUsuarios getInstance() {
		if (instancia == null) {
			instancia = new RepositorioUsuarios();
		}
		return instancia;
	}

	/**
	 * Carga los usuarios desde la base de datos y los almacena en los mapas
	 * locales.
	 */
	private void inicializarRepositorio() {
		usuarioDao.getAll().forEach(usuario -> {
			usuarios.put(usuario.getId(), usuario);
			usuariosPorEmail.put(usuario.getEmail(), usuario);
			usuariosPorUsername.put(usuario.getUsername(), usuario);
		});
	}

	/**
	 * Devuelve una lista con todos los usuarios almacenados en el repositorio.
	 *
	 * @return Una {@code List} de {@code Usuario}.
	 */
	public List<Usuario> obtenerTodosLosUsuarios() {
		return new LinkedList<>(usuarios.values());
	}

	/**
	 * Guarda un usuario en la base de datos y lo añade a la caché en memoria.
	 *
	 * @param usuario El {@code Usuario} a guardar.
	 */
	public void guardarUsuario(Usuario usuario) {
		usuarioDao.save(usuario);

		usuarios.put(usuario.getId(), usuario);
		usuariosPorEmail.put(usuario.getEmail(), usuario);
		usuariosPorUsername.put(usuario.getUsername(), usuario);
	}

	/**
	 * Actualiza un usuario existente.
	 *
	 * @param usuario El {@code Usuario} a actualizar.
	 */
	public void actualizarUsuario(Usuario usuario) {
		usuarioDao.edit(usuario);
	}

	/**
	 * Crea un nuevo usuario con los datos proporcionados y lo añade a la caché en
	 * memoria.
	 *
	 * @param email    El email del usuario.
	 * @param username El nombre de usuario.
	 * @param password La contraseña del usuario.
	 * @return
	 */
	public Usuario guardarUsuario(String nombre, String email, String username, String password, LocalDate fechaNacim) {
		Usuario usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setEmail(email);
		usuario.setUsername(username);
		usuario.setPassword(password);
		usuario.setFechaNacim(fechaNacim);

		guardarUsuario(usuario);

		return usuario;
	}

	/**
	 * Busca un usuario por su ID.
	 *
	 * @param id El ID del usuario a buscar.
	 * @return Un {@code Optional<Usuario>} que contendrá al usuario si se
	 *         encuentra.
	 */
	public Optional<Usuario> obtenerPorId(Long id) {
		return Optional.ofNullable(usuarios.get(id));
	}

	/**
	 * Busca un usuario por su email.
	 *
	 * @param email El email del usuario a buscar.
	 * @return Un {@code Optional<Usuario>} que contendrá al usuario si se
	 *         encuentra.
	 */
	public Optional<Usuario> obtenerPorEmail(String email) {
		return Optional.ofNullable(usuariosPorEmail.get(email));
	}

	/**
	 * Busca un usuario por su nombre de usuario (username).
	 *
	 * @param username El nombre de usuario a buscar.
	 * @return Un {@code Optional<Usuario>} que contendrá al usuario si se
	 *         encuentra.
	 */
	public Optional<Usuario> obtenerPorUsername(String username) {
		return usuarios.values().stream().filter(u -> u.getUsername().equals(username)).findFirst();
	}

	/**
	 * Elimina un usuario de la base de datos y de la caché.
	 *
	 * @param usuario El {@code Usuario} a eliminar.
	 */
	public void eliminarUsuario(Usuario usuario) {
		if (usuario != null && usuario.getId() != null) {
			usuarioDao.delete(usuario);
			usuarios.remove(usuario.getId());
			usuariosPorEmail.remove(usuario.getEmail());
			usuariosPorUsername.remove(usuario.getUsername());
		}
	}

}