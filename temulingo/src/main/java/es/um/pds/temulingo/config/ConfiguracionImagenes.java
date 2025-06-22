package es.um.pds.temulingo.config;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase utilitaria encargada de cargar y proporcionar rutas de iconos desde un
 * archivo de propiedades.
 * 
 * <p>
 * Al iniciarse, carga el archivo {@code /config/imagenes.properties} desde el
 * classpath, y expone un método estático para recuperar rutas de iconos
 * mediante claves.
 * </p>
 */
public class ConfiguracionImagenes {

	private static final String RUTA_IMAGENES = "/config/imagenes.properties";
	private static final Properties imagenes = new Properties();

	private static final Logger LOGGER = Logger.getLogger(ConfiguracionImagenes.class.getName());

	static {
		try {
			imagenes.load(ConfiguracionImagenes.class.getResourceAsStream(RUTA_IMAGENES));
		} catch (IOException | NullPointerException e) {
			LOGGER.log(Level.SEVERE, "No se pudo cargar el archivo de imágenes: " + RUTA_IMAGENES, e);
		}
	}

	/**
	 * Devuelve la ruta del icono asociada a la clave dada, según lo especificado en
	 * el archivo de propiedades.
	 *
	 * @param clave clave del icono (por ejemplo, "icono.login")
	 * @return ruta del icono relativa al classpath, o {@code null} si no se
	 *         encuentra
	 */
	public static String getRutaIcono(String clave) {
		return imagenes.getProperty(clave);
	}
}