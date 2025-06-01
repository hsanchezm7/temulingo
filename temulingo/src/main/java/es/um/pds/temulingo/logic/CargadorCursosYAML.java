package es.um.pds.temulingo.logic;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CargadorCursosYAML {
	public static Curso parseCourseFromResources(String resourcePath) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))

		// Get the resource as InputStream
		try (InputStream inputStream = CargadorCursosJSON.class.getResourceAsStream(resourcePath)) {

			if (inputStream == null) {
				throw new IOException("Resource not found: " + resourcePath);
			}

			return objectMapper.readValue(inputStream, Curso.class);
		}
	}
}