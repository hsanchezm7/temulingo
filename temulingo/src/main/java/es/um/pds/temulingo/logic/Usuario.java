package es.um.pds.temulingo.logic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NOMBRE")
	private String nombre;

	@Column(name = "EMAIL", unique = true)
	private String email;

	@Column(name = "USERNAME", unique = true)
	private String username;

	// TODO: Implementar cifrado/hash de contraseña para no usar texto plano
	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_NACIM")
	private LocalDate fechaNacim;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Curso> cursos = new LinkedList<>();

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Progreso> progresos = new ArrayList<>();

	public Usuario() {
	}

	public Usuario(Long id, String nombre, String email, String username, String password, LocalDate fechaNacim,
			List<Curso> cursos, List<Progreso> progresos) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.username = username;
		this.password = password;
		this.fechaNacim = fechaNacim;
		this.cursos = cursos;
		this.progresos = progresos;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getFechaNacim() {
		return fechaNacim;
	}

	public void setFechaNacim(LocalDate fechaNacim) {
		this.fechaNacim = fechaNacim;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public void addCurso(Curso curso) {
		cursos.add(curso);
		curso.setUsuario(this);
	}

	public List<Progreso> getProgresos() {
		return progresos;
	}

	public void setProgresos(List<Progreso> progresos) {
		this.progresos = progresos;
	}

	public void addProgreso(Progreso progreso) {
		progresos.add(progreso);
	}

	public Progreso iniciarCurso(Curso curso) {
		Progreso cursoNuevo = new Progreso();
		cursoNuevo.setCurso(curso);

		progresos.add(cursoNuevo);

		return cursoNuevo;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Usuario usuario = (Usuario) o;
		return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}

}
