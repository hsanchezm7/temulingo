package es.um.pds.temulingo.logic;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Clase utilitaria para cargar cursos desde archivos en formato JSON o YAML.
 * 
 * Utiliza Jackson para deserializar archivos de configuración de cursos,
 * proporcionando funcionalidades de debug y validación durante el proceso de
 * carga para asegurar la integridad de los datos.
 * 
 * Características principales: - Soporte para múltiples formatos (JSON, YAML) -
 * Validación de estructura de datos - Logging detallado para debug - Manejo
 * robusto de errores
 */
public class ParseadorCursos {

	private static final Logger LOGGER = Logger.getLogger(ParseadorCursos.class.getName());

	/**
	 * Enumeración que define los formatos de archivo soportados. Cada formato
	 * implementa su propia factoría de Jackson.
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
	 * @param <T>       Tipo genérico del objeto a deserializar
	 * @param fichero   Archivo a parsear (debe existir y ser accesible)
	 * @param tipoClase Clase del tipo T para la deserialización
	 * @param formato   Formato del archivo (JSON o YAML)
	 * @return Instancia del tipo T con los datos del archivo
	 * @throws IOException              Si hay problemas de acceso al archivo o
	 *                                  parseo
	 * @throws IllegalArgumentException Si los parámetros son inválidos
	 */
	public static <T> T parsearDesdeFichero(File fichero, Class<T> tipoClase, Formato formato) throws IOException {
		if (fichero == null || !fichero.exists()) {
			String mensaje = "Archivo no encontrado: " + (fichero != null ? fichero.getAbsolutePath() : "null");
			LOGGER.warning(mensaje);
			throw new IOException(mensaje);
		}

		if (formato == null) {
			LOGGER.warning("Intento de parseo sin especificar formato");
			throw new IllegalArgumentException("Formato no especificado.");
		}

		ObjectMapper objectMapper = new ObjectMapper(formato.createFactory());
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		return objectMapper.readValue(fichero, tipoClase);
	}

	/**
	 * Serializa un objeto y lo escribe en un archivo en el formato especificado.
	 * 
	 * @param <T>     Tipo genérico del objeto a serializar
	 * @param objeto  Objeto a serializar (no puede ser null)
	 * @param fichero Archivo de destino donde se escribirá el objeto
	 * @param formato Formato del archivo (JSON o YAML)
	 * @throws IOException              Si hay problemas al escribir en el archivo
	 * @throws IllegalArgumentException Si los parámetros son inválidos
	 */
	public static <T> void parsearAFichero(T objeto, File fichero, Formato formato) throws IOException {
		if (objeto == null) {
			LOGGER.warning("Intento de serializar objeto null");
			throw new IllegalArgumentException("El objeto a serializar no puede ser null.");
		}
		if (fichero == null) {
			LOGGER.warning("Intento de serializar a archivo null");
			throw new IllegalArgumentException("El archivo de destino no puede ser null.");
		}
		if (formato == null) {
			LOGGER.warning("Intento de serializar sin especificar formato");
			throw new IllegalArgumentException("El formato no especificado.");
		}

		ObjectMapper objectMapper = new ObjectMapper(formato.createFactory());
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		objectMapper.writeValue(fichero, objeto);
	}

}
