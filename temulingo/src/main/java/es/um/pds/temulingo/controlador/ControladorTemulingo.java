package es.um.pds.temulingo.controlador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.logic.CargadorCursos;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.Progreso;
import es.um.pds.temulingo.logic.RepositorioCursos;
import es.um.pds.temulingo.logic.Usuario;

public class ControladorTemulingo {

	private static ControladorTemulingo instance = null;

	private Usuario usuarioActual;

	private RepositorioCursos repoCursos;

	private Progreso cursoActual;

	// Si se opta por implementar usuarios, esta lista
	// debería ser una propiedad de la clase Usuario
	private List<Progreso> progresos = new ArrayList<>();

	private FactoriaDao factoriaDao;
	private Dao<Usuario> usuarioDao;
	private Dao<Progreso> progresoDao;

	private Map<Long, Usuario> usuarios = new HashMap<>();

	private ControladorTemulingo() {
		inicializarAdaptadores();

		this.repoCursos = RepositorioCursos.getInstance();
		this.progresos = progresoDao.getAll();

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
		this.usuarios = usuarios;
	}

	private void inicializarAdaptadores() {
		factoriaDao = FactoriaDao.getDaoFactory();

		usuarioDao = factoriaDao.getUsuarioDao();
		progresoDao = factoriaDao.getProgresoDao();
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

	public Progreso getCursoActual() {
		return cursoActual;
	}

	public void setCursoActual(Progreso cursoActual) {
		this.cursoActual = cursoActual;
	}

	public void iniciarCurso(Curso curso) {
		Progreso cursoNuevo = new Progreso(curso);

		progresoDao.save(cursoNuevo);

		progresos.add(cursoNuevo);
		setCursoActual(cursoNuevo);
	}

	public Pregunta getSiguientePregunta() {
		return cursoActual.getSiguientePregunta();
	}

	public boolean resolverPregunta(Pregunta pregunta, String respuesta) {
		return cursoActual.resolverPregunta(pregunta, respuesta);
	}

	public boolean esCursoActualCompletado() {
		return getSiguientePregunta() == null;
	}

	public void iniciarCursoTest() {
		iniciarCurso(repoCursos.obtenerTodosLosCursos().get(0));
	}

}
