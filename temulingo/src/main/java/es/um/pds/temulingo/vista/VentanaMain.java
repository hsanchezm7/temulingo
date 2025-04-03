package es.um.pds.temulingo.vista;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VentanaMain extends JFrame {
    private static final String FUNCION = "Inicio";

    private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE_APP + " - " + FUNCION;

    public VentanaMain() {
        initComponents();
    }

    public void initComponents() {
        /* Window properties */
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon("/media/logo100px.png");
        setIconImage(img.getImage());

        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panelLogo = crearPanelLogo();
        getContentPane().add(panelLogo, BorderLayout.NORTH);

        JPanel panelCentro = crearPanelPrincipal();
        getContentPane().add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        pack();
        setResizable(true);
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    public JPanel crearPanelLogo() {
        JPanel panelLogo = new JPanel();
        getContentPane().add(panelLogo, BorderLayout.NORTH);
        panelLogo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblAppchat = new JLabel("");
        lblAppchat.setIcon(new ImageIcon(VentanaMain.class.getResource("/media/logo100px.png")));
        panelLogo.add(lblAppchat);

        return panelLogo;
    }

    public JPanel crearPanelPrincipal() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelPrincipal.setLayout(new BorderLayout(0, 0));

        JPanel panelWrapperPrincipal = new JPanel();
        panelWrapperPrincipal.setBorder(
                new TitledBorder(null, " Menú Principal ", TitledBorder.LEFT, TitledBorder.TOP, null, null));
        panelPrincipal.add(panelWrapperPrincipal, BorderLayout.CENTER);

        // Usar BoxLayout para organizar los botones verticalmente
        panelWrapperPrincipal.setLayout(new BoxLayout(panelWrapperPrincipal, BoxLayout.Y_AXIS));

        // Añadir espacio al principio
        panelWrapperPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botón para Biblioteca de cursos
        JButton btnBiblioteca = new JButton("Biblioteca de cursos");
        btnBiblioteca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        btnBiblioteca.setPreferredSize(new Dimension(400, 70));
        btnBiblioteca.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBiblioteca.setFont(new Font("Dialog", Font.PLAIN, 16));
        btnBiblioteca.setMargin(new Insets(15, 15, 15, 15));
        btnBiblioteca.setFocusPainted(false);
        // btnBiblioteca.addActionListener(e -> abrirBiblioteca());

        // Espacio entre botones
        panelWrapperPrincipal.add(btnBiblioteca);
        panelWrapperPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón para Añadir curso
        JButton btnAnadir = new JButton("Añadir curso");
        btnAnadir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        btnAnadir.setPreferredSize(new Dimension(400, 70));
        btnAnadir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAnadir.setFont(new Font("Dialog", Font.PLAIN, 16));
        btnAnadir.setMargin(new Insets(15, 15, 15, 15));
        btnAnadir.setFocusPainted(false);
        // btnAnadir.addActionListener(e -> anadirCurso());

        panelWrapperPrincipal.add(btnAnadir);

        // Añadir espacio al final
        panelWrapperPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        return panelPrincipal;
    }

    public JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();
        panelBotones.setBorder(new EmptyBorder(0, 10, 10, 10));
        panelBotones.setLayout(new BorderLayout(0, 0));

        JPanel panelBtnCancel = new JPanel();
        panelBotones.add(panelBtnCancel, BorderLayout.WEST);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBtnCancel.add(btnSalir);
        // btnCancel.addActionListener(e -> handleCancel());

        // Se elimina el panel y botón de "Confirmar" que no se necesita
        // JPanel panelBtnConfirmRegister = new JPanel();
        // panelBotones.add(panelBtnConfirmRegister, BorderLayout.EAST);
        // JButton btnConfirmRegister = new JButton("Confirm");
        // btnConfirmRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        // panelBtnConfirmRegister.add(btnConfirmRegister);
        // getRootPane().setDefaultButton(btnConfirmRegister);

        return panelBotones;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaMain().setVisible(true);
        });
    }
}