package es.um.pds.temulingo.logic;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "BLOQUE")
public class Bloque implements Serializable {

    public enum TipoEjercicio {
        TEST, HUECOS, TRADUCCION;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CURSO_ID")
    private Curso curso;

    @OneToMany(mappedBy = "bloque", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Pregunta> preguntas;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_EJERCICIO")
    private TipoEjercicio tipo;

    public Bloque() {
    }

    public Bloque(Long id, String nombre, String descripcion, Curso curso, TipoEjercicio tipo, List<Pregunta> preguntas) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.curso = curso;
        this.tipo = tipo;
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

    public TipoEjercicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoEjercicio tipo) {
        this.tipo = tipo;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bloque bloque = (Bloque) o;
        return Objects.equals(id, bloque.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
