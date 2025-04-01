package es.um.pds.temulingo.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Ventana de login para nuestra aplicación Temulingo.
 * Permite iniciar sesión con username del usuario
 * o ir a la ventana de registro (si no se tiene cuenta todavía).
 */
public class VentanaLogin extends JFrame {
    
    private JTextField usernameField;
    private JButton loginButton;
    private JButton registerButton;
    private ArrayList<String> usersList;
    
    // Constantes para el diseño
    private final Color PRIMARY_COLOR = new Color(25, 118, 210); // Colores Azules
    private final Color SECONDARY_COLOR = new Color(255, 255, 255);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    
    public VentanaLogin() {
        // Configuración de la ventana
        setTitle("Temulingo - Aprende de forma divertida");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Cargar usuarios
        loadUsers();
        
        // Configurar el panel principal
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
        
        // Etiqueta de bienvenida
        JLabel welcomeLabel = new JLabel("¡Bienvenido a Temulingo!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setBounds(110, 30, 200, 30);
        
        // Nombre de usuario
        JLabel usernameLabel = new JLabel("Nombre de usuario:");
        usernameLabel.setFont(NORMAL_FONT);
        usernameLabel.setBounds(50, 80, 150, 25);
        
        usernameField = new JTextField();
        usernameField.setBounds(50, 110, 300, 35);
        usernameField.setFont(NORMAL_FONT);
        
        // Botones
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBounds(50, 170, 300, 40);
        loginButton.setFont(NORMAL_FONT);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        
        registerButton = new JButton("Registrarse");
        registerButton.setBounds(50, 230, 300, 40);
        registerButton.setFont(NORMAL_FONT);
        registerButton.setBackground(new Color(220, 220, 220));
        registerButton.setFocusPainted(false);
        
        // Componentes del formulario
        formPanel.add(welcomeLabel);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
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
    
    // Más metodos para cargar los usuarios previamente guardados pero vamos que supongo que habrá que usar la 
    // persistencia que implementó Hugo.
    
    private void loadUsers() {
        usersList = new ArrayList<>();
        try {
            File file = new File("users.txt");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    usersList.add(line.trim());
                }
                br.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar usuarios: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, introduce un nombre de usuario", 
                "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (userExists(username)) {
            JOptionPane.showMessageDialog(this, 
                "¡Bienvenido, " + username + "!", 
                "Inicio exitoso", JOptionPane.INFORMATION_MESSAGE);
            
            // Aquí se abriría la ventana principal de la aplicación
            // MainFrame mainFrame = new MainFrame(username);
            // mainFrame.setVisible(true);
            // this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario no encontrado. Por favor, regístrate primero.", 
                "Error de inicio", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean userExists(String username) {
        return usersList.contains(username);
    }
    
    private void openRegisterFrame() {
        VentanaRegistro registerFrame = new VentanaRegistro(this);
        registerFrame.setVisible(true);
        this.setVisible(false);
    }
    
    public void refreshUsersList() {
        loadUsers();
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
                new VentanaLogin().setVisible(true);
            }
        });
    }
}