package es.um.pds.temulingo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatIntelliJLaf;

import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.utils.H2EmbeddedServer;
import es.um.pds.temulingo.vista.VentanaMain;

public class App {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Hello World!");

		try {
			UIManager.setLookAndFeel(new FlatIntelliJLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}

		H2EmbeddedServer.start();

		// Forzar inicialización del controlador
		ControladorTemulingo.getInstance();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VentanaMain ventanaMain = new VentanaMain();
				ventanaMain.setVisible(true);
			}
		});
	}
}
