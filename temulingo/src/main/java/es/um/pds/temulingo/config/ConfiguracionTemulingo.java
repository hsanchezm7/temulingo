package es.um.pds.temulingo.config;

import java.io.IOException;
import java.util.Properties;

public class ConfiguracionTemulingo {

	public static final String NOMBRE_APP = "Temulingo";
	public static final String VERSION = "1.0.0";
	public static final String URL = "https://github.com/hsanchezm7/learning-app";
	public static final String NIVEL_LOGGING = "INFO";
	public static final String FORMATO_FECHA = "dd/MM/yyyy HH:mm:ss";
	public static final String PERSISTENCE_UNIT_NAME = "temulingo-persistence-unit";
	private static final String RUTA_IMAGENES = "/config/imagenes.properties";

	private static final Properties imagenes = new Properties();

	static {
		try {
			imagenes.load(ConfiguracionTemulingo.class.getResourceAsStream(RUTA_IMAGENES));
		} catch (IOException | NullPointerException e) {
			System.err.println("No se pudo cargar el archivo de imágenes: " + e.getMessage());
		}
	}

	public static String getRutaIcono(String clave) {
		return imagenes.getProperty(clave);
	}

}
