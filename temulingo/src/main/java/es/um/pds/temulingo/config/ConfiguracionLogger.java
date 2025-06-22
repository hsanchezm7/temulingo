package es.um.pds.temulingo.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Configura el logger global para que imprima mensajes con un formato común.
 * <p>
 * El formato incluye la fecha y hora, nivel de log y mensaje. Esta
 * configuración se aplica a la consola (System.out).
 * </p>
 *
 * Uso típico: llamar a {@code ConfiguracionLogger.configurar()} al inicio de la
 * aplicación.
 */
public class ConfiguracionLogger {

	// Patrón de fecha para timestamps en los logs
	private static final String FORMATO_FECHA = ConfiguracionTemulingo.FORMATO_FECHA_HORA;

	// Formato de salida del log: fecha [nivel] mensaje + salto de línea
	private static final String FORMATO_LOG = "%s [%s] %s%n";

	private static final Level NIVEL_LOG = Level.INFO;

	/**
	 * Configura el logger raíz para usar un formateador simple que incluye la
	 * fecha, hora, nivel y mensaje.
	 */
	public static void inicializar() {
		Logger rootLogger = LogManager.getLogManager().getLogger("");
		Handler[] handlers = rootLogger.getHandlers();

		Formatter formateador = new Formatter() {
			private final SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);

			@Override
			public String format(LogRecord record) {
				String fecha = sdf.format(new Date(record.getMillis()));
				String nivel = record.getLevel().getName();
				String mensaje = formatMessage(record);
				return String.format(FORMATO_LOG, fecha, nivel, mensaje);
			}
		};

		for (Handler handler : handlers) {
			if (handler instanceof ConsoleHandler) {
				handler.setFormatter(formateador);
				handler.setLevel(NIVEL_LOG);
			}
		}

		rootLogger.setLevel(NIVEL_LOG);
	}
}
