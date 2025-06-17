package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.config.ConfiguracionUI;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.exception.ExcepcionCredencialesInvalidas;

/**
 * Ventana de login para nuestra aplicación Temulingo. Permite iniciar sesión
 * con username/email del usuario o abrir la ventana de registro para crear una
 * nueva cuenta.
 */
public class VentanaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(VentanaLogin.class.getName());

	private static final String FUNCION = "Login";
	private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE + " - " + FUNCION;

	private static final String PLACEHOLDER_USERNAME = "Nombre de usuario/email";
	private static final String PLACEHOLDER_PASSWORD = "Contraseña";

	private JTextField campoUsernameEmail;
	private JPasswordField campoPassword;
	private JButton btnMostrarPassword;
	private boolean passwordVisible = false;
	private ImageIcon iconoOjoVisible;
	private ImageIcon iconoOjoOculto;

	public VentanaLogin() {
		initComponents();
	}

	public void initComponents() {
		setTitle(NOMBRE_VENTANA);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));

		setIconImage(ConfiguracionUI.getIconoApp().getImage());

		// Cargar iconos para el botón de mostrar/ocultar contraseña
		cargarIconosPassword();

		JPanel panelLogo = crearPanelLogo();
		getContentPane().add(panelLogo, BorderLayout.NORTH);

		JPanel panelCentro = crearPanelFormulario();
		getContentPane().add(panelCentro, BorderLayout.CENTER);

		JPanel panelBotones = crearPanelBotones();
		getContentPane().add(panelBotones, BorderLayout.SOUTH);

		pack();
		setResizable(true);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		SwingUtilities.invokeLater(() -> this.requestFocusInWindow());

		LOGGER.info("Ventana de login inicializada correctamente");
	}

	public JPanel crearPanelLogo() {
		JPanel panelLogo = new JPanel();
		panelLogo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblAppchat = new JLabel("");
		String rutaIconoLogo = ConfiguracionTemulingo.getRutaIcono("banner-alt.icono");
		ImageIcon iconoLogo = new ImageIcon(getClass().getResource(rutaIconoLogo));
		lblAppchat.setIcon(iconoLogo);
		panelLogo.add(lblAppchat);

		return panelLogo;
	}

	public JPanel crearPanelFormulario() {
		JPanel panelCentro = new JPanel();
		panelCentro.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelCentro.setLayout(new BorderLayout(0, 0));

		JPanel panelWrapperForm = new JPanel();
		panelWrapperForm
				.setBorder(new TitledBorder(null, "  Login  ", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panelCentro.add(panelWrapperForm, BorderLayout.CENTER);
		panelWrapperForm.setLayout(new BorderLayout(0, 0));

		JPanel panelLogin = new JPanel();
		panelLogin.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelWrapperForm.add(panelLogin, BorderLayout.CENTER);
		GridBagLayout gbl_panelLogin = new GridBagLayout();
		gbl_panelLogin.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panelLogin.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panelLogin.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panelLogin.rowWeights = new double[] { 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		panelLogin.setLayout(gbl_panelLogin);

		// Campo Username/Email con placeholder
		String rutaUserLogo = ConfiguracionTemulingo.getRutaIcono("user.icono");
		ImageIcon userLogo = new ImageIcon(getClass().getResource(rutaUserLogo));
		JLabel lblUsernameEmail = new JLabel(userLogo);
		GridBagConstraints gbc_lblUsernameEmail = new GridBagConstraints();
		gbc_lblUsernameEmail.anchor = GridBagConstraints.EAST;
		gbc_lblUsernameEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsernameEmail.gridx = 0;
		gbc_lblUsernameEmail.gridy = 0;
		panelLogin.add(lblUsernameEmail, gbc_lblUsernameEmail);
		lblUsernameEmail.setHorizontalAlignment(SwingConstants.RIGHT);

		campoUsernameEmail = new JTextField();
		configurarPlaceholder(campoUsernameEmail, PLACEHOLDER_USERNAME);
		GridBagConstraints gbc_campoUsernameEmail = new GridBagConstraints();
		gbc_campoUsernameEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_campoUsernameEmail.insets = new Insets(0, 0, 5, 5);
		gbc_campoUsernameEmail.gridx = 1;
		gbc_campoUsernameEmail.gridy = 0;
		gbc_campoUsernameEmail.gridwidth = 2;
		panelLogin.add(campoUsernameEmail, gbc_campoUsernameEmail);

		// Campo Password con placeholder y botón mostrar/ocultar
		String rutaKeyLogo = ConfiguracionTemulingo.getRutaIcono("key.icono");
		ImageIcon keyLogo = new ImageIcon(getClass().getResource(rutaKeyLogo));
		JLabel lblPassword = new JLabel(keyLogo);
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		panelLogin.add(lblPassword, gbc_lblPassword);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);

		campoPassword = new JPasswordField();
		configurarPlaceholder(campoPassword, PLACEHOLDER_PASSWORD);
		GridBagConstraints gbc_campoPassword = new GridBagConstraints();
		gbc_campoPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_campoPassword.insets = new Insets(0, 0, 5, 5);
		gbc_campoPassword.gridx = 1;
		gbc_campoPassword.gridy = 1;
		panelLogin.add(campoPassword, gbc_campoPassword);

		// Botón mostrar/ocultar contraseña
		btnMostrarPassword = new JButton();
		btnMostrarPassword.setIcon(iconoOjoOculto);
		btnMostrarPassword.setToolTipText("Mostrar contraseña");
		btnMostrarPassword.addActionListener(e -> alternarVisibilidadPassword());
		GridBagConstraints gbc_btnMostrarPassword = new GridBagConstraints();
		gbc_btnMostrarPassword.insets = new Insets(0, 0, 5, 5);
		gbc_btnMostrarPassword.gridx = 2;
		gbc_btnMostrarPassword.gridy = 1;
		panelLogin.add(btnMostrarPassword, gbc_btnMostrarPassword);

		return panelCentro;
	}

	/**
	 * Carga los iconos para el botón de mostrar/ocultar contraseña
	 */
	private void cargarIconosPassword() {
		try {
			String rutaIconoVisible = ConfiguracionTemulingo.getRutaIcono("view.icono");
			iconoOjoVisible = new ImageIcon(getClass().getResource(rutaIconoVisible));

			String rutaIconoOculto = ConfiguracionTemulingo.getRutaIcono("hide.icono");
			iconoOjoOculto = new ImageIcon(getClass().getResource(rutaIconoOculto));

			LOGGER.fine("Iconos de contraseña cargados correctamente");
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error al cargar iconos de contraseña, usando texto por defecto", e);
		}
	}

	public JPanel crearPanelBotones() {
		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(new EmptyBorder(5, 10, 5, 10));
		panelBotones.setLayout(new BorderLayout(0, 0));

		JPanel panelBotonRegistro = new JPanel();
		panelBotones.add(panelBotonRegistro, BorderLayout.WEST);

		JButton btnCrearCuenta = new JButton("Crear cuenta");
		btnCrearCuenta.addActionListener(e -> gestionarRegistro());
		btnCrearCuenta.setVerticalAlignment(SwingConstants.BOTTOM);
		panelBotonRegistro.add(btnCrearCuenta);

		JPanel panelOtrosBotones = new JPanel();
		panelBotones.add(panelOtrosBotones, BorderLayout.EAST);

		JButton btnSalir = new JButton("Salir");
		panelOtrosBotones.add(btnSalir);
		btnSalir.setVerticalAlignment(SwingConstants.BOTTOM);
		btnSalir.addActionListener(e -> gestionarSalida());

		JButton btnLogin = new JButton("Login");
		btnLogin.setVerticalAlignment(SwingConstants.BOTTOM);
		btnLogin.addActionListener(e -> gestionarLogin());
		panelOtrosBotones.add(btnLogin);

		getRootPane().setDefaultButton(btnLogin);

		return panelBotones;
	}

	private void gestionarRegistro() {
		LOGGER.info("Navegando a ventana de registro");
		VentanaRegistro registerFrame = new VentanaRegistro();
		registerFrame.setVisible(true);
		this.setVisible(false);
	}

	private void gestionarSalida() {
		LOGGER.info("Usuario solicitó salir de la aplicación");
		int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas salir?", "Confirmar salida",
				JOptionPane.YES_NO_OPTION);

		if (respuesta == JOptionPane.YES_OPTION) {
			LOGGER.info("Cerrando aplicación por petición del usuario");
			System.exit(0);
		} else {
			LOGGER.info("Usuario canceló la salida");
		}
	}

	/**
	 * Verifica si los campos no están vacíos y no contienen solo placeholders
	 * 
	 * @return true si los campos están correctamente llenos
	 */
	private boolean comprobarCampos() {
		String usernameText = campoUsernameEmail.getText().trim();
		char[] passwordChars = campoPassword.getPassword();
		String passwordText = new String(passwordChars);

		boolean usernameVacio = usernameText.isEmpty() || usernameText.equals(PLACEHOLDER_USERNAME);
		boolean passwordVacio = passwordChars.length == 0 || passwordText.equals(PLACEHOLDER_PASSWORD);

		if (usernameVacio || passwordVacio) {
			LOGGER.warning("Intento de login con campos vacíos");
			JOptionPane.showMessageDialog(this,
					"Por favor, introduce un nombre de usuario/email y contraseña correctos.", "Campos vacíos",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		return true;
	}

	private void gestionarLogin() {
		LOGGER.info("Iniciando proceso de login");

		if (!comprobarCampos()) {
			return;
		}

		String input = campoUsernameEmail.getText().trim();
		char[] passwordChars = campoPassword.getPassword();
		String password = new String(passwordChars);

		// Limpiar el array de contraseña por seguridad
		java.util.Arrays.fill(passwordChars, '\0');

		if (input.equals(PLACEHOLDER_USERNAME)) {
			input = "";
		}
		if (password.equals(PLACEHOLDER_PASSWORD)) {
			password = "";
		}

		try {
			ControladorTemulingo controlador = ControladorTemulingo.getInstance();
			boolean login = controlador.iniciarSesion(input, password);

			if (login) {
				String username = controlador.getUsuarioActual().getUsername();
				LOGGER.info("Login exitoso para usuario: " + username);

				JOptionPane.showMessageDialog(this, "¡Bienvenido, " + username + "!", "Inicio exitoso",
						JOptionPane.INFORMATION_MESSAGE);

				VentanaMain ventanaMain = new VentanaMain();
				ventanaMain.setVisible(true);
				this.dispose();
			}
		} catch (NullPointerException e) {
			LOGGER.log(Level.WARNING, "Error de campos nulos durante el login", e);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Campos vacíos", JOptionPane.WARNING_MESSAGE);
		} catch (ExcepcionCredencialesInvalidas e) {
			LOGGER.log(Level.WARNING, "Credenciales inválidas para usuario: " + input, e);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error inesperado durante el login", e);
			JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			// Limpiar campo de contraseña por seguridad
			campoPassword.setText("");
		}
	}

	/**
	 * Configura el placeholder para un campo de texto
	 * 
	 * @param campo       Campo al que añadir el placeholder
	 * @param placeholder Texto del placeholder
	 */
	private void configurarPlaceholder(JTextField campo, String placeholder) {
		campo.setText(placeholder);
		campo.setForeground(Color.GRAY);

		campo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent evt) {
				if (campo.getText().equals(placeholder)) {
					campo.setText("");
					campo.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent evt) {
				if (campo.getText().isEmpty()) {
					campo.setText(placeholder);
					campo.setForeground(Color.GRAY);
				}
			}
		});
	}

	/**
	 * Alterna la visibilidad de la contraseña
	 */
	private void alternarVisibilidadPassword() {
		if (passwordVisible) {
			campoPassword.setEchoChar('•');
			btnMostrarPassword.setIcon(iconoOjoOculto);
			btnMostrarPassword.setToolTipText("Mostrar contraseña");
			passwordVisible = false;
			LOGGER.fine("Contraseña ocultada");
		} else {
			campoPassword.setEchoChar((char) 0);
			btnMostrarPassword.setIcon(iconoOjoVisible);
			btnMostrarPassword.setToolTipText("Ocultar contraseña");
			passwordVisible = true;
			LOGGER.fine("Contraseña mostrada");
		}
	}
}