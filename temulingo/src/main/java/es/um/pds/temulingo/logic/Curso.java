package es.um.pds.temulingo.logic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonSetter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "CURSO")
public class Curso implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum EstrategiaAprendizaje {
		SECUENCIAL, REPETICION_ESPACIADA, ALEATORIA;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TITULO")
	private String titulo;

	@Column(name = "AUTOR")
	private String autor;

	@Column(name = "DESCRIPCION")
	private String descripcion;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CREACION")
	private LocalDate fechaCreacion;

	@Enumerated(EnumType.STRING)
	@Column(name = "ESTRAT_APRENDIZAJE")
	private EstrategiaAprendizaje estrategiaAprendizaje;

	@OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Bloque> bloques = new LinkedList<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;

	public Curso() {
	}

	public Curso(Long id, String titulo, String descripcion, LocalDate fechaCreacion, List<Bloque> bloques) {
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.bloques = bloques;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public List<Bloque> getBloques() {
		return bloques;
	}

	@JsonSetter("bloques")
	public void setBloques(List<Bloque> bloques) {
		this.bloques = bloques;

		bloques.forEach(bloque -> bloque.setCurso(this));
	}

	public EstrategiaAprendizaje getEstrategiaAprendizaje() {
		return estrategiaAprendizaje;
	}

	public void setEstrategiaAprendizaje(EstrategiaAprendizaje estrategiaAprendizaje) {
		this.estrategiaAprendizaje = estrategiaAprendizaje;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Curso curso = (Curso) o;
		return Objects.equals(id, curso.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	public int getTotalPreguntas() {
		// Contar total de preguntas en todos los bloques
		return bloques.stream().mapToInt(bloque -> bloque.getPreguntas().size()).sum();
	}

}
