package es.um.pds.temulingo.logic;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_NACIM")
    private LocalDate fechaNacim;

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String email, LocalDate fechaNacim) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fechaNacim = fechaNacim;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaNacim() {
        return fechaNacim;
    }

    public void setFechaNacim(LocalDate fechaNacim) {
        this.fechaNacim = fechaNacim;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
