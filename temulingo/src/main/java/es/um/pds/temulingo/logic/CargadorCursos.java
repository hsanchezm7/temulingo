package es.um.pds.temulingo.logic;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Clase utilitaria para cargar cursos desde archivos en formato JSON o YAML.
 * 
 * Utiliza Jackson para deserializar archivos de configuración de cursos,
 * proporcionando funcionalidades de debug y validación durante el proceso
 * de carga para asegurar la integridad de los datos.
 * 
 * Características principales:
 * - Soporte para múltiples formatos (JSON, YAML)
 * - Validación de estructura de datos
 * - Logging detallado para debug
 * - Manejo robusto de errores
 */
public class CargadorCursos {

	/**
     * Enumeración que define los formatos de archivo soportados.
     * Cada formato implementa su propia factoría de Jackson.
     */
	public enum Formato {
		JSON {
			@Override
			public JsonFactory createFactory() {
				return new JsonFactory();
			}
		},
		YAML {
			@Override
			public JsonFactory createFactory() {
				return new YAMLFactory();
			}
		};

		public abstract JsonFactory createFactory();
	}

	/**
     * Parsea un archivo y lo deserializa en un objeto del tipo especificado.
     * 
     * Este método realiza las siguientes operaciones:
     * 1. Valida la existencia del archivo y parámetros
     * 2. Configura el ObjectMapper con las opciones apropiadas
     * 3. Realiza un parseo previo para debug y validación
     * 4. Deserializa el archivo al tipo de objeto solicitado
     * 5. Ejecuta validaciones post-deserialización
     * 
     * @param <T> Tipo genérico del objeto a deserializar
     * @param fichero Archivo a parsear (debe existir y ser accesible)
     * @param tipoClase Clase del tipo T para la deserialización
     * @param formato Formato del archivo (JSON o YAML)
     * @return Instancia del tipo T con los datos del archivo
     * @throws IOException Si hay problemas de acceso al archivo o parseo
     * @throws IllegalArgumentException Si los parámetros son inválidos
     */
	public static <T> T parsearDesdeFichero(File fichero, Class<T> tipoClase, Formato formato) throws IOException {
		if (fichero == null || !fichero.exists()) {
			throw new IOException("Archivo no encontrado: " + (fichero != null ? fichero.getAbsolutePath() : "null"));
		}

		if (formato == null) {
			throw new IllegalArgumentException("Formato no especificado.");
		}

		ObjectMapper objectMapper = new ObjectMapper(formato.createFactory());
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// Configuraciones para robustez en el parseo
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		System.out.println("=== INICIO PARSEO ARCHIVO ===");
		System.out.println("Archivo: " + fichero.getAbsolutePath());
		System.out.println("Formato: " + formato);
		System.out.println("Clase objetivo: " + tipoClase.getSimpleName());

		try {
			// DEBUGGING: Leer primero como JsonNode para inspeccionar
			JsonNode rootNode = objectMapper.readTree(fichero);
			System.out.println("JSON parseado exitosamente");

			// Verificar estructura de bloques y preguntas
			if (rootNode.has("bloques")) {
				JsonNode bloquesNode = rootNode.get("bloques");
				System.out.println("Número de bloques encontrados: " + bloquesNode.size());

				for (int i = 0; i < bloquesNode.size(); i++) {
					JsonNode bloqueNode = bloquesNode.get(i);
					System.out.println("Bloque " + i + ": "
							+ (bloqueNode.has("nombre") ? bloqueNode.get("nombre").asText() : "Sin nombre"));

					if (bloqueNode.has("preguntas")) {
						JsonNode preguntasNode = bloqueNode.get("preguntas");
						System.out.println("  Preguntas en bloque " + i + ": " + preguntasNode.size());

						for (int j = 0; j < preguntasNode.size(); j++) {
							JsonNode preguntaNode = preguntasNode.get(j);
							String tipo = preguntaNode.has("tipo") ? preguntaNode.get("tipo").asText() : "DESCONOCIDO";
							String enunciado = preguntaNode.has("enunciado") ? preguntaNode.get("enunciado").asText()
									: "Sin enunciado";

							System.out.println("    Pregunta " + j + " - Tipo: " + tipo);
							System.out.println("    Enunciado: " + enunciado);

							if ("TEST".equals(tipo) && preguntaNode.has("opciones")) {
								JsonNode opcionesNode = preguntaNode.get("opciones");
								System.out.println("    Opciones encontradas: " + opcionesNode.size());
								for (int k = 0; k < opcionesNode.size(); k++) {
									System.out.println("      [" + k + "]: " + opcionesNode.get(k).asText());
								}

								if (preguntaNode.has("solucion")) {
									System.out.println("    Solución: " + preguntaNode.get("solucion").asText());
								}
							}
						}
					}
				}
			}

			System.out.println("=== INICIANDO DESERIALIZACIÓN ===");

			// Deserializar a objeto
			T resultado = objectMapper.readValue(fichero, tipoClase);

			System.out.println("=== DESERIALIZACIÓN COMPLETADA ===");

			// VALIDACIÓN POST-DESERIALIZACIÓN
			if (resultado instanceof Curso) {
				Curso curso = (Curso) resultado;
				System.out.println("Curso cargado: " + curso.getTitulo());
				System.out.println("Bloques en curso: " + (curso.getBloques() != null ? curso.getBloques().size() : 0));

				if (curso.getBloques() != null) {
					for (int i = 0; i < curso.getBloques().size(); i++) {
						Bloque bloque = curso.getBloques().get(i);
						System.out.println("Bloque " + i + ": " + bloque.getNombre());
						System.out.println(
								"  Preguntas: " + (bloque.getPreguntas() != null ? bloque.getPreguntas().size() : 0));

						if (bloque.getPreguntas() != null) {
							for (int j = 0; j < bloque.getPreguntas().size(); j++) {
								Pregunta pregunta = bloque.getPreguntas().get(j);
								System.out.println("    Pregunta " + j + ": " + pregunta.getClass().getSimpleName());
								System.out.println("    Enunciado: " + pregunta.getEnunciado());

								if (pregunta instanceof PreguntaTest) {
									PreguntaTest preguntaTest = (PreguntaTest) pregunta;
									System.out.println("    Opciones cargadas: "
											+ (preguntaTest.getOpciones() != null ? preguntaTest.getOpciones().size()
													: 0));
									System.out.println("    Solución: " + preguntaTest.getSolucion());

									// Validar pregunta
									preguntaTest.isValid();
								}
							}
						}
					}
				}
			}

			System.out.println("=== PARSEO COMPLETADO EXITOSAMENTE ===");
			return resultado;

		} catch (Exception e) {
			System.err.println("=== ERROR DURANTE PARSEO ===");
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
			throw new IOException("Error al parsear archivo: " + e.getMessage(), e);
		}
	}
}
