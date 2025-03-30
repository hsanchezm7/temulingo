package es.um.pds.temulingo.logic;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PREGUNTA")
public abstract class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ENUNCIADO")
    private String enunciado;

    @ManyToOne(fetch = FetchType.EAGER)
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
}
