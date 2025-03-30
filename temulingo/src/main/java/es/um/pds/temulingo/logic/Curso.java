package es.um.pds.temulingo.logic;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "CURSO")
public class Curso implements Serializable {

    public enum EstrategiaAprendizaje {
        SECUENCIAL, REPETICION_ESPACIADA, ALEATORIA;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTRAT_APRENDIZAJE")
    private EstrategiaAprendizaje estrategiaAprendizaje;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Bloque> bloques;

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

    public void setBloques(List<Bloque> bloques) {
        this.bloques = bloques;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(id, curso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
