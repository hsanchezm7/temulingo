package es.um.pds.temulingo.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

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

	public Progreso() {
	}

	public Progreso(Long id, Curso curso, Usuario usuario) {
		this.id = id;
		this.curso = curso;
		this.usuario = usuario;
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
	}

	public boolean resolverPregunta(Pregunta pregunta, String respuesta) {
		respuestas.put(pregunta, respuesta);

		// Avanzar al siguiente índice
		indicePreguntaActual++;

		// Actualiza la instancia persistente
		// TODO: quizás esta operación se debería hacer desde un
		// repositorio de Usuarios o de Progresos de cursos.
		FactoriaDao.getDaoFactory().getProgresoDao().edit(this);

		return pregunta.esSolucion(respuesta);
	}

	public Pregunta getSiguientePregunta() {
		/*
		 * for (Bloque bloque : curso.getBloques()) { for (Pregunta pregunta :
		 * bloque.getPreguntas()) { if (!respuestas.containsKey(pregunta)) { return
		 * pregunta; } } } return null;
		 */
		// Si no hay preguntas ordenadas, inicializar
		if (preguntasOrdenadas.isEmpty()) {
			inicializarPreguntasOrdenadas();
		}

		// Buscar la siguiente pregunta no respondida en el orden establecido
		for (int i = 0; i < preguntasOrdenadas.size(); i++) {
			Pregunta pregunta = preguntasOrdenadas.get(i);
			if (!respuestas.containsKey(pregunta)) {
				return pregunta;
			}
		}

		return null;
	}

	public boolean esCursoCompletado() {
		return getSiguientePregunta() == null;
	}

	public int getNumTotalPreguntas() {
		// TODO: mover lógica a Curso, para no violar patrón experto
		return curso.getBloques().stream().mapToInt(b -> b.getPreguntas().size()).sum();
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
		// Reiniciar el progreso
		this.respuestas.clear();
		this.indicePreguntaActual = 0;

		// Preparar las preguntas según la estrategia
		prepararPreguntasSegunEstrategia(estrategia);
	}

	private void prepararPreguntasSegunEstrategia(Curso.EstrategiaAprendizaje estrategia) {
		// Obtener todas las preguntas del curso
		List<Pregunta> todasLasPreguntas = new ArrayList<>();
		for (Bloque bloque : curso.getBloques()) {
			todasLasPreguntas.addAll(bloque.getPreguntas());
		}

		switch (estrategia) {
		case SECUENCIAL:
			// Las preguntas ya están en orden secuencial
			preguntasOrdenadas = new ArrayList<>(todasLasPreguntas);
			break;

		case ALEATORIA:
			// Mezclar las preguntas aleatoriamente
			preguntasOrdenadas = new ArrayList<>(todasLasPreguntas);
			Collections.shuffle(preguntasOrdenadas, new Random()); // CORREGIDO: era todasLasPreguntas
			break;

		case REPETICION_ESPACIADA:
			// Para la repetición espaciada, podríamos implementar un algoritmo más complejo
			// Por ahora, mezclamos con un seed fijo para que sea reproducible
			preguntasOrdenadas = new ArrayList<>(todasLasPreguntas);
			Collections.shuffle(preguntasOrdenadas, new Random(42)); // CORREGIDO: era todasLasPreguntas
			break;
		}

		// Actualizar el orden de las preguntas en los bloques
		// actualizarOrdenPreguntas(todasLasPreguntas);
	}

	/*
	 * private void actualizarOrdenPreguntas(List<Pregunta> preguntasOrdenadas) { //
	 * Limpiar preguntas de todos los bloques for (Bloque bloque :
	 * curso.getBloques()) { bloque.getPreguntas().clear(); }
	 * 
	 * // Redistribuir las preguntas ordenadas // Por simplicidad, las ponemos todas
	 * en el primer bloque if (!curso.getBloques().isEmpty()) {
	 * curso.getBloques().get(0).getPreguntas().addAll(preguntasOrdenadas); } }
	 */

}