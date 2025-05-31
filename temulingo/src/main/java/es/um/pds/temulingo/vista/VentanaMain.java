package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;

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
		panelLogo.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

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
	    panelWrapper.setBorder(
	        new TitledBorder(null, "  Menú  ", TitledBorder.CENTER, TitledBorder.TOP, null, null));
	    panelCentro.add(panelWrapper, BorderLayout.CENTER);
	    panelWrapper.setLayout(new BorderLayout(0, 0));

	    // 🔼 Subpanel Norte: Botones horizontales
	    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
	    Dimension tamBoton = new Dimension(150, 50);

	    JButton btnAgregar = new JButton("Agregar");
	    btnAgregar.setPreferredSize(tamBoton);
	    btnAgregar.setIcon(new ImageIcon(getClass().getResource("/media/stats_32px.png")));
	    btnAgregar.setIconTextGap(15);

	    JButton btnActualizar = new JButton("Importar");
	    btnActualizar.setIcon(new ImageIcon(getClass().getResource("/media/add_32px.png")));
	    btnActualizar.setPreferredSize(tamBoton);
	    btnActualizar.setIconTextGap(15);

	    panelBotones.add(btnAgregar);
	    panelBotones.add(btnActualizar);
	    panelWrapper.add(panelBotones, BorderLayout.NORTH);

	    // 🔽 Subpanel Central: Lista de cursos
	    JPanel panelLista = new JPanel(new BorderLayout());
	    panelLista.setBorder(new EmptyBorder(10, 10, 10, 10));

	    DefaultListModel<String> modeloCursos = new DefaultListModel<>();
	    modeloCursos.addElement("Curso de Java");
	    modeloCursos.addElement("Curso de Spring Boot");
	    modeloCursos.addElement("Curso de Git");
	    modeloCursos.addElement("Curso de Bases de Datos");

	    JList<String> listaCursos = new JList<>(modeloCursos);
	    listaCursos.setVisibleRowCount(8);
	    JScrollPane scrollCursos = new JScrollPane(listaCursos);

	    panelLista.add(scrollCursos, BorderLayout.CENTER);
	    panelWrapper.add(panelLista, BorderLayout.CENTER);

	    return panelCentro;
	}


	public JPanel crearPanelBotones() {
		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelBotones.setLayout(new BorderLayout(0, 0));

		JPanel panelBtnCancel = new JPanel();
		panelBotones.add(panelBtnCancel, BorderLayout.WEST);

		JButton btnSalir = new JButton("Salir");
		btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelBtnCancel.add(btnSalir);
		
		JPanel panelBtnIniciar = new JPanel();
		panelBotones.add(panelBtnIniciar, BorderLayout.EAST);
		
		// TODO: Añadir listener bloqueo/desbloqueo
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelBtnIniciar.add(btnIniciar);

		return panelBotones;
	}

	private void abrirImportarCurso() {
		DialogoImportarCurso ventanaImportar = new DialogoImportarCurso(this);
		ventanaImportar.setVisible(true);
	}
}