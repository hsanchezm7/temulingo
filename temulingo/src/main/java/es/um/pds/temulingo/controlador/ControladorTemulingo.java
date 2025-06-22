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
import es.um.pds.temulingo.exception.ExcepcionRegistroInvalido;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Estadistica;
import es.um.pds.temulingo.logic.EstadoCurso;
import es.um.pds.temulingo.logic.ParseadorCursos;
import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.PreguntaHuecos;
import es.um.pds.temulingo.logic.PreguntaTest;
import es.um.pds.temulingo.logic.PreguntaTraduccion;
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

	private boolean firstLogin;

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

	public boolean registrarUsuario(String nombre, String email, String username, String password, LocalDate fechaNacim)
			throws NullPointerException, ExcepcionRegistroInvalido {

		// Caso 1: validar los parámetros de entrada
		Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
		Objects.requireNonNull(email, "El email no puede ser nulo");
		Objects.requireNonNull(username, "El nombre de usuario no puede ser nulo");
		Objects.requireNonNull(password, "La contraseña no puede ser nula");
		Objects.requireNonNull(fechaNacim, "La fecha de nacimiento no puede ser nula");

		// Caso 2: el email ya está registrado
		if (repoUsuarios.obtenerPorEmail(email).isPresent()) {
			throw new ExcepcionRegistroInvalido("Ya existe un usuario con el email: " + email);
		}

		// Caso 3: el username ya está registrado
		if (repoUsuarios.obtenerPorUsername(username).isPresent()) {
			throw new ExcepcionRegistroInvalido("Ya existe un usuario con el nombre de usuario: " + username);
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
		firstLogin = usuario.isFirstLogin();

		usuarioActual.setFirstLogin(false);
		repoUsuarios.actualizarUsuario(usuarioActual);

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
		ParseadorCursos.Formato formato = null;
		String nombre = fichero.getName().toLowerCase();

		// Detectar por extensión
		if (nombre.endsWith(".json")) {
			formato = ParseadorCursos.Formato.JSON;
		} else if (nombre.endsWith(".yaml") || nombre.endsWith(".yml")) {
			formato = ParseadorCursos.Formato.YAML;
		}

		Curso curso = ParseadorCursos.parsearDesdeFichero(fichero, Curso.class, formato);
		guardarCurso(curso);
	}

	public void exportarCursoAFichero(Curso curso, File fichero, ParseadorCursos.Formato formato) throws IOException {
		ParseadorCursos.parsearAFichero(curso, fichero, formato);
	}

	public List<Curso> getAllCursos() {
		return usuarioActual.getCursos();
	}

	// ========================================
	// MÉTODOS DE REANUDACIÓN DE CURSOS
	// ========================================

	public void iniciarCurso(Curso curso) {
		if (tieneProgresoGuardado(curso)) {
			reanudarCurso(curso);
		} else {
			empezarCursoNuevo(curso);
		}
	}

	/**
	 * Verifica si un curso tiene progreso guardado
	 */
	public boolean tieneProgresoGuardado(Curso curso) {
		if (usuarioActual == null) {
			return false;
		}

		return usuarioActual.getProgresos().stream()
				.anyMatch(p -> p.getCurso().equals(curso) && (p.isEstadoGuardado() || p.getNumRespuestas() > 0));
	}

	/**
	 * Obtiene el progreso guardado de un curso específico
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
	 * Reanuda un curso desde donde se dejó
	 */
	public boolean reanudarCurso(Curso curso) {
		try {
			Progreso progresoGuardado = obtenerProgresoGuardado(curso);

			if (progresoGuardado == null) {
				System.err.println("No hay progreso guardado para el curso: " + curso.getTitulo());
				return false;
			}

			setCursoActual(progresoGuardado);

			if (progresoGuardado.getCurso().getEstrategiaAprendizaje() == null) {
				progresoGuardado.getCurso().setEstrategiaAprendizaje(Curso.EstrategiaAprendizaje.SECUENCIAL);
				cursoDao.edit(progresoGuardado.getCurso());
			}

			inicializarEstructurasProgresoExistente(progresoGuardado);

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
	 * Inicia un curso completamente nuevo
	 */
	public boolean empezarCursoNuevo(Curso curso) {
		try {

			Progreso progresoExistente = obtenerProgresoGuardado(curso);
			if (progresoExistente != null) {
				System.out.println("Intentando eliminar progreso existente para curso: " + curso.getTitulo()
						+ " con ID: " + progresoExistente.getId());

				Progreso progresoManaged = null;
				if (progresoExistente.getId() != null) {
					progresoManaged = progresoDao.get(progresoExistente.getId()).orElse(null);
				}

				if (progresoManaged != null) {
					usuarioActual.getProgresos().remove(progresoManaged);
					progresoDao.delete(progresoManaged); // Usar el método 'delete' de tu DaoImpl
					System.out.println("Progreso existente eliminado correctamente.");
				} else {
					System.out.println("Advertencia: El progreso existente (ID: " + progresoExistente.getId()
							+ ") no se encontró para adjuntar/eliminar. Puede que ya haya sido eliminado o el ID sea incorrecto.");
					usuarioActual.getProgresos().remove(progresoExistente);
				}
			}

			// Crear nuevo progreso
			Progreso progresoNuevo = new Progreso();
			progresoNuevo.setCurso(curso);
			progresoNuevo.setUsuario(usuarioActual);
			progresoNuevo.setFechaUltimaSesion(new Date());

			usuarioActual.getProgresos().add(progresoNuevo);
			progresoDao.save(progresoNuevo);
			setCursoActual(progresoNuevo);

			System.out.println("Curso nuevo iniciado: " + curso.getTitulo());
			return true;

		} catch (Exception e) {
			System.err.println("Error al iniciar curso nuevo: " + e.getMessage());
			e.printStackTrace();
			setCursoActual(null);
			return false;
		}
	}

	/**
	 * Obtiene información de resumen del progreso
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
	 * Obtiene la fecha de la última sesión formateada
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
	// MÉTODOS DE GUARDADO
	// ========================================

	/**
	 * Guardado automático mejorado
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

		inicializarEstructurasProgresoExistente(cursoActual);

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

	public boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	// ========================================
	// MÉTODOS PARA SOPORTE MVC DE LA VISTA
	// ========================================

	/**
	 * Encuentra el bloque que contiene una pregunta específica
	 */
	public Bloque encontrarBloqueParaPregunta(Pregunta pregunta) {
		if (cursoActual == null || cursoActual.getCurso() == null) {
			return null;
		}

		return cursoActual.getCurso().getBloques().stream().filter(bloque -> bloque.getPreguntas().contains(pregunta))
				.findFirst().orElse(null);
	}

	/**
	 * Determina la siguiente acción en el flujo del curso
	 */
	public enum TipoAccion {
		SIGUIENTE_PREGUNTA, NUEVO_BLOQUE, FINALIZAR
	}

	public class ResultadoNavegacion {
		private TipoAccion tipo;
		private Bloque bloque;

		public ResultadoNavegacion(TipoAccion tipo, Bloque bloque) {
			this.tipo = tipo;
			this.bloque = bloque;
		}

		public TipoAccion getTipo() {
			return tipo;
		}

		public Bloque getBloque() {
			return bloque;
		}
	}

	public ResultadoNavegacion determinarSiguienteAccion(Pregunta preguntaActual) {
		Bloque bloqueActual = encontrarBloqueParaPregunta(preguntaActual);
		Pregunta proximaPregunta = getSiguientePregunta();

		if (proximaPregunta == null) {
			return new ResultadoNavegacion(TipoAccion.FINALIZAR, null);
		}

		Bloque bloqueProximaPregunta = encontrarBloqueParaPregunta(proximaPregunta);

		if (bloqueActual != null && bloqueProximaPregunta != null && bloqueActual != bloqueProximaPregunta) {
			return new ResultadoNavegacion(TipoAccion.NUEVO_BLOQUE, bloqueProximaPregunta);
		} else {
			return new ResultadoNavegacion(TipoAccion.SIGUIENTE_PREGUNTA, bloqueProximaPregunta);
		}
	}

	/**
	 * Formatea el tiempo transcurrido para mostrar en la vista
	 */
	public String formatearTiempoTranscurrido(long tiempoInicio) {
		if (cursoActual == null) {
			return "Tiempo: --:--:--";
		}

		long tiempoTranscurridoAcumulado = cursoActual.getTiempoTranscurrido();
		long tiempoActualSesion = System.currentTimeMillis() - tiempoInicio;
		long tiempoTotal = tiempoTranscurridoAcumulado + tiempoActualSesion;

		long segundos = (tiempoTotal / 1000) % 60;
		long minutos = (tiempoTotal / (1000 * 60)) % 60;
		long horas = (tiempoTotal / (1000 * 60 * 60));

		return String.format("Tiempo: %02d:%02d:%02d", horas, minutos, segundos);
	}

	/**
	 * Actualiza el tiempo transcurrido del curso actual
	 */
	public boolean actualizarTiempoSesion(long tiempoInicio) {
		if (cursoActual == null) {
			return false;
		}

		long tiempoActualSesion = System.currentTimeMillis() - tiempoInicio;
		cursoActual.setTiempoTranscurrido(cursoActual.getTiempoTranscurrido() + tiempoActualSesion);

		return true;
	}

	/**
	 * Procesa una respuesta y retorna toda la información necesaria para la vista
	 */
	public RespuestaProcesada procesarRespuestaCompleta(Pregunta pregunta, String respuesta) {
		boolean esCorrecta = resolverPregunta(pregunta, respuesta);
		String respuestaCorrecta = obtenerRespuestaCorrecta(pregunta);

		String feedbackTexto;
		if (esCorrecta) {
			feedbackTexto = "¡Correcto! Muy bien.";
		} else {
			feedbackTexto = "Incorrecto. La respuesta correcta era: " + respuestaCorrecta;
		}

		return new RespuestaProcesada(esCorrecta, feedbackTexto, respuestaCorrecta);
	}

	/**
	 * Obtiene la respuesta correcta de cualquier tipo de pregunta
	 */
	private String obtenerRespuestaCorrecta(Pregunta pregunta) {
		if (pregunta instanceof PreguntaTest) {
			return ((PreguntaTest) pregunta).getSolucion();
		} else if (pregunta instanceof PreguntaHuecos) {
			return ((PreguntaHuecos) pregunta).getSolucion();
		} else if (pregunta instanceof PreguntaTraduccion) {
			return ((PreguntaTraduccion) pregunta).getSolucion();
		}
		return "Respuesta no disponible";
	}

	/**
	 * Clase para encapsular el resultado del procesamiento de una respuesta
	 */
	public class RespuestaProcesada {
		private boolean esCorrecta;
		private String feedbackTexto;
		private String respuestaCorrecta;

		public RespuestaProcesada(boolean esCorrecta, String feedbackTexto, String respuestaCorrecta) {
			this.esCorrecta = esCorrecta;
			this.feedbackTexto = feedbackTexto;
			this.respuestaCorrecta = respuestaCorrecta;
		}

		public boolean isEsCorrecta() {
			return esCorrecta;
		}

		public String getFeedbackTexto() {
			return feedbackTexto;
		}

		public String getRespuestaCorrecta() {
			return respuestaCorrecta;
		}
	}

}
