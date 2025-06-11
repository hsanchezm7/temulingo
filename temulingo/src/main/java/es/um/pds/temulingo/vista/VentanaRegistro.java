package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import es.um.pds.temulingo.controlador.ControladorTemulingo;

/**
 * Ventana de registro para la aplicación Temulingo. Permite registrar nuevos
 * usuarios con su nombre, email, username, contraseña y fecha de nacimiento,
 */
public class VentanaRegistro extends JFrame {

	private JTextField nameField;
	private JTextField usernameField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JTextField dobField;
	private JComboBox<String> userTypeComboBox;
	private JButton registerButton;
	private JButton backButton;
	private VentanaLogin parentFrame;

	// Constantes para el diseño de la ventana
	private final Color PRIMARY_COLOR = new Color(25, 118, 210); // Azul Temulingo
	private final Color SECONDARY_COLOR = new Color(255, 255, 255); // Blanco
	private final Color TEXT_COLOR = new Color(50, 50, 50); // Gris oscuro para el texto
	private final Color BUTTON_HOVER_COLOR = new Color(30, 144, 255); // Azul un poco más claro para el hover

	private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
	private final Font HEADING_FONT = new Font("Arial", Font.BOLD, 18); // Nueva fuente para "Crea tu cuenta"
	private final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 15); // Fuente para etiquetas
	private final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 16); // Fuente para campos de texto
	private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16); // Fuente para botones

	public VentanaRegistro(VentanaLogin parent) {
		this.parentFrame = parent;

		// Configuración de la ventana
		setTitle("Temulingo - Registro");
		setSize(500, 850);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		// Panel principal
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(SECONDARY_COLOR);

		// Panel de título
		JPanel titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setBackground(PRIMARY_COLOR);
		titlePanel.setPreferredSize(new Dimension(WIDTH, 120));

		JLabel titleLabel = new JLabel("Temulingo");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(Color.WHITE);
		titlePanel.add(titleLabel);

		// Panel de formulario
		JPanel formPanel = new JPanel();
		formPanel.setLayout(null);
		formPanel.setBackground(SECONDARY_COLOR);

		// Etiqueta de registro
		JLabel registerLabel = new JLabel("Crea tu cuenta");
		registerLabel.setFont(HEADING_FONT);
		registerLabel.setForeground(TEXT_COLOR);
		registerLabel.setHorizontalAlignment(JLabel.CENTER);
		registerLabel.setBounds(0, 20, 450, 30);

		// Nombre
		JLabel nameLabel = new JLabel("Nombre completo:");
		nameLabel.setFont(HEADING_FONT);
		nameLabel.setBounds(75, 60, 250, 25);

		nameField = new JTextField();
		nameField.setBounds(75, 90, 300, 40);
		nameField.setFont(HEADING_FONT);
		nameField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1));

		// Nombre de usuario
		JLabel usernameLabel = new JLabel("Nombre de usuario:");
		usernameLabel.setFont(HEADING_FONT);
		usernameLabel.setForeground(TEXT_COLOR);
		usernameLabel.setBounds(75, 220, 250, 25);

		usernameField = new JTextField();
		usernameField.setBounds(75, 250, 300, 40);
		usernameField.setFont(FIELD_FONT);
		usernameField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1));

		// Email
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setFont(LABEL_FONT);
		emailLabel.setForeground(TEXT_COLOR);
		emailLabel.setBounds(75, 140, 250, 25);

		emailField = new JTextField();
		emailField.setBounds(75, 170, 300, 40);
		emailField.setFont(FIELD_FONT);
		emailField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1));

		// Tipo de usuario
		JLabel userTypeLabel = new JLabel("Tipo de usuario:");
		userTypeLabel.setFont(LABEL_FONT);
		userTypeLabel.setForeground(TEXT_COLOR);
		userTypeLabel.setBounds(75, 380, 250, 25);

		String[] userTypes = { "Estudiante", "Creador de Cursos" };
		userTypeComboBox = new JComboBox<>(userTypes);
		userTypeComboBox.setBounds(75, 410, 300, 40);
		userTypeComboBox.setFont(FIELD_FONT);
		((JLabel) userTypeComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER); // Centrar texto en JComboBox
		userTypeComboBox.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1));

		// Contraseña
		JLabel passwordLabel = new JLabel("Contraseña:");
		passwordLabel.setFont(LABEL_FONT);
		passwordLabel.setForeground(TEXT_COLOR);
		passwordLabel.setBounds(75, 300, 250, 25);

		passwordField = new JPasswordField();
		passwordField.setBounds(75, 330, 300, 40);
		passwordField.setFont(FIELD_FONT);
		passwordField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1));

		// Fecha de nacimiento
		JLabel dobLabel = new JLabel("Fecha de nacimiento (YYYY-MM-DD):");
		dobLabel.setFont(LABEL_FONT);
		dobLabel.setForeground(TEXT_COLOR);
		dobLabel.setBounds(75, 460, 300, 25);

		dobField = new JTextField();
		dobField.setBounds(75, 490, 300, 40);
		dobField.setFont(FIELD_FONT);
		dobField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1));

		// Botones
		registerButton = new JButton("Registrarse");
		registerButton.setBounds(75, 560, 300, 50);
		registerButton.setFont(BUTTON_FONT);
		registerButton.setBackground(PRIMARY_COLOR);
		registerButton.setForeground(Color.WHITE);
		registerButton.setFocusPainted(false);
		registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				registerButton.setBackground(BUTTON_HOVER_COLOR);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				registerButton.setBackground(PRIMARY_COLOR);
			}
		});

		backButton = new JButton("Volver al Login");
		backButton.setBounds(75, 620, 300, 50);
		backButton.setFont(BUTTON_FONT);
		backButton.setBackground(new Color(220, 220, 220));
		backButton.setForeground(TEXT_COLOR);
		backButton.setFocusPainted(false);
		backButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				backButton.setBackground(new Color(200, 200, 200));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				backButton.setBackground(new Color(220, 220, 220));
			}
		});

		// Componentes del formulario
		formPanel.add(registerLabel);
		formPanel.add(usernameLabel);
		formPanel.add(nameLabel);
		formPanel.add(nameField);
		formPanel.add(usernameField);
		formPanel.add(emailLabel);
		formPanel.add(emailField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);
		formPanel.add(userTypeLabel);
		formPanel.add(userTypeComboBox);
		formPanel.add(dobLabel);
		formPanel.add(dobField);
		formPanel.add(registerButton);
		formPanel.add(backButton);

		// Paneles principales??
		mainPanel.add(titlePanel, BorderLayout.NORTH);
		mainPanel.add(formPanel, BorderLayout.CENTER);

		// Añadir panel principal a la ventana
		add(mainPanel);

		// Eventos
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goBackToLogin();
			}
		});
	}

	private void register() {
		String name = nameField.getText().trim();
		String username = usernameField.getText().trim();
		String userType = (String) userTypeComboBox.getSelectedItem();
		String email = emailField.getText().trim();
		String password = new String(passwordField.getPassword());
		String dobString = dobField.getText().trim();
		LocalDate fechaNacim = null;

		if (username.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, introduce un nombre de usuario", "Campos vacíos",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || dobString.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Este nombre de usuario ya existe. Por favor, elige otro.",
					"Usuario duplicado", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!email.contains("@") || !email.contains(".")) {
			JOptionPane.showMessageDialog(this, "Por favor, introduce un email válido.", "Email inválido",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Parse date of birth
		try {
			fechaNacim = LocalDate.parse(dobString);
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD.", "Fecha inválida",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			ControladorTemulingo controlador = ControladorTemulingo.getInstance();
			boolean registroExitoso = controlador.registrarUsuario(name, email, username, password, fechaNacim);

			if (registroExitoso) {
				JOptionPane.showMessageDialog(this,
						"¡Registro exitoso! Ya puedes iniciar sesión como " + userType + ".", "Registro completado",
						JOptionPane.INFORMATION_MESSAGE);
				goBackToLogin();
			} else {
				// This implies that either the email or username already exists (as per
				// ControladorTemulingo logic)
				JOptionPane.showMessageDialog(this, "El usuario o email ya existe. Por favor, elige otros.",
						"Error de registro", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void goBackToLogin() {

		parentFrame.setVisible(true);
		this.dispose();
	}
}
