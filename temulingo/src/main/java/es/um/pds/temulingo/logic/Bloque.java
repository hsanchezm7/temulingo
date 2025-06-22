package es.um.pds.temulingo.logic;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "BLOQUE")
public class Bloque implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "NOMBRE")
	private String nombre;

	@Column(name = "DESCRIPCION")
	private String descripcion;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CURSO_ID")
	@JsonIgnore
	private Curso curso;

	@OneToMany(mappedBy = "bloque", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Pregunta> preguntas = new LinkedList<>();

	public Bloque() {
	}

	public Bloque(Long id, String nombre, String descripcion, Curso curso, List<Pregunta> preguntas) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.curso = curso;
		this.preguntas = preguntas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Pregunta> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;

		// Asegurar bidireccionalidad
		if (preguntas != null) {
			preguntas.forEach(pregunta -> pregunta.setBloque(this));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Bloque bloque = (Bloque) o;
		return Objects.equals(id, bloque.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
