package es.um.pds.temulingo;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatIntelliJLaf;

import es.um.pds.temulingo.vista.VentanaMain;

/**
 * Clase que he hecho para ver como se ven las ventanas de Login y Registro
 */
public class AppPruebaVentanas {
	public static void main(String[] args) {
		// Configurar el look and feel del sistema (Basicamente pilla el look del
		// sistema operativo del usuario
		// y lo adapta a la aplicacion)
		try {
			UIManager.setLookAndFeel(new FlatIntelliJLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Iniciar la aplicación con la ventana de login
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Crear la ventana de login primero (pero no se muestra)
				VentanaMain ventanaMain = new VentanaMain();

				ventanaMain.setVisible(true);
			}
		});
	}
}
