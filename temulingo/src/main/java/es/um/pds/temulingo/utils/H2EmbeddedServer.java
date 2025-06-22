package es.um.pds.temulingo.utils;

import java.sql.SQLException;

import org.h2.tools.Server;

/**
 * Clase utilitaria para iniciar y detener un servidor web embebido de H2.
 */
public class H2EmbeddedServer {
	private static final String DEFAULT_PORT = "8082";

	private static Server webServer;

	public static void start() {
		try {
			webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", DEFAULT_PORT).start();
		} catch (SQLException e) {
			throw new RuntimeException("Error iniciando la consola H2", e);
		}
	}

	public static void stop() {
		if (webServer != null) {
			webServer.stop();
		}
	}
}
