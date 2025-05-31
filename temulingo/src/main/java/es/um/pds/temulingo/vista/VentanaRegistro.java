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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Ventana de registro para la aplicación Temulingo. Permite registrar nuevos
 * usuarios con su respectivo username y seleccionar su de tipo de usuario.
 */
public class VentanaRegistro extends JFrame {

	private JTextField usernameField;
	private JComboBox<String> userTypeComboBox;
	private JButton registerButton;
	private JButton backButton;
	private VentanaLogin parentFrame;

	// Constantes para el diseño de la ventana
	private final Color PRIMARY_COLOR = new Color(25, 118, 210); // Azul Temulingo
	private final Color SECONDARY_COLOR = new Color(255, 255, 255);
	private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
	private final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);

	public VentanaRegistro(VentanaLogin parent) {
		this.parentFrame = parent;

		// Configuración de la ventana
		setTitle("Temulingo - Registro");
		setSize(400, 500);
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
		titlePanel.setPreferredSize(new Dimension(400, 100));

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
		registerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		registerLabel.setBounds(145, 30, 200, 30);

		// Nombre de usuario
		JLabel usernameLabel = new JLabel("Nombre de usuario:");
		usernameLabel.setFont(NORMAL_FONT);
		usernameLabel.setBounds(50, 80, 150, 25);

		usernameField = new JTextField();
		usernameField.setBounds(50, 110, 300, 35);
		usernameField.setFont(NORMAL_FONT);

		// Tipo de usuario
		JLabel userTypeLabel = new JLabel("Tipo de usuario:");
		userTypeLabel.setFont(NORMAL_FONT);
		userTypeLabel.setBounds(50, 160, 150, 25);

		String[] userTypes = { "Estudiante", "Creador de Cursos" };
		userTypeComboBox = new JComboBox<>(userTypes);
		userTypeComboBox.setBounds(50, 190, 300, 35);
		userTypeComboBox.setFont(NORMAL_FONT);

		// Botones
		registerButton = new JButton("Registrarse");
		registerButton.setBounds(50, 250, 300, 40);
		registerButton.setFont(NORMAL_FONT);
		registerButton.setBackground(PRIMARY_COLOR);
		registerButton.setForeground(Color.BLACK);
		registerButton.setFocusPainted(false);

		backButton = new JButton("Volver al Login");
		backButton.setBounds(50, 310, 300, 40);
		backButton.setFont(NORMAL_FONT);
		backButton.setBackground(new Color(220, 220, 220));
		backButton.setFocusPainted(false);

		// Componentes del formulario
		formPanel.add(registerLabel);
		formPanel.add(usernameLabel);
		formPanel.add(usernameField);
		formPanel.add(userTypeLabel);
		formPanel.add(userTypeComboBox);
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
		String username = usernameField.getText().trim();
		String userType = (String) userTypeComboBox.getSelectedItem();

		if (username.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, introduce un nombre de usuario", "Campos vacíos",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (userExists(username)) {
			JOptionPane.showMessageDialog(this, "Este nombre de usuario ya existe. Por favor, elige otro.",
					"Usuario duplicado", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Guardar el usuario (Esto lo he hecho para probar si funcionaria así pero
		// vamos que ni idea)
		try {
			FileWriter fw = new FileWriter("users.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(username);
			bw.newLine();
			bw.close();

			// Intento de guardar el tipo de usuario en otro lao (otro archivo distinto)
			FileWriter fwTypes = new FileWriter("usertypes.txt", true);
			BufferedWriter bwTypes = new BufferedWriter(fwTypes);
			bwTypes.write(username + "," + userType);
			bwTypes.newLine();
			bwTypes.close();

			JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ya puedes iniciar sesión como " + userType,
					"Registro completado", JOptionPane.INFORMATION_MESSAGE);

			goBackToLogin();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean userExists(String username) {
		try {
			File file = new File("users.txt");
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.trim().equals(username)) {
						br.close();
						return true;
					}
				}
				br.close();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error al verificar usuario: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	private void goBackToLogin() {
		parentFrame.refreshUsersList();
		parentFrame.setVisible(true);
		this.dispose();
	}
}
