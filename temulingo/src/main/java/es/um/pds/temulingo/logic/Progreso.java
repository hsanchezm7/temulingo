package es.um.pds.temulingo.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.um.pds.temulingo.dao.factory.FactoriaDao;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROGRESO")
public class Progreso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "CURSO_ID")
	private Curso curso;

	// ¿Sería mejor inicializar el mapa con todas las respuestas en blanco,
	// por ejemplo, ocn un string vacío? Así no habría que acceder al objeto
	// curso?
	// Desventaja: sería difícil saber cual sería la siguiente pregunta
	@ElementCollection
	@CollectionTable(name = "RESPUESTAS_PREGUNTAS", joinColumns = @JoinColumn(name = "PROGRESO_ID"))
	@MapKeyJoinColumn(name = "PREGUNTA_ID")
	@Column(name = "RESPUESTA")
	private Map<Pregunta, String> respuestas = new HashMap<>();

	public Progreso() {
	}

	public Progreso(Curso curso) {
		this.curso = curso;
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

	public Map<Pregunta, String> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(Map<Pregunta, String> respuestas) {
		this.respuestas = respuestas;
	}

	public boolean resolverPregunta(Pregunta pregunta, String respuesta) {
		respuestas.put(pregunta, respuesta);

		// Actualiza la instancia persistente
		// TODO: quizás esta operación se debería hacer desde un
		// repositorio de Usuarios o de Progresos de cursos.
		FactoriaDao.getDaoFactory().getProgresoDao().edit(this);

		return pregunta.esSolucion(respuesta);
	}

	public Pregunta getSiguientePregunta() {
		for (Bloque bloque : curso.getBloques()) {
			for (Pregunta pregunta : bloque.getPreguntas()) {
				if (!respuestas.containsKey(pregunta)) {
					return pregunta;
				}
			}
		}
		return null;
	}

	public boolean cursoCompletado() {
		return getSiguientePregunta() == null;
	}

	public int getNumeroTotalPreguntas() {
		return curso.getBloques().stream().mapToInt(b -> b.getPreguntas().size()).sum();
	}

	public int getNumeroRespuestasCorrectas() {
		return (int) respuestas.entrySet().stream().filter(e -> e.getKey().esSolucion(e.getValue())).count();
	}

	public double getNotaTotal() {
		int total = getNumeroTotalPreguntas();
		if (total == 0) {
			return 0.0;
		}

		return (double) getNumeroRespuestasCorrectas() / total;
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

}