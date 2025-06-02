package es.um.pds.temulingo.controlador;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.logic.CargadorCursos;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.RepositorioCursos;
import es.um.pds.temulingo.logic.Usuario;

public class ControladorTemulingo {

	private static ControladorTemulingo instance = null;

	private Usuario usuarioActual;

	private RepositorioCursos repoCursos;

	private FactoriaDao factoriaDao;
	private Dao<Usuario> usuarioDao;

	private HashMap<Long, Usuario> usuarios;

	private ControladorTemulingo() {
		inicializarAdaptadores();

		this.repoCursos = RepositorioCursos.getInstance();

		cargarUsuarios();

		this.usuarioActual = null;
	}

	public static ControladorTemulingo getInstance() {
		if (instance == null) {
			instance = new ControladorTemulingo();
		}

		return instance;
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public void setUsuarioActual(Usuario usuarioActual) {
		this.usuarioActual = usuarioActual;
	}

	public Map<Long, Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Map<Long, Usuario> usuarios) {
		this.usuarios = (HashMap<Long, Usuario>) usuarios;
	}

	private void inicializarAdaptadores() {
		factoriaDao = FactoriaDao.getDaoFactory();

		usuarioDao = factoriaDao.getUsuarioDao();
	}

	private void cargarUsuarios() {
		List<Usuario> usuariosBD = usuarioDao.getAll();

		this.usuarios = new HashMap<>();

		for (Usuario usuario : usuariosBD) {
			this.usuarios.put(usuario.getId(), usuario);
		}
	}

	public void iniciarSesion(String nombre, String email) {

	}

	public void guardarCurso(Curso curso) {
		if (curso != null) {
			repoCursos.guardarCurso(curso);
		}
	}

	public void importarCursoDesdeFichero(File fichero) throws IOException {
		Curso curso = CargadorCursos.parsearDesdeFichero(fichero, Curso.class, CargadorCursos.Formato.YAML);

		repoCursos.guardarCurso(curso);
	}

	public List<Curso> getAllCursos() {
		return repoCursos.obtenerTodosLosCursos();
	}

}
