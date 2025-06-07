package es.um.pds.temulingo;

import javax.swing.SwingUtilities;

import es.um.pds.temulingo.config.ConfiguracionUI;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.utils.H2EmbeddedServer;
import es.um.pds.temulingo.vista.VentanaMain;

public class App {

	public static void main(String[] args) throws InterruptedException {		
		ConfiguracionUI.inicializar();

		// Consola web H2 para el acceso a la BBDD: http://localhost:8082
		// url: jdbc:h2:./data/temulingo_bbdd
		// user: sa
		// password:
		// TODO: Eliminar una vez termine el desarrollo
		H2EmbeddedServer.start();

		// Forzar inicialización del controlador
		ControladorTemulingo controlador = ControladorTemulingo.getInstance();

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
