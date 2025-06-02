package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.utils.CursoCellRenderer;

public class VentanaMain extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final String FUNCION = "Inicio";
	private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE_APP + " - " + FUNCION;

	private static final int MARGEN = 10;
	private static final int ANCHO_BOTON = 170;
	private static final int ALTO_BOTON = 50;
	private static final int ESPACIO_ICONO_TEXTO = 15;
	private static final int FILAS_VISIBLES_LISTA = 5;
	private static final int ANCHO_SCROLL_PANE = 500;
	private static final int ALTO_SCROLL_PANE = 300;

	private DefaultListModel<Curso> modelo;
	private JList<Curso> listaCursos;
	private JButton btnEstadisticas;
	private JButton btnActualizar;
	private JButton btnImportar;
	private JButton btnIniciar;
	private JButton btnSalir;

	public VentanaMain() {
		cargarModelo();
		inicializarComponentes();
		configurarEventListeners();
	}

	private void cargarModelo() {
		modelo = new DefaultListModel<>();
		cargarCursos();
	}

	private void cargarCursos() {
		ControladorTemulingo.getInstance().getAllCursos().stream().forEach(c -> modelo.addElement(c));
	}

	private void inicializarComponentes() {
		setTitle(NOMBRE_VENTANA);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		String rutaIconoLogo = ConfiguracionTemulingo.getRutaIcono("logo.icono");
		ImageIcon iconoLogo = new ImageIcon(getClass().getResource(rutaIconoLogo));
		setIconImage(iconoLogo.getImage());

		configurarLayout();

		pack();
		setResizable(false);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
	}

	private void configurarLayout() {
		add(crearPanelLogo(), BorderLayout.NORTH);
		add(crearPanelPrincipal(), BorderLayout.CENTER);
		add(crearPanelBotones(), BorderLayout.SOUTH);
	}

	private JPanel crearPanelLogo() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, MARGEN, MARGEN));

		String rutaIconoLogo = ConfiguracionTemulingo.getRutaIcono("logo.icono");
		ImageIcon iconoLogo = new ImageIcon(getClass().getResource(rutaIconoLogo));

		JLabel lblLogo = new JLabel(iconoLogo);
		panel.add(lblLogo);

		return panel;
	}

	private JPanel crearPanelPrincipal() {
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		panelPrincipal.setBorder(new EmptyBorder(MARGEN, MARGEN, MARGEN, MARGEN));

		JPanel panelWrapper = new JPanel(new BorderLayout());
		panelWrapper.setBorder(new TitledBorder(null, "  Menú  ", TitledBorder.CENTER, TitledBorder.TOP));

		panelWrapper.add(crearPanelBotonesAccion(), BorderLayout.NORTH);
		panelWrapper.add(crearPanelListaCursos(), BorderLayout.CENTER);

		panelPrincipal.add(panelWrapper, BorderLayout.CENTER);
		return panelPrincipal;
	}

	private JPanel crearPanelBotonesAccion() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, MARGEN, MARGEN));
		Dimension tamanoBoton = new Dimension(ANCHO_BOTON, ALTO_BOTON);

		btnEstadisticas = crearBotonAccion("Estadísticas", ConfiguracionTemulingo.getRutaIcono("stats.icono"),
				tamanoBoton, "Ver estadísticas");
		btnActualizar = crearBotonAccion("Actualizar", ConfiguracionTemulingo.getRutaIcono("update.icono"), tamanoBoton,
				"Actualizar cursos");
		btnImportar = crearBotonAccion("Importar", ConfiguracionTemulingo.getRutaIcono("add.icono"), tamanoBoton,
				"Importar curso");

		panel.add(btnEstadisticas);
		panel.add(btnActualizar);
		panel.add(btnImportar);

		return panel;
	}

	private JPanel crearPanelListaCursos() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(MARGEN, MARGEN, MARGEN, MARGEN));

		// TODO: mostrar un JLabel: 'no hay ningún curso en la librería' en caso de que
		// no haya cursos

		listaCursos = new JList<>(modelo);
		listaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaCursos.setCellRenderer(new CursoCellRenderer());

		listaCursos.setVisibleRowCount(FILAS_VISIBLES_LISTA);

		JScrollPane scrollPane = new JScrollPane(listaCursos);
		scrollPane.setPreferredSize(new Dimension(ANCHO_SCROLL_PANE, ALTO_SCROLL_PANE));

		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelBotones() {
		JPanel panelBotones = new JPanel(new BorderLayout());
		panelBotones.setBorder(new EmptyBorder(MARGEN, MARGEN, MARGEN, MARGEN));

		JPanel panelIzquierdo = new JPanel();
		btnSalir = new JButton("Salir");
		btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelIzquierdo.add(btnSalir);

		JPanel panelDerecho = new JPanel();
		btnIniciar = new JButton("Iniciar");
		btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelDerecho.add(btnIniciar);

		panelBotones.add(panelIzquierdo, BorderLayout.WEST);
		panelBotones.add(panelDerecho, BorderLayout.EAST);

		return panelBotones;
	}

	private JButton crearBotonAccion(String texto, String rutaIcono, Dimension tamano, String textoAlternativo) {
		JButton boton = new JButton(texto);
		ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
		boton.setIcon(icono);
		boton.setPreferredSize(tamano);
		boton.setIconTextGap(ESPACIO_ICONO_TEXTO);
		boton.setToolTipText(textoAlternativo);
		return boton;
	}

	private void configurarEventListeners() {
		btnImportar.addActionListener(e -> abrirImportarCurso());
		btnSalir.addActionListener(e -> System.exit(0));
		// TODO: Añadir listener para btnIniciar y btnAgregar
	}

	private void abrirImportarCurso() {
		DialogoImportarCurso ventanaImportar = new DialogoImportarCurso(this);
		ventanaImportar.setVisible(true);
	}

	public void agregarCurso(Curso curso) {
		if (curso != null) {
			modelo.addElement(curso);
		}
	}

	public Curso obtenerCursoSeleccionado() {
		return listaCursos.getSelectedValue();
	}

	public void limpiarSeleccion() {
		listaCursos.clearSelection();
	}
}