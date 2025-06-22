package es.um.pds.temulingo;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import es.um.pds.temulingo.config.ConfiguracionLogger;
import es.um.pds.temulingo.config.ConfiguracionUI;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.vista.VentanaLogin;

/**
 * Clase principal que inicia la aplicación Temulingo.
 * 
 * <p>
 * Realiza la configuración inicial de la interfaz de usuario, fuerza la
 * creación del controlador principal y lanza la ventana de inicio de sesión.
 * 
 * <p>
 * Durante el desarrollo, permite habilitar el servidor H2 embebido para pruebas
 * de base de datos.
 * </p>
 */
public class App {

	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {

		ConfiguracionLogger.inicializar();
		ConfiguracionUI.inicializar();

		LOGGER.info("Iniciando Temulingo...");

		// Solo para desarrollo
		// H2EmbeddedServer.start();

		// Forzar inicialización del controlador
		ControladorTemulingo.getInstance();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("Mostrando ventana de login.");
				VentanaLogin ventanaLogin = new VentanaLogin();
				ventanaLogin.setVisible(true);
			}
		});
	}
}
