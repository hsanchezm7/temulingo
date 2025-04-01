package es.um.pds.temulingo;

import es.um.pds.temulingo.vista.VentanaLogin;
import es.um.pds.temulingo.vista.VentanaRegistro;

/**
 * Clase que he hecho para ver como se ven las ventanas de Login y Registro
 */
public class AppPruebaVentanas {
    public static void main(String[] args) {
        // Configurar el look and feel del sistema (Basicamente pilla el look del sistema operativo del usuario
    	// y lo adapta a la aplicacion)
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar la aplicación con la ventana de login
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Crear la ventana de login primero (pero no se muestra)
                VentanaLogin ventanaLogin = new VentanaLogin();
                
                // Crear la ventana de registro y mostrarla
                VentanaRegistro ventanaRegistro = new VentanaRegistro(ventanaLogin);
                ventanaRegistro.setVisible(true);
            }
        });
    }
}
