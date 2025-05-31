package es.um.pds.temulingo;

import java.sql.SQLException;

import org.h2.tools.Server;

public class H2EmbeddedServer {
	private static Server webServer;

	public static void start() {
		try {
			webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
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
