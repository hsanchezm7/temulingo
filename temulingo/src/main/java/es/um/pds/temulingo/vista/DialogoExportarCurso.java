package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.ParseadorCursos;

/**
 * Ventana para exportar un curso seleccionado a un fichero en formato JSON o
 * YAML.
 */
public class DialogoExportarCurso extends JDialog {

	private static final long serialVersionUID = 1L;

	// --- Constantes ---
	private static final String FUNCION = "Exportar curso";
	private static final String NOMBRE_VENTANA = "Temulingo - " + FUNCION;

	private static final String DEFAULT_DIRECTORIO = "user.home";
	private static final int ANCHO_DIALOGO = 400;
	private static final int ALTO_DIALOGO = 260;

	// --- Componentes UI ---
	private JComboBox<Curso> comboCursos;
	private JRadioButton radioJson;
	private JRadioButton radioYaml;
	private JTextField txtFicheroDestino;
	private JButton btnExportar;

	// --- Estado ---
	private final Frame owner;
	private File ficheroDestino;

	// --- Constructor ---
	public DialogoExportarCurso(Frame parent) {
		super(parent, NOMBRE_VENTANA, true);
		this.owner = parent;
		inicializarComponentes();
		cargarCursos(); // Carga los cursos en el ComboBox
	}

	private void inicializarComponentes() {
		getContentPane().setLayout(new BorderLayout());

		JPanel panelCentro = crearPanelPrincipal();
		getContentPane().add(panelCentro, BorderLayout.CENTER);

		JPanel panelBotones = crearPanelBotones();
		getContentPane().add(panelBotones, BorderLayout.SOUTH);

		pack();
		setSize(ANCHO_DIALOGO, ALTO_DIALOGO);
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	/**
	 * Panel principal que contiene todos los controles de selección.
	 */
	private JPanel crearPanelPrincipal() {
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

		// 1. Selector de Curso
		JPanel panelCursos = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelCursos.add(new JLabel("Curso:"));
		comboCursos = new JComboBox<>();
		comboCursos.setPreferredSize(new Dimension(250, comboCursos.getPreferredSize().height));
		panelCursos.add(comboCursos);
		panelPrincipal.add(panelCursos);

		// 2. Selector de Formato
		JPanel panelFormato = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelFormato.add(new JLabel("Formato:"));

		radioJson = new JRadioButton("JSON", true);
		radioYaml = new JRadioButton("YAML");

		ButtonGroup grupoFormato = new ButtonGroup();
		grupoFormato.add(radioJson);
		grupoFormato.add(radioYaml);

		panelFormato.add(radioJson);
		panelFormato.add(radioYaml);
		panelPrincipal.add(panelFormato);

		// 3. Selector de Fichero de Destino
		JPanel panelFichero = crearPanelSeleccionFichero();
		panelPrincipal.add(panelFichero);

		return panelPrincipal;
	}

	/**
	 * Panel para seleccionar la ruta del fichero de destino.
	 */
	private JPanel crearPanelSeleccionFichero() {
		JPanel panelFichero = new JPanel(new BorderLayout(5, 5));
		panelFichero.setBorder(new EmptyBorder(10, 0, 0, 0));

		txtFicheroDestino = new JTextField();
		txtFicheroDestino.setEditable(false);
		txtFicheroDestino.setBackground(Color.WHITE);
		txtFicheroDestino.setToolTipText("Ruta del archivo de destino");

		JButton btnExaminar = new JButton("Examinar...");
		btnExaminar.addActionListener(e -> seleccionarArchivoDestino());

		panelFichero.add(new JLabel("Guardar en:"), BorderLayout.NORTH);
		panelFichero.add(txtFicheroDestino, BorderLayout.CENTER);
		panelFichero.add(btnExaminar, BorderLayout.EAST);

		return panelFichero;
	}

	/**
	 * Panel con los botones de acción "Exportar" y "Cancelar".
	 */
	private JPanel crearPanelBotones() {
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBotones.setBorder(new EmptyBorder(0, 15, 10, 10));

		btnExportar = new JButton("Exportar");
		btnExportar.setPreferredSize(new Dimension(110, 30));
		btnExportar.addActionListener(this::accionExportar);
		panelBotones.add(btnExportar);
		getRootPane().setDefaultButton(btnExportar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setPreferredSize(new Dimension(110, 30));
		btnCancelar.addActionListener(e -> dispose());
		panelBotones.add(btnCancelar);

		return panelBotones;
	}

	// --- Lógica de Acciones ---

	/**
	 * Acción que se ejecuta al pulsar el botón "Exportar".
	 */
	private void accionExportar(ActionEvent e) {
		Curso cursoSeleccionado = (Curso) comboCursos.getSelectedItem();

		if (cursoSeleccionado == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar un curso.", "Error de validación",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (ficheroDestino == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar un fichero de destino.", "Error de validación",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		ParseadorCursos.Formato formato = radioJson.isSelected() ? ParseadorCursos.Formato.JSON
				: ParseadorCursos.Formato.YAML;

		try {
			ControladorTemulingo.getInstance().exportarCursoAFichero(cursoSeleccionado, ficheroDestino, formato);

			JOptionPane.showMessageDialog(this, "Curso exportado correctamente.", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Error al exportar el curso: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		JOptionPane.showMessageDialog(this, "Curso exportado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	/**
	 * Abre un JFileChooser para que el usuario elija dónde guardar el archivo.
	 */
	private void seleccionarArchivoDestino() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Guardar curso como...");
		fileChooser.setCurrentDirectory(new File(System.getProperty(DEFAULT_DIRECTORIO)));
		fileChooser.setAcceptAllFileFilterUsed(false); // Muy importante

		// Detectar el formato seleccionado
		ParseadorCursos.Formato formato = radioJson.isSelected() ? ParseadorCursos.Formato.JSON
				: ParseadorCursos.Formato.YAML;

		FileNameExtensionFilter filtro;
		String ext;

		if (formato == ParseadorCursos.Formato.JSON) {
			filtro = new FileNameExtensionFilter("Archivos JSON (*.json)", "json");
			ext = ".json";
		} else {
			filtro = new FileNameExtensionFilter("Archivos YAML (*.yaml, *.yml)", "yaml", "yml");
			ext = ".yaml";
		}

		fileChooser.setFileFilter(filtro);
		fileChooser.setAcceptAllFileFilterUsed(false);

		Curso cursoSel = (Curso) comboCursos.getSelectedItem();
		if (cursoSel != null) {
			fileChooser.setSelectedFile(new File(cursoSel.getTitulo().replace(" ", "_").toLowerCase() + ext));
		}

		int resultado = fileChooser.showSaveDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			File archivoSeleccionado = fileChooser.getSelectedFile();

			// Asegurar que tenga la extensión correcta
			if (!archivoSeleccionado.getName().toLowerCase().endsWith(ext)) {
				archivoSeleccionado = new File(archivoSeleccionado.getAbsolutePath() + ext);
			}

			this.ficheroDestino = archivoSeleccionado;
			txtFicheroDestino.setText(this.ficheroDestino.getAbsolutePath());
		}
	}

	/**
	 * Carga los cursos del usuario actual en el JComboBox. Utiliza la estructura
	 * real: Controlador -> Usuario -> Cursos
	 */
	/**
	 * Carga los cursos del usuario actual en el JComboBox. Si no hay cursos,
	 * muestra un mensaje informativo en el combo.
	 */
	private void cargarCursos() {
		Collection<Curso> cursos = ControladorTemulingo.getInstance().getUsuarioActual().getCursos();

		comboCursos.removeAllItems();

		if (cursos != null && !cursos.isEmpty()) {
			for (Curso curso : cursos) {
				comboCursos.addItem(curso);
			}
			comboCursos.setEnabled(true);
			btnExportar.setEnabled(true);
		} else {
			Curso noCursosPlaceholder = new Curso();
			noCursosPlaceholder.setTitulo("No hay cursos disponibles");
			comboCursos.addItem(noCursosPlaceholder);
			comboCursos.setEnabled(false);
			btnExportar.setEnabled(false);
		}
	}

}