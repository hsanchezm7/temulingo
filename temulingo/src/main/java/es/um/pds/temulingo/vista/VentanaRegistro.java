package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

import com.toedter.calendar.JDateChooser;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.config.ConfiguracionUI;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.exception.ExcepcionRegistroInvalido;

/**
 * Ventana de registro para la aplicación Temulingo. Permite registrar nuevos
 * usuarios con su nombre, email, username, contraseña y fecha de nacimiento,
 */
public class VentanaRegistro extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(VentanaRegistro.class.getName());

	private static final String FUNCION = "Registro";
	private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE + " - " + FUNCION;

	private static final String PLACEHOLDER_NOMBRE = "Nombre completo";
	private static final String PLACEHOLDER_EMAIL = "Correo electrónico";
	private static final String PLACEHOLDER_USERNAME = "Nombre de usuario";
	private static final String PLACEHOLDER_PASSWORD = "Contraseña";

	private JTextField campoNombre;
	private JTextField campoEmail;
	private JTextField campoUsername;
	private JPasswordField campoPassword;
	private JDateChooser campoFechaNacimiento;
	private JButton btnMostrarPassword;
	private boolean passwordVisible = false;
	private ImageIcon iconoOjoVisible;
	private ImageIcon iconoOjoOculto;

	public VentanaRegistro() {
		initComponents();
	}

	public void initComponents() {
		setTitle(NOMBRE_VENTANA);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

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
		setResizable(false);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		SwingUtilities.invokeLater(() -> this.requestFocusInWindow());

		LOGGER.info("Ventana de registro inicializada correctamente");
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
				.setBorder(new TitledBorder(null, "  Registro  ", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panelCentro.add(panelWrapperForm, BorderLayout.CENTER);
		panelWrapperForm.setLayout(new BorderLayout(0, 0));

		JPanel panelRegistro = new JPanel();
		panelRegistro.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelWrapperForm.add(panelRegistro, BorderLayout.CENTER);
		GridBagLayout gbl_panelRegistro = new GridBagLayout();
		gbl_panelRegistro.columnWidths = new int[] { 0, 0, 0 };
		gbl_panelRegistro.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelRegistro.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gbl_panelRegistro.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		panelRegistro.setLayout(gbl_panelRegistro);

		// Campo Nombre
		String rutaIdLogo = ConfiguracionTemulingo.getRutaIcono("id.icono");
		ImageIcon idLogo = new ImageIcon(getClass().getResource(rutaIdLogo));
		JLabel lblNombre = new JLabel(idLogo);
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.EAST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 0;
		gbc_lblNombre.gridy = 0;
		panelRegistro.add(lblNombre, gbc_lblNombre);

		campoNombre = new JTextField();
		configurarPlaceholder(campoNombre, PLACEHOLDER_NOMBRE);
		GridBagConstraints gbc_campoNombre = new GridBagConstraints();
		gbc_campoNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_campoNombre.insets = new Insets(0, 0, 5, 0);
		gbc_campoNombre.gridx = 1;
		gbc_campoNombre.gridy = 0;
		gbc_campoNombre.gridwidth = 2;
		panelRegistro.add(campoNombre, gbc_campoNombre);

		// Campo Email
		String rutaEmailLogo = ConfiguracionTemulingo.getRutaIcono("email.icono");
		ImageIcon emailLogo = new ImageIcon(getClass().getResource(rutaEmailLogo));
		JLabel lblEmail = new JLabel(emailLogo);
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 1;
		panelRegistro.add(lblEmail, gbc_lblEmail);

		campoEmail = new JTextField();
		configurarPlaceholder(campoEmail, PLACEHOLDER_EMAIL);
		GridBagConstraints gbc_campoEmail = new GridBagConstraints();
		gbc_campoEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_campoEmail.insets = new Insets(0, 0, 5, 0);
		gbc_campoEmail.gridx = 1;
		gbc_campoEmail.gridy = 1;
		gbc_campoEmail.gridwidth = 2;
		panelRegistro.add(campoEmail, gbc_campoEmail);

		// Campo Username
		String rutaUsernameLogo = ConfiguracionTemulingo.getRutaIcono("user.icono");
		ImageIcon usernameLogo = new ImageIcon(getClass().getResource(rutaUsernameLogo));
		JLabel lblUsername = new JLabel(usernameLogo);
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 2;
		panelRegistro.add(lblUsername, gbc_lblUsername);

		campoUsername = new JTextField();
		configurarPlaceholder(campoUsername, PLACEHOLDER_USERNAME);
		GridBagConstraints gbc_campoUsername = new GridBagConstraints();
		gbc_campoUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_campoUsername.insets = new Insets(0, 0, 5, 0);
		gbc_campoUsername.gridx = 1;
		gbc_campoUsername.gridy = 2;
		gbc_campoUsername.gridwidth = 2;
		panelRegistro.add(campoUsername, gbc_campoUsername);

		// Campo Contraseña
		String rutaPasswordLogo = ConfiguracionTemulingo.getRutaIcono("key.icono");
		ImageIcon passwordLogo = new ImageIcon(getClass().getResource(rutaPasswordLogo));
		JLabel lblPassword = new JLabel(passwordLogo);
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 3;
		panelRegistro.add(lblPassword, gbc_lblPassword);

		campoPassword = new JPasswordField();
		configurarPlaceholder(campoPassword, PLACEHOLDER_PASSWORD);
		GridBagConstraints gbc_campoPassword = new GridBagConstraints();
		gbc_campoPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_campoPassword.insets = new Insets(0, 0, 5, 5);
		gbc_campoPassword.gridx = 1;
		gbc_campoPassword.gridy = 3;
		panelRegistro.add(campoPassword, gbc_campoPassword);

		btnMostrarPassword = new JButton();
		btnMostrarPassword.setIcon(iconoOjoOculto);
		btnMostrarPassword.setToolTipText("Mostrar contraseña");
		btnMostrarPassword.addActionListener(e -> alternarVisibilidadPassword());
		GridBagConstraints gbc_btnMostrarPassword = new GridBagConstraints();
		gbc_btnMostrarPassword.insets = new Insets(0, 0, 5, 0);
		gbc_btnMostrarPassword.gridx = 2;
		gbc_btnMostrarPassword.gridy = 3;
		panelRegistro.add(btnMostrarPassword, gbc_btnMostrarPassword);

		// Campo Fecha de nacimiento
		String rutaEventoLogo = ConfiguracionTemulingo.getRutaIcono("event.icono");
		ImageIcon eventoLogo = new ImageIcon(getClass().getResource(rutaEventoLogo));
		JLabel lblFechaNacimiento = new JLabel(eventoLogo);
		GridBagConstraints gbc_lblFechaNacimiento = new GridBagConstraints();
		gbc_lblFechaNacimiento.anchor = GridBagConstraints.EAST;
		gbc_lblFechaNacimiento.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaNacimiento.gridx = 0;
		gbc_lblFechaNacimiento.gridy = 4;
		panelRegistro.add(lblFechaNacimiento, gbc_lblFechaNacimiento);

		campoFechaNacimiento = new JDateChooser("dd/MM/yyyy", "##/##/##", '_');
		campoFechaNacimiento.setDateFormatString("dd/MM/yyyy");
		GridBagConstraints gbc_campoFechaNacimiento = new GridBagConstraints();
		gbc_campoFechaNacimiento.fill = GridBagConstraints.HORIZONTAL;
		gbc_campoFechaNacimiento.insets = new Insets(0, 0, 5, 0);
		gbc_campoFechaNacimiento.gridx = 1;
		gbc_campoFechaNacimiento.gridy = 4;
		gbc_campoFechaNacimiento.gridwidth = 2;
		panelRegistro.add(campoFechaNacimiento, gbc_campoFechaNacimiento);

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
			// Fallback en caso de que no se puedan cargar los iconos
			iconoOjoVisible = null;
			iconoOjoOculto = null;
		}
	}

	public JPanel crearPanelBotones() {
		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(new EmptyBorder(5, 10, 5, 10));
		panelBotones.setLayout(new BorderLayout(0, 0));

		JPanel panelBotonRegistro = new JPanel();
		panelBotones.add(panelBotonRegistro, BorderLayout.WEST);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> gestionarVolver());
		btnVolver.setVerticalAlignment(SwingConstants.BOTTOM);
		panelBotonRegistro.add(btnVolver);

		JPanel panelOtrosBotones = new JPanel();
		panelBotones.add(panelOtrosBotones, BorderLayout.EAST);

		JButton btnSalir = new JButton("Salir");
		panelOtrosBotones.add(btnSalir);
		btnSalir.setVerticalAlignment(SwingConstants.BOTTOM);
		btnSalir.addActionListener(e -> gestionarSalida());

		JButton btnRegistro = new JButton("Crear cuenta");
		btnRegistro.setVerticalAlignment(SwingConstants.BOTTOM);
		btnRegistro.addActionListener(e -> gestionarRegistro());
		panelOtrosBotones.add(btnRegistro);

		getRootPane().setDefaultButton(btnRegistro);

		return panelBotones;
	}

	private void gestionarVolver() {
		LOGGER.info("Usuario solicitó volver a la ventana de login");

		VentanaLogin ventanaLogin = new VentanaLogin();
		ventanaLogin.setVisible(true);
		this.dispose();
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
		String nombreText = campoNombre.getText().trim();
		String emailText = campoEmail.getText().trim();
		String usernameText = campoUsername.getText().trim();
		char[] passwordChars = campoPassword.getPassword();
		String passwordText = new String(passwordChars);

		Date fecha = campoFechaNacimiento.getDate();

		boolean nombreVacio = nombreText.isEmpty() || nombreText.equals(PLACEHOLDER_NOMBRE);
		boolean emailVacio = emailText.isEmpty() || emailText.equals(PLACEHOLDER_EMAIL);
		boolean usernameVacio = usernameText.isEmpty() || usernameText.equals(PLACEHOLDER_USERNAME);
		boolean passwordVacio = passwordChars.length == 0 || passwordText.equals(PLACEHOLDER_PASSWORD);
		boolean fechaVacia = (fecha == null);

		if (nombreVacio || emailVacio || usernameVacio || passwordVacio || fechaVacia) {
			LOGGER.warning("Intento de registro con campos vacíos");
			JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos del formulario", "Campos vacíos",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		return true;
	}

	private void gestionarRegistro() {
		LOGGER.info("Iniciando proceso de registro");

		if (!comprobarCampos()) {
			return;
		}

		String nombre = campoNombre.getText().trim();
		String email = campoEmail.getText().trim();
		String username = campoUsername.getText().trim();
		char[] passwordChars = campoPassword.getPassword();
		String password = new String(passwordChars);
		java.util.Arrays.fill(passwordChars, '\0'); // Limpiar por seguridad

		LocalDate fechaNacim = null;
		try {
			Date fecha = campoFechaNacimiento.getDate();
			if (fecha != null) {
				fechaNacim = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error al obtener la fecha de nacimiento", e);
		}

		// Validar placeholders vacíos
		if (nombre.equals(PLACEHOLDER_NOMBRE)) {
			nombre = "";
		}
		if (email.equals(PLACEHOLDER_EMAIL)) {
			email = "";
		}
		if (username.equals(PLACEHOLDER_USERNAME)) {
			username = "";
		}
		if (password.equals(PLACEHOLDER_PASSWORD)) {
			password = "";
		}

		try {
			ControladorTemulingo controlador = ControladorTemulingo.getInstance();
			boolean registrado = controlador.registrarUsuario(nombre, email, username, password, fechaNacim);

			if (registrado) {
				LOGGER.info("Registro exitoso para usuario: " + username);

				JOptionPane.showMessageDialog(this,
						"¡Cuenta creada con éxito! Bienvenido a Temulingo " + username + " ", "Registro exitoso",
						JOptionPane.INFORMATION_MESSAGE);

				VentanaLogin ventanaLogin = new VentanaLogin();
				ventanaLogin.setVisible(true);
				this.dispose();
			}
		} catch (NullPointerException e) {
			LOGGER.log(Level.WARNING, "Error de campos nulos durante el registro", e);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Campos vacíos", JOptionPane.WARNING_MESSAGE);
		} catch (ExcepcionRegistroInvalido e) {
			LOGGER.log(Level.WARNING, "Registro inválido para usuario: " + username, e);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Registro inválido", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error inesperado durante el registro", e);
			JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
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
