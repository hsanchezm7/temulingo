package es.um.pds.temulingo.config;

public class ConfiguracionTemulingo {

	public static final String NOMBRE = "Temulingo";
	public static final String VERSION = "1.0.0";

	public static final String DESCRIPCION = "Aplicación de aprendizaje de idiomas creada en Java 17";
	public static final String AUTOR = "Facultad de Informática - Universidad de Murcia";
	public static final String ANO = "2025";

	public static final String URL = "https://github.com/hsanchezm7/learning-app";

	public static final String FORMATO_FECHA_HORA = "dd-MM-yyyy HH:mm:ss";
	public static final String PERSISTENCE_UNIT_NAME = "temulingo-persistence-unit";

	public static String getRutaIcono(String clave) {
		return ConfiguracionImagenes.getRutaIcono(clave);
	}
}
