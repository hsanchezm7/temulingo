package es.um.pds.temulingo.logic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PREGUNTA")
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "tipo"
	)
	@JsonSubTypes({
	    @JsonSubTypes.Type(value = PreguntaTest.class, name = "TEST"),
	    @JsonSubTypes.Type(value = PreguntaHuecos.class, name = "HUECOS"),
	    @JsonSubTypes.Type(value = PreguntaTraduccion.class, name = "TRADUCCION")
	})
public abstract class Pregunta {
	
	public enum TipoPregunta {
		TEST, HUECOS, TRADUCCION;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ENUNCIADO")
	private String enunciado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BLOQUE_ID")
	private Bloque bloque;

	protected Pregunta() {
	}

	protected Pregunta(Long id, String enunciado, Bloque bloque) {
		this.id = id;
		this.enunciado = enunciado;
		this.bloque = bloque;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public Bloque getBloque() {
		return bloque;
	}

	public void setBloque(Bloque bloque) {
		this.bloque = bloque;
	}
	
	public abstract boolean esSolucion(String respuesta);
	
}
