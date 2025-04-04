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
        setTitle(NOMBRE_VENTANA);
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
        setResizable(false);
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
        JPanel panelCentro = new JPanel();
        panelCentro.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelCentro.setLayout(new BorderLayout(0, 0));

        JPanel panelWrapper = new JPanel();
        panelWrapper.setBorder(new TitledBorder(null, "  Menú Principal  ", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        panelCentro.add(panelWrapper, BorderLayout.CENTER);
        panelWrapper.setLayout(new BorderLayout(0, 0));

        JPanel panelContenido = new JPanel();
        panelContenido.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelWrapper.add(panelContenido, BorderLayout.CENTER);

        // Layout vertical para los botones
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botón: Biblioteca de cursos
        JButton btnBiblioteca = new JButton("Biblioteca de cursos");
        btnBiblioteca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        btnBiblioteca.setPreferredSize(new Dimension(400, 70));
        btnBiblioteca.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBiblioteca.setFont(new Font("Dialog", Font.PLAIN, 16));
        btnBiblioteca.setMargin(new Insets(10, 10, 10, 10));
        btnBiblioteca.setFocusPainted(false);
        panelContenido.add(btnBiblioteca);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón: Añadir curso
        JButton btnAnadir = new JButton("Añadir curso");
        btnAnadir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        btnAnadir.setPreferredSize(new Dimension(400, 70));
        btnAnadir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAnadir.setFont(new Font("Dialog", Font.PLAIN, 16));
        btnAnadir.setMargin(new Insets(10, 10, 10, 10));
        btnAnadir.setFocusPainted(false);
        panelContenido.add(btnAnadir);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));

        btnAnadir.addActionListener(e -> abrirImportarCurso());

        return panelCentro;
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

        return panelBotones;
    }

    private void abrirImportarCurso() {
        DialogoImportarCurso ventanaImportar = new DialogoImportarCurso(this);
        ventanaImportar.setVisible(true);
    }
}