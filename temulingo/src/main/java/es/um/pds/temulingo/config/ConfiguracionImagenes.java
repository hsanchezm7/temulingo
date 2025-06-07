package es.um.pds.temulingo.config;

import java.io.IOException;
import java.util.Properties;

public class ConfiguracionImagenes {

	private static final String RUTA_IMAGENES = "/config/imagenes.properties";
	private static final Properties imagenes = new Properties();

	static {
		try {
			imagenes.load(ConfiguracionImagenes.class.getResourceAsStream(RUTA_IMAGENES));
		} catch (IOException | NullPointerException e) {
			System.err.println("No se pudo cargar el archivo de imágenes: " + e.getMessage());
		}
	}

	public static String getRutaIcono(String clave) {
		return imagenes.getProperty(clave);
	}
}
