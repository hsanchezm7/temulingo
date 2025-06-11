package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import es.um.pds.temulingo.controlador.ControladorTemulingo;

/**
 * Ventana de login para nuestra aplicación Temulingo. Permite iniciar sesión
 * con username del usuario o ir a la ventana de registro (si no se tiene cuenta
 * todavía).
 */
public class VentanaLogin extends JFrame {

	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton registerButton;

	// Constantes para el diseño
	private final Color PRIMARY_COLOR = new Color(25, 118, 210); // Azul Temulingo
	private final Color SECONDARY_COLOR = new Color(255, 255, 255); // Blanco
	private final Color TEXT_COLOR = new Color(50, 50, 50); // Gris oscuro para el texto
	private final Color BUTTON_HOVER_COLOR = new Color(30, 144, 255); // Azul un poco más claro para el hover

	private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28); // Fuente más grande para el título
	private final Font HEADING_FONT = new Font("Arial", Font.BOLD, 18); // Nueva fuente para "Bienvenido"
	private final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 15); // Fuente para etiquetas
	private final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 16); // Fuente para campos de texto
	private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16); // Fuente para botones

	public VentanaLogin() {
		// Configuración de la ventana
		setTitle("Temulingo - Aprende de forma divertida");
		setSize(450, 600);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		// Configurar el panel principal
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(SECONDARY_COLOR);

		// Panel de título
		JPanel titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setBackground(PRIMARY_COLOR);
		titlePanel.setPreferredSize(new Dimension(WIDTH, 120));

		JLabel titleLabel = new JLabel("Temulingo");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(SECONDARY_COLOR);
		titlePanel.add(titleLabel);

		// Panel de formulario
		JPanel formPanel = new JPanel();
		formPanel.setLayout(null);
		formPanel.setBackground(SECONDARY_COLOR);

		// Etiqueta de bienvenida
		JLabel welcomeLabel = new JLabel("¡Bienvenido a Temulingo!");
		welcomeLabel.setFont(HEADING_FONT);
		welcomeLabel.setForeground(TEXT_COLOR);
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		welcomeLabel.setBounds(0, 30, 450, 30);

		// Nombre de usuario
		JLabel usernameLabel = new JLabel("Usuario o Email:");
		usernameLabel.setFont(LABEL_FONT);
		usernameLabel.setForeground(TEXT_COLOR);
		usernameLabel.setBounds(75, 80, 250, 25);

		usernameField = new JTextField();
		usernameField.setBounds(75, 110, 300, 40);
		usernameField.setFont(FIELD_FONT);
		usernameField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1));

		// Contraseña
		JLabel passwordLabel = new JLabel("Contraseña:"); // Added password label
		passwordLabel.setFont(LABEL_FONT);
		passwordLabel.setForeground(TEXT_COLOR);
		passwordLabel.setBounds(75, 170, 250, 25);

		passwordField = new JPasswordField(); // Instantiated JPasswordField
		passwordField.setBounds(75, 200, 300, 40);
		passwordField.setFont(FIELD_FONT);
		passwordField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1)); // Borde sutil

		// Botones
		loginButton = new JButton("Iniciar Sesión");
		loginButton.setBounds(75, 280, 300, 50);
		loginButton.setFont(BUTTON_FONT);
		loginButton.setBackground(PRIMARY_COLOR);
		loginButton.setForeground(Color.WHITE);
		loginButton.setFocusPainted(false);
		// Añadir efecto hover
		loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				loginButton.setBackground(BUTTON_HOVER_COLOR);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				loginButton.setBackground(PRIMARY_COLOR);
			}
		});

		registerButton = new JButton("Registrarse");
		registerButton.setBounds(75, 350, 300, 50);
		registerButton.setFont(BUTTON_FONT);
		registerButton.setBackground(new Color(220, 220, 220));
		registerButton.setForeground(TEXT_COLOR);
		registerButton.setFocusPainted(false);
		registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				registerButton.setBackground(new Color(200, 200, 200));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				registerButton.setBackground(new Color(220, 220, 220));
			}
		});

		// Componentes del formulario
		formPanel.add(welcomeLabel);
		formPanel.add(usernameLabel);
		formPanel.add(usernameField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);
		formPanel.add(loginButton);
		formPanel.add(registerButton);

		// Añadimos paneles al panel principal
		mainPanel.add(titlePanel, BorderLayout.NORTH);
		mainPanel.add(formPanel, BorderLayout.CENTER);

		// Añadimos panel principal a la ventana
		add(mainPanel);

		// Eventos
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openRegisterFrame();
			}
		});
	}
	
	private void login() {
		String input = usernameField.getText().trim();
		String password = new String(passwordField.getPassword());

		if (input.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, introduce un nombre de usuario/email y contraseña",
					"Campos vacíos", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			ControladorTemulingo controlador = ControladorTemulingo.getInstance();
			boolean loginExitoso = false;

			if (input.contains("@") && input.contains(".")) {
				loginExitoso = controlador.iniciarSesionConEmail(input, password);
			} else {
				loginExitoso = controlador.iniciarSesionConUsername(input, password);
			}

			if (loginExitoso) {
				JOptionPane.showMessageDialog(this,
						"¡Bienvenido, " + controlador.getUsuarioActual().getUsername() + "!", "Inicio exitoso",
						JOptionPane.INFORMATION_MESSAGE);

				VentanaMain mainFrame = new VentanaMain();
				mainFrame.setVisible(true);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this,
						"Credenciales incorrectas. Por favor, verifica tu usuario/email y contraseña.",
						"Error de inicio", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void openRegisterFrame() {
		VentanaRegistro registerFrame = new VentanaRegistro(this);
		registerFrame.setVisible(true);
		this.setVisible(false);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ControladorTemulingo.getInstance();
				new VentanaLogin().setVisible(true);
			}
		});
	}
}