package es.um.pds.temulingo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.SwingUtilities;

import es.um.pds.temulingo.config.ConfiguracionUI;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.exception.ExcepcionCredencialesInvalidas;
import es.um.pds.temulingo.exception.ExcepcionRegistroInvalido;
import es.um.pds.temulingo.utils.H2EmbeddedServer;
import es.um.pds.temulingo.vista.VentanaLogin;
import es.um.pds.temulingo.vista.VentanaMain;

public class App {

	public static void main(String[] args) throws InterruptedException, IOException, NullPointerException, ExcepcionRegistroInvalido, ExcepcionCredencialesInvalidas {
		ConfiguracionUI.inicializar();

		// Consola web H2 para el acceso a la BBDD: http://localhost:8082
		// url: jdbc:h2:./data/temulingo_bbdd
		// user: sa
		// password:
		// TODO: Eliminar una vez termine el desarrollo
		H2EmbeddedServer.start();

		// Forzar inicialización del controlador
		ControladorTemulingo controlador = ControladorTemulingo.getInstance();

		// Intento de login
		//controlador.registrarUsuario("Juan Pérez", "juan@example.com", "juanp", "123456", LocalDate.of(1990, 5, 20));

		boolean loginExitoso = false;
		try {
			loginExitoso = controlador.iniciarSesion("juan@example.com", "123456");
			System.out.println("¿Login exitoso?: " + loginExitoso);
		} catch (Exception e) {
			System.out.println("Error al iniciar sesión: " + e.getMessage());
			
			// Si falla el login, intentar registrar el usuario
	        try {
	            controlador.registrarUsuario("Juan Pérez", "juan@example.com", "juanp", "123456", LocalDate.of(1990, 5, 20));
	            System.out.println("Usuario registrado correctamente");
	            
	            // Ahora intentar login de nuevo
	            loginExitoso = controlador.iniciarSesion("juan@example.com", "123456");
	            System.out.println("¿Login exitoso después del registro?: " + loginExitoso);
	        } catch (ExcepcionRegistroInvalido ex) {
	            System.out.println("Error al registrar usuario: " + ex.getMessage());
	        }
		}

		// Ruta del archivo
		String ruta = "src/main/resources/libreria-cursos/curso_aleman_basico.yaml";

		// Crear objeto File con la ruta
		File archivo = new File(ruta);

		controlador.importarCursoDesdeFichero(archivo);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VentanaLogin ventanaLogin = new VentanaLogin();
                ventanaLogin.setVisible(true);
			}
		});
	}
}
