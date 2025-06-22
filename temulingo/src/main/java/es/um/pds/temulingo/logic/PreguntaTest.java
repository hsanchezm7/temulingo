package es.um.pds.temulingo.logic;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

/**
 * Entidad que representa una pregunta de tipo test (opción múltiple). Extiende
 * de la clase base Pregunta y añade opciones de respuesta y una solución
 * correcta.
 * 
 * Utiliza herencia con tabla única (discriminador "TEST") para diferenciarse de
 * otros tipos de preguntas en la base de datos.
 */
@Entity
@DiscriminatorValue("TEST")
public class PreguntaTest extends Pregunta {

	/**
	 * Lista de opciones de respuesta para la pregunta. Se almacena en una tabla
	 * separada para normalizar la base de datos.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PreguntaTest_opciones", joinColumns = @JoinColumn(name = "PreguntaTest_id"))
	@Column(name = "OPCIONES")
	@JsonProperty("opciones")
	private List<String> opciones;

	/**
	 * Respuesta correcta de la pregunta. Debe coincidir con una de las opciones
	 * disponibles.
	 */
	@Column(name = "SOLUCION")
	@JsonProperty("solucion") // Mapeo explícito para Jackson
	private String solucion;

	// ===== CONSTRUCTORES =====

	/**
	 * Constructor por defecto requerido por JPA. Inicializa la lista de opciones
	 * vacía para evitar NullPointerException.
	 */
	public PreguntaTest() {
		super();
		this.opciones = new ArrayList<>();
	}

	/**
	 * Constructor con parámetros para crear una pregunta test completa.
	 * 
	 * @param id        ID único de la pregunta
	 * @param enunciado Texto de la pregunta
	 * @param bloque    Bloque temático al que pertenece
	 * @param opciones  Lista de opciones de respuesta
	 * @param solucion  Respuesta correcta
	 */
	public PreguntaTest(Long id, String enunciado, Bloque bloque, List<String> opciones, String solucion) {
		super(id, enunciado, bloque);
		this.opciones = opciones != null ? new ArrayList<>(opciones) : new ArrayList<>();
		this.solucion = solucion;
	}

	// ===== GETTERS Y SETTERS =====

	/**
	 * Obtiene la lista de opciones de respuesta. Garantiza que nunca retorne null
	 * inicializando la lista si es necesario.
	 * 
	 * @return Lista de opciones de respuesta
	 */
	@JsonProperty("opciones")
	public List<String> getOpciones() {
		if (opciones == null) {
			opciones = new ArrayList<>();
		}
		return opciones;
	}

	/**
	 * Establece las opciones de respuesta para la pregunta. Crea una copia
	 * defensiva de la lista para evitar modificaciones externas.
	 * 
	 * @param opciones Nueva lista de opciones
	 */
	@JsonSetter("opciones")
	public void setOpciones(List<String> opciones) {
		if (opciones != null) {
			this.opciones = new ArrayList<>(opciones);
		} else {
			this.opciones = new ArrayList<>();
		}
	}

	/**
	 * Obtiene la solución correcta de la pregunta.
	 * 
	 * @return Respuesta correcta
	 */
	@Override
	@JsonProperty("solucion")
	public String getSolucion() {
		return solucion;
	}

	/**
	 * Establece la solución correcta de la pregunta.
	 * 
	 * @param solucion Nueva respuesta correcta
	 */
	@JsonSetter("solucion")
	public void setSolucion(String solucion) {
		this.solucion = solucion;
	}

	// ===== MÉTODOS DE LÓGICA DE NEGOCIO =====

	/**
	 * Verifica si una respuesta dada es correcta para esta pregunta. Realiza
	 * comparación normalizada (sin espacios extra, en minúsculas) para ser más
	 * tolerante con las variaciones de entrada del usuario.
	 * 
	 * @param respuesta Respuesta a verificar
	 * @return true si la respuesta es correcta, false en caso contrario
	 */
	@Override
	public boolean esSolucion(String respuesta) {
		if (respuesta == null || solucion == null) {
			return false;
		}

		// Normalizar respuestas: convertir a minúsculas y remover espacios extra
		String respuestaNormalizada = respuesta.trim().toLowerCase();
		String solucionNormalizada = solucion.trim().toLowerCase();

		return respuestaNormalizada.equals(solucionNormalizada);
	}

	/**
	 * Valida que la pregunta tenga todos los datos necesarios y sea coherente. Una
	 * pregunta test válida debe tener: - Enunciado no vacío - Al menos una opción -
	 * Una solución definida - La solución debe estar entre las opciones disponibles
	 * 
	 * @return true si la pregunta es válida, false en caso contrario
	 */
	@JsonIgnore
	public boolean isValid() {
		// Verificar enunciado
		if (getEnunciado() == null || getEnunciado().trim().isEmpty()) {
			return false;
		}

		// Verificar opciones
		if (opciones == null || opciones.isEmpty()) {
			return false;
		}

		// Verificar solución
		if (solucion == null || solucion.trim().isEmpty()) {
			return false;
		}

		// Verificar coherencia: la solución debe estar entre las opciones
		return opciones.contains(solucion);
	}

	// ===== MÉTODOS DE UTILIDAD PARA DEBUGGING =====

	/**
	 * Método de utilidad para imprimir información completa de la pregunta. Útil
	 * durante el desarrollo y depuración.
	 */
	public void debug() {
		System.out.println("=== DEBUG PreguntaTest ===");
		System.out.println("ID: " + getId());
		System.out.println("Enunciado: " + getEnunciado());
		System.out.println("Solución: " + solucion);
		System.out.println("Opciones: " + (opciones != null ? opciones : "NULL"));
		System.out.println("Número de opciones: " + (opciones != null ? opciones.size() : 0));

		if (opciones != null) {
			for (int i = 0; i < opciones.size(); i++) {
				System.out.println("  [" + i + "]: '" + opciones.get(i) + "'");
			}
		}

		System.out.println("Es válida: " + isValid());
		System.out.println("========================");
	}
}
