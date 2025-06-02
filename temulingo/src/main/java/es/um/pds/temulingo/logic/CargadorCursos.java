package es.um.pds.temulingo.logic;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CargadorCursos {

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

		return objectMapper.readValue(fichero, tipoClase);
	}
}
