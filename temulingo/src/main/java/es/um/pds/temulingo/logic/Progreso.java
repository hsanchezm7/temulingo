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
	
	@ElementCollection
    @CollectionTable(
        name = "RESPUESTAS_PREGUNTAS",
        joinColumns = @JoinColumn(name = "PROGRESO_ID")
    )
    @MapKeyJoinColumn(name = "PREGUNTA_ID")
    @Column(name = "RESPUESTA")
    private Map<Pregunta, String> respuestas = new HashMap<>();

	public Progreso() {
    }
	
    public Progreso(Curso curso) {
    	this.curso = curso;
    }

	public boolean resolverPregunta(Pregunta pregunta, String respuesta) {
		respuestas.put(pregunta, respuesta);
		
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