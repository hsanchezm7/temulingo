package es.um.pds.temulingo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.SwingUtilities;

import es.um.pds.temulingo.config.ConfiguracionUI;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.utils.H2EmbeddedServer;
import es.um.pds.temulingo.vista.VentanaMain;

public class App {

	public static void main(String[] args) throws InterruptedException, IOException {
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
		controlador.registrarUsuario("Juan Pérez", "juan@example.com", "juanp", "123456", LocalDate.of(1990, 5, 20));

		try {
			boolean loginExitoso = controlador.iniciarSesionConEmail("juan@example.com", "123456");
			System.out.println("¿Login exitoso?: " + loginExitoso);
		} catch (Exception e) {
			System.out.println("Error al iniciar sesión: " + e.getMessage());
		}

		// Ruta del archivo
		String ruta = "/home/hugo/repo/learning-app/temulingo/src/main/resources/libreria-cursos/curso_aleman_basico.yaml";

		// Crear objeto File con la ruta
		File archivo = new File(ruta);

		controlador.importarCursoDesdeFichero(archivo);

//		// Iniciar curso
//        controlador.iniciarCursoTest();
//        System.out.println("Curso iniciado...\n");
//
//        // Loop de preguntas
//        while (!controlador.esCursoActualCompletado()) {
//            Pregunta pregunta = controlador.getSiguientePregunta();
//            System.out.println("Pregunta: " + pregunta.getEnunciado());
//
//            String respuesta = "test";
//            boolean correcta = controlador.resolverPregunta(pregunta, respuesta);
//            System.out.println("Respuesta dada: \"" + respuesta + "\" - ¿Correcta?: " + correcta + "\n");
//        }
//
//        System.out.println("✅ Curso completado.");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VentanaMain ventanaMain = new VentanaMain();
				ventanaMain.setVisible(true);
			}
		});
	}
}
