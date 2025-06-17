package es.um.pds.temulingo.controlador;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.um.pds.temulingo.dao.base.Dao;
import es.um.pds.temulingo.dao.factory.FactoriaDao;
import es.um.pds.temulingo.exception.ExcepcionCredencialesInvalidas;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.CargadorCursos;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Estadistica;
import es.um.pds.temulingo.logic.EstadoCurso;
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
	private Dao<Curso> cursoDao;

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
		cursoDao = factoriaDao.getCursoDao();
	}

	// ========================================
	// MÉTODOS DE AUTENTICACIÓN Y REGISTRO
	// ========================================

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

	public boolean iniciarSesion(String input, String password)
			throws NullPointerException, ExcepcionCredencialesInvalidas {
		// Caso 1: validar los parámetros de entrada
		Objects.requireNonNull(input, "El username/email no puede ser nulo");
		Objects.requireNonNull(password, "La contraseña no puede ser nula");

		// Caso 2: usuario no existe
		Usuario usuario;

		if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			usuario = repoUsuarios.obtenerPorEmail(input).orElseThrow(
					() -> new ExcepcionCredencialesInvalidas("Usuario con email '" + input + "' no encontrado"));
		} else {
			usuario = repoUsuarios.obtenerPorUsername(input).orElseThrow(() -> new ExcepcionCredencialesInvalidas(
					"Usuario con nombre de usuario '" + input + "' no encontrado"));
		}

		// Caso 3: la contraseña es incorrecta
		if (!usuario.getPassword().equals(password)) {
			throw new ExcepcionCredencialesInvalidas("La contraseña introducida es incorrecta");
		}

		usuarioActual = usuario;

		return true;

	}

	// ========================================
	// MÉTODOS DE GESTIÓN DE CURSOS
	// ========================================

	public void guardarCurso(Curso curso) {
		if (curso != null) {
			// Verificar si el curso ya existe en la lista del usuario
			boolean cursoYaExiste = usuarioActual.getCursos().stream()
					.anyMatch(c -> c.getId() != null && c.getId().equals(curso.getId())
							|| c.getTitulo().equals(curso.getTitulo()));

			if (!cursoYaExiste) {
				usuarioActual.addCurso(curso);
				repoCursos.guardarCurso(curso);
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

	// ========================================
	// MÉTODOS DE REANUDACIÓN DE CURSOS
	// ========================================

	/**
	 * CU08: Verifica si un curso tiene progreso guardado
	 */
	public boolean tieneProgresoGuardado(Curso curso) {
		if (usuarioActual == null) {
			return false;
		}

		return usuarioActual.getProgresos().stream()
				.anyMatch(p -> p.getCurso().equals(curso) && (p.isEstadoGuardado() || p.getNumRespuestas() > 0));
	}

	/**
	 * CU08: Obtiene el progreso guardado de un curso específico
	 */
	public Progreso obtenerProgresoGuardado(Curso curso) {
		if (usuarioActual == null) {
			return null;
		}

		return usuarioActual.getProgresos().stream()
				.filter(p -> p.getCurso().equals(curso) && (p.isEstadoGuardado() || p.getNumRespuestas() > 0))
				.findFirst().orElse(null);
	}

	/**
	 * CU08: Reanuda un curso desde donde se dejó
	 */
	public boolean reanudarCurso(Curso curso) {
		try {
			Progreso progresoGuardado = obtenerProgresoGuardado(curso);

			if (progresoGuardado == null) {
				System.err.println("No hay progreso guardado para el curso: " + curso.getTitulo());
				return false;
			}

			// Establecer como curso actual
			setCursoActual(progresoGuardado);

			// Asegurarse de que la estrategia esté configurada en el objeto Progreso
			if (progresoGuardado.getCurso().getEstrategiaAprendizaje() == null) {
				// Si por alguna razón no tiene estrategia, establecer una por defecto
				progresoGuardado.getCurso().setEstrategiaAprendizaje(Curso.EstrategiaAprendizaje.SECUENCIAL);
				cursoDao.edit(progresoGuardado.getCurso()); // Persistir si se cambió
			}

			// Inicializar estructuras de estrategia sin perder progreso
			inicializarEstructurasProgresoExistente(progresoGuardado);

			// Actualizar fecha de última sesión
			progresoGuardado.setFechaUltimaSesion(new Date());
			progresoDao.edit(progresoGuardado);

			System.out.println("Curso reanudado: " + curso.getTitulo());
			System.out.println("Progreso: " + String.format("%.1f%%", progresoGuardado.getProgresoPorcentaje()));
			System.out.println("Preguntas respondidas: " + progresoGuardado.getNumRespuestas() + "/"
					+ progresoGuardado.getNumTotalPreguntas());

			return true;

		} catch (Exception e) {
			System.err.println("Error al reanudar curso: " + e.getMessage());
			return false;
		}
	}

	/**
	 * CU08: Inicia un curso completamente nuevo
	 */
	public boolean empezarCursoNuevo(Curso curso) {
		try {
			// =========================================================================
			// *** CAMBIOS PARA SOLUCIONAR "Removing a detached instance" ***
			// =========================================================================

			Progreso progresoExistente = obtenerProgresoGuardado(curso); // Esta instancia podría estar detached
			if (progresoExistente != null) {
				System.out.println("Intentando eliminar progreso existente para curso: " + curso.getTitulo()
						+ " con ID: " + progresoExistente.getId());

				// 1. Obtener una instancia 'gestionada' del progreso existente usando su ID.
				// El método 'get' de tu DaoImpl es el equivalente a 'findById'.
				Progreso progresoManaged = null;
				if (progresoExistente.getId() != null) { // Asegúrate de que Progreso tenga un método getId()
					progresoManaged = progresoDao.get(progresoExistente.getId()).orElse(null);
				}

				if (progresoManaged != null) {
					// 2. Eliminar la instancia gestionada del progreso de la colección del usuario.
					usuarioActual.getProgresos().remove(progresoManaged);
					// 3. Eliminar la instancia gestionada a través del DAO.
					progresoDao.delete(progresoManaged); // Usar el método 'delete' de tu DaoImpl
					System.out.println("Progreso existente eliminado correctamente.");
				} else {
					// Esto ocurre si el progreso con ese ID ya no existe en la BD o no se pudo
					// adjuntar.
					// No se puede eliminar a través del DAO si no está gestionado o no existe.
					System.out.println("Advertencia: El progreso existente (ID: " + progresoExistente.getId()
							+ ") no se encontró para adjuntar/eliminar. Puede que ya haya sido eliminado o el ID sea incorrecto.");
					// Si aún necesitas eliminarlo de la lista en memoria del usuario (si estaba
					// allí):
					usuarioActual.getProgresos().remove(progresoExistente);
				}
			}

			// =========================================================================
			// *** FIN DE LOS CAMBIOS DE ELIMINACIÓN ***
			// =========================================================================

			// Crear nuevo progreso
			Progreso progresoNuevo = new Progreso();
			progresoNuevo.setCurso(curso);
			progresoNuevo.setUsuario(usuarioActual);
			progresoNuevo.setFechaUltimaSesion(new Date());

			usuarioActual.getProgresos().add(progresoNuevo);
			progresoDao.save(progresoNuevo); // Asegurarse de que el objeto se guarda y se le asigna un ID si es
												// necesario
			setCursoActual(progresoNuevo); // ESTO ESTÁ GARANTIZADO SI NO HAY EXCEPCIÓN PREVIA

			System.out.println("Curso nuevo iniciado: " + curso.getTitulo());
			return true;

		} catch (Exception e) {
			System.err.println("Error al iniciar curso nuevo: " + e.getMessage());
			e.printStackTrace(); // Imprime el stack trace completo para depuración
			setCursoActual(null); // Asegurarse de que cursoActual sea null si falla el inicio del curso
			return false;
		}
	}

	/**
	 * CU08: Obtiene información de resumen del progreso
	 */
	public String obtenerResumenProgreso(Curso curso) {
		Progreso progreso = obtenerProgresoGuardado(curso);
		if (progreso == null) {
			return "Sin progreso";
		}

		return String.format("%.1f%% - %d/%d preguntas", progreso.getProgresoPorcentaje(), progreso.getNumRespuestas(),
				progreso.getNumTotalPreguntas());
	}

	/**
	 * CU08: Obtiene la fecha de la última sesión formateada
	 */
	public String obtenerFechaUltimaSesion(Curso curso) {
		Progreso progreso = obtenerProgresoGuardado(curso);
		if (progreso == null || progreso.getFechaUltimaSesion() == null) {
			return "Nunca iniciado";
		}

		return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(progreso.getFechaUltimaSesion());
	}

	// ========================================
	// MÉTODOS DE PROGRESO Y CURSO ACTUAL
	// ========================================

	public Progreso getCursoActual() {
		return cursoActual;
	}

	public void setCursoActual(Progreso cursoActual) {
		this.cursoActual = cursoActual;
	}

	public void iniciarCurso(Curso curso) {
		/*
		 * // Buscar progreso existente Progreso progresoExistente =
		 * usuarioActual.getProgresos().stream().filter(p -> p.getCurso().equals(curso))
		 * .findFirst().orElse(null);
		 * 
		 * if (progresoExistente != null) { setCursoActual(progresoExistente);
		 * System.out.println("Continuando curso existente: " + curso.getTitulo());
		 * progresoExistente.reiniciarConEstrategia(progresoExistente.getCurso().
		 * getEstrategiaAprendizaje());
		 * 
		 * // Importante: NO limpiar las respuestas aquí, solo reinicializar estructuras
		 * // Por eso creamos un método específico para esto
		 * inicializarEstructurasProgresoExistente(progresoExistente);
		 * System.out.println("Continuando curso existente: " + curso.getTitulo());
		 * System.out.println("Estrategia aplicada: " +
		 * progresoExistente.getCurso().getEstrategiaAprendizaje());
		 * 
		 * } else { Progreso progresoNuevo = new Progreso();
		 * progresoNuevo.setCurso(curso); // Usa el curso gestionado
		 * progresoNuevo.setUsuario(usuarioActual);
		 * 
		 * usuarioActual.getProgresos().add(progresoNuevo);
		 * progresoDao.save(progresoNuevo); setCursoActual(progresoNuevo);
		 * System.out.println("Iniciando nuevo curso: " + curso.getTitulo());
		 * System.out.println("Estrategia aplicada: " +
		 * curso.getEstrategiaAprendizaje());
		 * 
		 * }
		 */
		if (tieneProgresoGuardado(curso)) {
			reanudarCurso(curso);
		} else {
			empezarCursoNuevo(curso);
		}
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
	// CU08: MÉTODOS DE GUARDADO
	// ========================================

	/**
	 * CU08: Guardado automático mejorado
	 */
	public boolean guardarEstadoAutomatico() {
		if (cursoActual == null) {
			return false;
		}

		try {
			cursoActual.setEstadoGuardado(true);
			cursoActual.setFechaUltimaSesion(new Date());
			progresoDao.edit(cursoActual);
			return true;
		} catch (Exception e) {
			System.err.println("Error en guardado automático: " + e.getMessage());
			return false;
		}
	}

	public boolean guardarEstadoCurso() {
		if (cursoActual == null) {
			return false;
		}

		try {
			// Marcar como guardado explícitamente
			cursoActual.setEstadoGuardado(true);
			cursoActual.setFechaUltimaSesion(new Date());

			// Guardar en BD
			progresoDao.edit(cursoActual);

			return true;
		} catch (Exception e) {
			System.err.println("Error al guardar estado del curso: " + e.getMessage());
			return false;
		}
	}

	public boolean pausarCurso(long tiempoTranscurrido) {
		if (cursoActual == null) {
			return false;
		}

		try {
			// Actualizar tiempo transcurrido
			cursoActual.setTiempoTranscurrido(cursoActual.getTiempoTranscurrido() + tiempoTranscurrido);

			// Guardar estado
			return guardarEstadoCurso();
		} catch (Exception e) {
			System.err.println("Error al pausar curso: " + e.getMessage());
			return false;
		}
	}

	public EstadoCurso obtenerEstadoActual() {
		if (cursoActual == null) {
			return null;
		}

		return new EstadoCurso(cursoActual.getSiguientePregunta(), cursoActual.getRespuestas(),
				cursoActual.getProgresoPorcentaje(), cursoActual.getTiempoTranscurrido(),
				cursoActual.getFechaUltimaSesion());
	}

	// ========================================
	// MÉTODOS DE ESTRATEGIA DE APRENDIZAJE
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
		// validarCursoActual();

		// Reiniciar el progreso con la nueva estrategia
		// cursoActual.reiniciarConEstrategia(estrategia);
		inicializarEstructurasProgresoExistente(cursoActual); // Este método ya maneja la restauración de respuestas.

		// Persistir los cambios en la base de datos
		progresoDao.edit(cursoActual);

		try {
			progresoDao.edit(cursoActual);
			cursoDao.edit(cursoActual.getCurso()); // Persistir la estrategia en el Curso
		} catch (Exception e) {
			System.err.println("Error al persistir cambios de estrategia: " + e.getMessage());
			e.printStackTrace();
		}

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
	 * Inicializa las estructuras de estrategia para un progreso existente sin
	 * borrar las respuestas ya dadas
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

	// ========================================
	// MÉTODOS DE DEBUG Y VALIDACIÓN
	// ========================================

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

	// ========================================
	// MÉTODOS DE ESTADÍSTICAS
	// ========================================

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

}
