package es.um.pds.temulingo.controlador;

import java.io.File;
import java.io.IOException;
import java.util.*;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.logic.*;

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
		this.usuarios = (HashMap<Long, Usuario>) usuarios;
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
		// Buscar si ya existe un progreso para este curso
		Progreso progresoExistente = buscarProgresoPorCurso(curso);

		if (progresoExistente != null) {
			// Si ya existe, usar el progreso existente
			setCursoActual(progresoExistente);
			System.out.println("Continuando curso existente: " + curso.getTitulo());
		} else {
			// Si no existe, crear uno nuevo
			Progreso cursoNuevo = new Progreso(curso);
			progresoDao.save(cursoNuevo);
			progresos.add(cursoNuevo);
			setCursoActual(cursoNuevo);
			System.out.println("Iniciando nuevo curso: " + curso.getTitulo());
		}
	}

	/**
	 * Busca un progreso existente para un curso específico.
	 * 
	 * @param curso El curso para el cual buscar el progreso
	 * @return El progreso encontrado, o null si no existe
	 */
	private Progreso buscarProgresoPorCurso(Curso curso) {
		return progresos.stream().filter(p -> p.getCurso().equals(curso)).findFirst().orElse(null);
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

	// ========================================
	// MÉTODOS DE DEBUG Y VALIDACIÓN
	// ========================================

	/**
	 * Establece la estrategia de aprendizaje para el curso actual. Reinicia el
	 * progreso del curso con la nueva estrategia.
	 * 
	 * @param estrategia La nueva estrategia de aprendizaje a aplicar
	 */
	public void setEstrategiaAprendizaje(Curso.EstrategiaAprendizaje estrategia) {
		if (cursoActual == null) {
			System.out.println("ERROR: No se puede establecer estrategia - no hay curso actual");
			return;
		}

		System.out.println("=== ESTABLECIENDO ESTRATEGIA ===");
		System.out.println("Estrategia: " + estrategia);

		// Actualizar la estrategia en el curso
		cursoActual.getCurso().setEstrategiaAprendizaje(estrategia);

		// Validar el estado del curso antes de continuar
		validarCursoActual();

		// Reiniciar el progreso con la nueva estrategia
		cursoActual.reiniciarConEstrategia(estrategia);

		// Persistir los cambios en la base de datos
		progresoDao.edit(cursoActual);

		// También actualizar el curso en la base de datos
		Dao<Curso> cursoDao = factoriaDao.getCursoDao();
		cursoDao.edit(cursoActual.getCurso());

		System.out.println("Estrategia de aprendizaje establecida: " + estrategia);
		System.out.println("===============================");
	}

	/**
	 * Obtiene la estrategia de aprendizaje actual del curso.
	 * 
	 * @return La estrategia de aprendizaje actual, o null si no hay curso activo
	 */
	public Curso.EstrategiaAprendizaje getEstrategiaAprendizajeActual() {
		if (cursoActual != null && cursoActual.getCurso() != null) {
			return cursoActual.getCurso().getEstrategiaAprendizaje();
		}
		return null;
	}

	/**
	 * Método de debug que valida y muestra información detallada del curso actual.
	 * Útil para depuración y verificación del estado del curso.
	 */
	public void validarCursoActual() {
		if (cursoActual == null) {
			System.out.println("ERROR: No hay curso actual");
			return;
		}

		Curso curso = cursoActual.getCurso();
		System.out.println("=== VALIDACIÓN CURSO ACTUAL ===");
		System.out.println("Título: " + curso.getTitulo());
		System.out.println("Bloques: " + (curso.getBloques() != null ? curso.getBloques().size() : 0));

		if (curso.getBloques() != null) {
			for (int i = 0; i < curso.getBloques().size(); i++) {
				Bloque bloque = curso.getBloques().get(i);
				System.out.println("Bloque " + i + ": " + bloque.getNombre());
				System.out
						.println("  Preguntas: " + (bloque.getPreguntas() != null ? bloque.getPreguntas().size() : 0));

				if (bloque.getPreguntas() != null) {
					validarPreguntasDelBloque(bloque.getPreguntas());
				}
			}
		}

		System.out.println("==============================");
	}

	/**
	 * Método auxiliar para validar las preguntas de un bloque específico.
	 * 
	 * @param preguntas Lista de preguntas a validar
	 */
	private void validarPreguntasDelBloque(List<Pregunta> preguntas) {
		for (int j = 0; j < preguntas.size(); j++) {
			Pregunta pregunta = preguntas.get(j);
			System.out.println("    Pregunta " + j + ": " + pregunta.getClass().getSimpleName());
			System.out.println("    Enunciado: " + pregunta.getEnunciado());
			System.out.println("    Solución: " + pregunta.getSolucion());

			// Validación específica para preguntas tipo test
			if (pregunta instanceof PreguntaTest) {
				validarPreguntaTest((PreguntaTest) pregunta);
			}
		}
	}

	/**
	 * Validación específica para preguntas de tipo test.
	 * 
	 * @param preguntaTest La pregunta de tipo test a validar
	 */
	private void validarPreguntaTest(PreguntaTest preguntaTest) {
		System.out.println("    Opciones: " + preguntaTest.getOpciones());
		System.out.println(
				"    Número opciones: " + (preguntaTest.getOpciones() != null ? preguntaTest.getOpciones().size() : 0));

		// Llamar al método debug específico de la pregunta
		preguntaTest.debug();
	}

	public Estadistica generarEstadisticas() {
		// En el caso de usar un repositorio de usuarios, se podría mover la lógica
		// a dicha clase
		int cursosCompletados = (int) progresos.stream()
				.filter(Progreso::esCursoCompletado)
				.count();

		int preguntasRespondidas = progresos.stream()
				.mapToInt(Progreso::getNumRespuestas)
				.sum();

		int preguntasAcertadas = progresos.stream()
				.mapToInt(Progreso::getNumRespuestasCorrectas)
				.sum();

		Estadistica estadisticas = new Estadistica();
		estadisticas.setCursosCompletados(cursosCompletados);
		estadisticas.setPreguntasRespondidas(preguntasRespondidas);
		estadisticas.setPreguntasAcertadas(preguntasAcertadas);

		return estadisticas;
	}

}
