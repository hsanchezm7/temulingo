package es.um.pds.temulingo.controlador;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.CargadorCursos;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Estadistica;
import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.PreguntaTest;
import es.um.pds.temulingo.logic.Progreso;
import es.um.pds.temulingo.logic.RepositorioCursos;
import es.um.pds.temulingo.logic.RepositorioUsuarios;
import es.um.pds.temulingo.logic.Usuario;

public class ControladorTemulingo {

	private static ControladorTemulingo instance = null;

	private Usuario usuarioActual;
	private Progreso cursoActual;

	private RepositorioUsuarios repoUsuarios;
	private RepositorioCursos repoCursos;

	private FactoriaDao factoriaDao;
	private Dao<Progreso> progresoDao;

	private ControladorTemulingo() {
		inicializarAdaptadores();

		this.repoUsuarios = RepositorioUsuarios.getInstance();
		this.repoCursos = RepositorioCursos.getInstance();

		this.usuarioActual = null;
		this.cursoActual = null;
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

	private void inicializarAdaptadores() {
		factoriaDao = FactoriaDao.getDaoFactory();

		progresoDao = factoriaDao.getProgresoDao();
	}

	public boolean registrarUsuario(String nombre, String email, String username, String password,
			LocalDate fechaNacim) {
		if (nombre == null || email == null || username == null || password == null || fechaNacim == null) {
			return false;
			// throw new IllegalArgumentException("Ningún campo puede ser nulo");
		}

		if (repoUsuarios.obtenerPorEmail(email).isPresent()) {
			return false;
			// throw new RegistroInvalidoException("Ya existe un usuario con este email");
		}

		if (repoUsuarios.obtenerPorUsername(username).isPresent()) {
			return false;
			// throw new RegistroInvalidoException("Ya existe un usuario con este nombre de
			// usuario");
		}

		repoUsuarios.guardarUsuario(nombre, email, username, password, fechaNacim);

		return true;
	}

	public boolean iniciarSesionConEmail(String email, String password) {
		if (email == null || password == null) {
			return false;
			// throw new IllegalArgumentException("El email y la contraseña no pueden ser
			// nulos");
		}

		Optional<Usuario> usuarioOpt = repoUsuarios.obtenerPorEmail(email);

		if (usuarioOpt.isEmpty()) {
			return false;
			// TODO: throw new CredencialesInvalidasException("Usuario no encontrado");
		}

		Usuario usuario = usuarioOpt.get();

		if (!usuario.getPassword().equals(password)) {
			return false;
			// TODO: throw new CredencialesInvalidasException("Contraseña incorrecta");
		}

		usuarioActual = usuario;

		return true;
	}

	public boolean iniciarSesionConUsername(String username, String password) {
		if (username == null || password == null) {
			return false;
			// throw new IllegalArgumentException("El nombre de usuario y la contraseña no
			// pueden ser nulos");
		}

		Optional<Usuario> usuarioOpt = repoUsuarios.obtenerPorUsername(username);

		if (usuarioOpt.isEmpty()) {
			return false;
			// TODO: throw new CredencialesInvalidasException("Usuario no encontrado");
		}

		Usuario usuario = usuarioOpt.get();

		if (!usuario.getPassword().equals(password)) {
			return false;
			// TODO: throw new CredencialesInvalidasException("Contraseña incorrecta");
		}

		usuarioActual = usuario;

		return true;
	}

	public void guardarCurso(Curso curso) {
		if (curso != null) {
			// Verificar si el curso ya existe en la lista del usuario
	        boolean cursoYaExiste = usuarioActual.getCursos().stream()
	            .anyMatch(c -> c.getId() != null && c.getId().equals(curso.getId()) || 
	                          c.getTitulo().equals(curso.getTitulo()));
	        
	        if (!cursoYaExiste) {
	            usuarioActual.addCurso(curso);
	            repoUsuarios.actualizarUsuario(usuarioActual);
	            System.out.println("Curso añadido: " + curso.getTitulo());
	        } else {
	            System.out.println("El curso ya existe en la lista del usuario: " + curso.getTitulo());
	        }
		}
	}

	public void importarCursoDesdeFichero(File fichero) throws IOException {
		Curso curso = CargadorCursos.parsearDesdeFichero(fichero, Curso.class, CargadorCursos.Formato.YAML);

		guardarCurso(curso);
	}

	public List<Curso> getAllCursos() {
		return usuarioActual.getCursos();
	}

	public Progreso getCursoActual() {
		return cursoActual;
	}

	public void setCursoActual(Progreso cursoActual) {
		this.cursoActual = cursoActual;
	}

	public void iniciarCurso(Curso curso) {
		// Persistir curso si es nuevo
		if (curso.getId() == null) {
			factoriaDao.getCursoDao().save(curso);
		}
		// Obtener curso gestionado por la BD
		Curso cursoGestionado = factoriaDao.getCursoDao().get(curso.getId())
				.orElseThrow(() -> new RuntimeException("Curso no encontrado"));
		
		// Buscar progreso existente
		Progreso progresoExistente = usuarioActual.getProgresos().stream()
				.filter(p -> p.getCurso().equals(cursoGestionado)).findFirst().orElse(null);

		if (progresoExistente != null) {
			setCursoActual(progresoExistente);
			System.out.println("Continuando curso existente: " + curso.getTitulo());
			progresoExistente.reiniciarConEstrategia(progresoExistente.getCurso().getEstrategiaAprendizaje());
	        
	        // Importante: NO limpiar las respuestas aquí, solo reinicializar estructuras
	        // Por eso creamos un método específico para esto
	        inicializarEstructurasProgresoExistente(progresoExistente);
	        System.out.println("Continuando curso existente: " + curso.getTitulo());
	        System.out.println("Estrategia aplicada: " + progresoExistente.getCurso().getEstrategiaAprendizaje());
	    
		} else {
			Progreso progresoNuevo = new Progreso();
			progresoNuevo.setCurso(cursoGestionado); // Usa el curso gestionado
			progresoNuevo.setUsuario(usuarioActual);

			usuarioActual.getProgresos().add(progresoNuevo);
			progresoDao.save(progresoNuevo);
			setCursoActual(progresoNuevo);
			System.out.println("Iniciando nuevo curso: " + curso.getTitulo());
			System.out.println("Estrategia aplicada: " + cursoGestionado.getEstrategiaAprendizaje());
		    
		}
	}

	/**
	 * Busca un progreso existente para un curso específico.
	 * 
	 * @param curso El curso para el cual buscar el progreso
	 * @return El progreso encontrado, o null si no existe
	 */
	/*
	 * private Progreso buscarProgresoPorCurso(Curso curso) { return
	 * usuarioActual.getProgresos().stream().filter(p ->
	 * p.getCurso().equals(curso)).findFirst().orElse(null); }
	 */

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

		preguntaTest.debug();
	}

	public Estadistica generarEstadisticas() {
		int cursosCompletados = usuarioActual.getCursosCompletados().size();
		int preguntasRespondidas = usuarioActual.getTotalPreguntasRespondidas();
		int preguntasAcertadas = usuarioActual.getTotalPreguntasAcertadas();

		Estadistica estadisticas = new Estadistica();
		estadisticas.setCursosCompletados(cursosCompletados);
		estadisticas.setPreguntasRespondidas(preguntasRespondidas);
		estadisticas.setPreguntasAcertadas(preguntasAcertadas);

		return estadisticas;
	}
	
	/**
	 * Inicializa las estructuras de estrategia para un progreso existente
	 * sin borrar las respuestas ya dadas
	 */
	private void inicializarEstructurasProgresoExistente(Progreso progreso) {
	    // Guardar las respuestas existentes
	    Map<Pregunta, String> respuestasExistentes = new HashMap<>(progreso.getRespuestas());
	    
	    // Reinicializar estructuras (esto borrará las respuestas temporalmente)
	    progreso.reiniciarConEstrategia(progreso.getCurso().getEstrategiaAprendizaje());
	    
	    // Restaurar las respuestas
	    progreso.setRespuestas(respuestasExistentes);
	    
	    System.out.println("Estructuras de estrategia inicializadas para progreso existente");
	    System.out.println("Respuestas restauradas: " + respuestasExistentes.size());
	}
}
