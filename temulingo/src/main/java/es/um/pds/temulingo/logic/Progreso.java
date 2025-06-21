package es.um.pds.temulingo.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.um.pds.temulingo.dao.factory.FactoriaDao;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity
@Table(name = "PROGRESO")
public class Progreso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CURSO_ID")
	private Curso curso;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Column(name = "TIEMPO_TRANSCURRIDO")
	private Long tiempoTranscurrido = 0L; // en milisegundos

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_ULTIMA_SESION")
	private Date fechaUltimaSesion;

	@Column(name = "ESTADO_GUARDADO")
	private boolean estadoGuardado = false;

	// ¿Sería mejor inicializar el mapa con todas las respuestas en blanco,
	// por ejemplo, ocn un string vacío? Así no habría que acceder al objeto
	// curso?
	// Desventaja: sería difícil saber cual sería la siguiente pregunta
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "RESPUESTAS_PREGUNTAS", joinColumns = @JoinColumn(name = "PROGRESO_ID"))
	@MapKeyJoinColumn(name = "PREGUNTA_ID")
	@Column(name = "RESPUESTA")
	@JsonIgnore
	private Map<Pregunta, String> respuestas = new HashMap<>();

	// Lista para mantener el orden de las preguntas según la estrategia
	@Transient
	private List<Pregunta> preguntasOrdenadas = new ArrayList<>();
	@Transient
	private int indicePreguntaActual = 0;

	@Transient
	private Map<Bloque, List<Pregunta>> preguntasPorBloque = new LinkedHashMap<>();
	@Transient
	private List<Bloque> bloquesOrdenados = new ArrayList<>();
	@Transient
	private int indiceBloqueActual = 0;
	@Transient
	private int indicePreguntaActualEnBloque = 0;

	public Progreso() {
	}

	public Progreso(Long id, Curso curso, Usuario usuario) {
		this.id = id;
		this.curso = curso;
		this.usuario = usuario;
	}

	// MÉTODO CLAVE: Inicializa las estructuras según la estrategia
	private void inicializarEstructurasEstrategia() {
		if (curso == null || curso.getEstrategiaAprendizaje() == null) {
			// Fallback a estrategia secuencial si no hay estrategia definida
			inicializarPreguntasSecuencial();
			return;
		}

		EstrategiaAprendizajeStrategy estrategiaImpl = EstrategiaFactory.crear(curso.getEstrategiaAprendizaje());
		this.preguntasPorBloque = estrategiaImpl.prepararPreguntas(curso.getBloques());
		this.bloquesOrdenados = new ArrayList<>(preguntasPorBloque.keySet());

		// Calcular los índices correctos basándose en las respuestas ya dadas
		recalcularIndices();
	}

	private void inicializarPreguntasSecuencial() {
		preguntasPorBloque.clear();
		bloquesOrdenados.clear();

		for (Bloque bloque : curso.getBloques()) {
			preguntasPorBloque.put(bloque, new ArrayList<>(bloque.getPreguntas()));
			bloquesOrdenados.add(bloque);
		}

		recalcularIndices();
	}

	// Recalcula los índices basándose en las respuestas ya dadas
	private void recalcularIndices() {
		int preguntasRespondidas = respuestas.size();
		int contador = 0;

		indiceBloqueActual = 0;
		indicePreguntaActualEnBloque = 0;

		// Encontrar la posición actual basándose en las preguntas ya respondidas
		for (int i = 0; i < bloquesOrdenados.size(); i++) {
			Bloque bloque = bloquesOrdenados.get(i);
			List<Pregunta> preguntas = preguntasPorBloque.get(bloque);

			if (contador + preguntas.size() <= preguntasRespondidas) {
				// Este bloque está completamente respondido
				contador += preguntas.size();
			} else {
				// Este es el bloque actual
				indiceBloqueActual = i;
				indicePreguntaActualEnBloque = preguntasRespondidas - contador;
				break;
			}
		}

		if (contador == preguntasRespondidas && indiceBloqueActual < bloquesOrdenados.size()) {
			// Estamos al final de un bloque, pero hay más bloques
			// No hacer nada, los índices ya están correctos
		}
	}

	private void inicializarPreguntasOrdenadas() {
		preguntasOrdenadas.clear();
		for (Bloque bloque : curso.getBloques()) {
			preguntasOrdenadas.addAll(bloque.getPreguntas());
		}
		indicePreguntaActual = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
		// Reinicializar estructuras cuando se cambia el curso
		if (curso != null) {
			inicializarEstructurasEstrategia();
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Map<Pregunta, String> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(Map<Pregunta, String> respuestas) {
		this.respuestas = respuestas;

		// Recalcular índices cuando se cambian las respuestas
		if (curso != null) {
			recalcularIndices();
		}
	}

	public boolean resolverPregunta(Pregunta pregunta, String respuesta) {
		respuestas.put(pregunta, respuesta);

		// Avanzar al siguiente índice
		if (indicePreguntaActualEnBloque < preguntasPorBloque.get(bloquesOrdenados.get(indiceBloqueActual)).size()) {
			indicePreguntaActualEnBloque++;
		} // Si hemos terminado el bloque actual, pasar al siguiente
		if (indiceBloqueActual < bloquesOrdenados.size() && indicePreguntaActualEnBloque >= preguntasPorBloque
				.get(bloquesOrdenados.get(indiceBloqueActual)).size()) {
			indiceBloqueActual++;
			indicePreguntaActualEnBloque = 0;
		}

		// Actualiza la instancia persistente
		// TODO: quizás esta operación se debería hacer desde un
		// repositorio de Usuarios o de Progresos de cursos.
		FactoriaDao.getDaoFactory().getProgresoDao().edit(this);

		return pregunta.esSolucion(respuesta);
	}

	public Pregunta getSiguientePregunta() {

		// Asegurar que las estructuras estén inicializadas
		if (bloquesOrdenados.isEmpty() || preguntasPorBloque.isEmpty()) {
			System.out.println("DEBUG: Inicializando estructuras de estrategia en getSiguientePregunta()");
			inicializarEstructurasEstrategia();
		}

		System.out.println("DEBUG: Estado actual - Bloque: " + indiceBloqueActual + ", Pregunta en bloque: "
				+ indicePreguntaActualEnBloque);
		System.out.println("DEBUG: Total bloques: " + bloquesOrdenados.size());

		while (indiceBloqueActual < bloquesOrdenados.size()) {
			Bloque bloque = bloquesOrdenados.get(indiceBloqueActual);
			List<Pregunta> preguntas = preguntasPorBloque.get(bloque);

			System.out.println(
					"DEBUG: Bloque actual: " + bloque.getNombre() + ", preguntas en bloque: " + preguntas.size());

			if (indicePreguntaActualEnBloque < preguntas.size()) {
				Pregunta siguientePregunta = preguntas.get(indicePreguntaActualEnBloque);

				System.out.println("DEBUG: Devolviendo pregunta: " + siguientePregunta.getEnunciado());
				// NO verificar si ya fue respondida - seguir el orden de la estrategia
				return siguientePregunta;
			} else {
				System.out.println("DEBUG: Fin de bloque, pasando al siguiente");
				indiceBloqueActual++;
				indicePreguntaActualEnBloque = 0;
			}
		}

		System.out.println("DEBUG: No hay más preguntas - curso completado");
		return null;
	}

	public boolean esCursoCompletado() {
		return getSiguientePregunta() == null;
	}

	public int getNumTotalPreguntas() {
		// TODO: mover lógica a Curso, para no violar patrón experto
		//return curso.getBloques().stream().mapToInt(b -> b.getPreguntas().size()).sum();
		return curso.getTotalPreguntas();
	}

	public int getNumRespuestasCorrectas() {
		return (int) respuestas.entrySet().stream().filter(e -> e.getKey().esSolucion(e.getValue())).count();
	}

	public double getNotaTotal() {
		int total = getNumTotalPreguntas();
		if (total == 0) {
			return 0.0;
		}

		return (double) getNumRespuestasCorrectas() / total;
	}

	public int getNumRespuestas() {
		return respuestas.size();
	}

	public double getProgresoPorcentaje() {
		if (getNumTotalPreguntas() == 0) {
			return 0.0;
		}
		return (double) getNumRespuestas() / getNumTotalPreguntas() * 100;
	}
	
	// Getters y setters
	public Long getTiempoTranscurrido() { return tiempoTranscurrido; }
	public void setTiempoTranscurrido(Long tiempoTranscurrido) { this.tiempoTranscurrido = tiempoTranscurrido; }

	public Date getFechaUltimaSesion() { return fechaUltimaSesion; }
	public void setFechaUltimaSesion(Date fechaUltimaSesion) { this.fechaUltimaSesion = fechaUltimaSesion; }

	public boolean isEstadoGuardado() { return estadoGuardado; }
	public void setEstadoGuardado(boolean estadoGuardado) { this.estadoGuardado = estadoGuardado; }

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Progreso progreso = (Progreso) o;
		return Objects.equals(id, progreso.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	public void reiniciarConEstrategia(Curso.EstrategiaAprendizaje estrategia) {
		// Aplicar la nueva estrategia PRIMERO
		EstrategiaAprendizajeStrategy estrategiaImpl = EstrategiaFactory.crear(estrategia);
		this.preguntasPorBloque = estrategiaImpl.prepararPreguntas(curso.getBloques());
		this.bloquesOrdenados = new ArrayList<>(preguntasPorBloque.keySet());

		// Reiniciar el progreso DESPUÉS de aplicar la estrategia
		this.respuestas.clear();
		this.indicePreguntaActual = 0;
		this.indiceBloqueActual = 0;
		this.indicePreguntaActualEnBloque = 0;

		// Debug
		System.out.println("=== PREGUNTAS ORDENADAS POR ESTRATEGIA ===");
		for (Map.Entry<Bloque, List<Pregunta>> entry : preguntasPorBloque.entrySet()) {
			System.out.println("Bloque: " + entry.getKey().getNombre());
			for (Pregunta p : entry.getValue()) {
				System.out.println(" - " + p.getEnunciado());
			}
		}
		System.out.println("==========================================");
	}

}