package es.um.pds.temulingo.config;

/**
 * Configuración general de la aplicación Temulingo.
 * 
 * <p>
 * Esta clase centraliza valores constantes como el nombre de la aplicación,
 * versión, autoría, formato de fecha y configuración de la unidad de
 * persistencia JPA. También proporciona acceso a recursos visuales mediante
 * claves de configuración.
 * </p>
 */
public class ConfiguracionTemulingo {

	public static final String NOMBRE = "Temulingo";
	public static final String VERSION = "1.0.0";

	public static final String DESCRIPCION = "Aplicación de aprendizaje creada en Java 17";
	public static final String AUTOR = "Facultad de Informática - Universidad de Murcia";
	public static final String ANO = "2025";

	public static final String URL = "https://github.com/hsanchezm7/temulingo";

	public static final String FORMATO_FECHA_HORA = "dd-MM-yyyy HH:mm:ss";
	public static final String PERSISTENCE_UNIT_NAME = "temulingo-persistence-unit";

}
