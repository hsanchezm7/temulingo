package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.controlador.ControladorTemulingo;

public class DialogoImportarCurso extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final String FUNCION = "Importar curso";
	private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE + " - " + FUNCION;

	private static final String DEFAULT_DIRECTORIO = "user.home";

	private static final int ANCHO_DIALOGO = 300;
	private static final int ALTO_DIALOGO = 180;

	private JTextField txtFicheroSel;

	private final Frame owner;

	private File ficheroSel;

	public DialogoImportarCurso(Frame parent) {
		super(parent, NOMBRE_VENTANA, true);

		this.owner = parent;

		inicializarComponentes();
	}

	private void inicializarComponentes() {
		getContentPane().setLayout(new BorderLayout());

		JPanel panelCentro = crearPanelSeleccionCurso();
		getContentPane().add(panelCentro, BorderLayout.CENTER);

		JPanel panelBotones = crearPanelBotones();
		getContentPane().add(panelBotones, BorderLayout.SOUTH);

		pack();
		setSize(ANCHO_DIALOGO, ALTO_DIALOGO);
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	private JPanel crearPanelSeleccionCurso() {
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

		JLabel lblTitulo = new JLabel("Seleccionar archivo de curso:", SwingConstants.CENTER);
		panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

		JPanel panelArchivo = new JPanel(new BorderLayout(5, 5));
		panelArchivo.setBorder(new EmptyBorder(10, 0, 0, 0));

		txtFicheroSel = new JTextField();
		txtFicheroSel.setEditable(false);
		txtFicheroSel.setBackground(Color.WHITE);
		txtFicheroSel.setToolTipText("Ruta del archivo seleccionado");

		JButton btnSeleccionar = new JButton("Examinar");
		btnSeleccionar.setPreferredSize(new Dimension(100, txtFicheroSel.getPreferredSize().height));

		btnSeleccionar.addActionListener(e -> seleccionarArchivo());

		panelArchivo.add(txtFicheroSel, BorderLayout.CENTER);
		panelArchivo.add(btnSeleccionar, BorderLayout.EAST);

		panelPrincipal.add(panelArchivo, BorderLayout.CENTER);

		return panelPrincipal;
	}

	private JPanel crearPanelBotones() {
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBotones.setBorder(new EmptyBorder(0, 15, 15, 15));

		JButton btnImportar = new JButton("Importar");
		btnImportar.setPreferredSize(new Dimension(100, 30));
		btnImportar.addActionListener(e -> gestionarSubida(ficheroSel));
		panelBotones.add(btnImportar);
		getRootPane().setDefaultButton(btnImportar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setPreferredSize(new Dimension(100, 30));
		btnCancelar.addActionListener(e -> dispose());
		panelBotones.add(btnCancelar);

		return panelBotones;
	}

	private void gestionarSubida(File fichero) {
		System.out.println("Gestionando la subida del fichero" + fichero.getName());
		try {
			ControladorTemulingo.getInstance().importarCursoDesdeFichero(fichero);
			JOptionPane.showMessageDialog(this, "Curso importado correctamente.", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error al importar el curso: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void seleccionarArchivo() {
		JFileChooser fileChooser = new JFileChooser();

		FileNameExtensionFilter yamlFilter = new FileNameExtensionFilter("Archivos YAML (*.yaml, *.yml)", "yml",
				"yaml");
		FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("Archivos JSON (*.json)", "json");
		FileNameExtensionFilter allFilter = new FileNameExtensionFilter(
				"Todos los archivos soportados (*.yaml, *.yml, *.json)", "yml", "yaml", "json");

		fileChooser.addChoosableFileFilter(allFilter);
		fileChooser.addChoosableFileFilter(yamlFilter);
		fileChooser.addChoosableFileFilter(jsonFilter);
		fileChooser.setFileFilter(allFilter);

		fileChooser.setDialogTitle("Seleccionar archivo de curso");
		fileChooser.setCurrentDirectory(new File(System.getProperty(DEFAULT_DIRECTORIO)));

		int resultado = fileChooser.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			File archivoSeleccionado = fileChooser.getSelectedFile();

			if (esArchivoValido(archivoSeleccionado)) {
				txtFicheroSel.setText(archivoSeleccionado.getAbsolutePath());
				txtFicheroSel.setCaretPosition(txtFicheroSel.getText().length());

				ficheroSel = archivoSeleccionado;
			} else {
				JOptionPane.showMessageDialog(this, "Por favor seleccione un archivo YAML (.yaml, .yml) o JSON (.json)",
						"Tipo de archivo no válido", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private boolean esArchivoValido(File archivo) {
		if (archivo == null || !archivo.exists() || !archivo.isFile()) {
			return false;
		}

		String nombre = archivo.getName().toLowerCase();
		return nombre.endsWith(".json") || nombre.endsWith(".yaml") || nombre.endsWith(".yml");
	}

}
