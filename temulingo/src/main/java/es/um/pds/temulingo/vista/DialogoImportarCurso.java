package es.um.pds.temulingo.vista;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;

public class DialogoImportarCurso extends JDialog {

	private static final String FUNCION = "Importar curso";

	private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE_APP + " - " + FUNCION;

	private JTextField textField;

	private final JFrame owner;

	public DialogoImportarCurso(JFrame parent) {
		super(parent, NOMBRE_VENTANA, true); // Bloquea la ventana padre hasta que esta se cierre

		this.owner = parent;

		initComponents();
	}

	private void initComponents() {

		getContentPane().setLayout(new BorderLayout());

		JPanel panelCentro = crearPanelSeleccionCurso();
		getContentPane().add(panelCentro, BorderLayout.CENTER);

		JPanel panelBotones = crearPanelBotones();
		getContentPane().add(panelBotones, BorderLayout.SOUTH);

		pack();
		setBounds(200, 200, 250, 150);
		setResizable(false);
		setMinimumSize(getSize());
		setLocationRelativeTo(owner);

	}

	private JPanel crearPanelSeleccionCurso() {
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 74, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		// Panel contenedor interno
		JPanel panelInterno = new JPanel();
		panelInterno.setLayout(new BorderLayout(0, 0));

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 3;
		gbc_panel.gridy = 2;
		contentPanel.add(panelInterno, gbc_panel);

		// Panel con el botón y el campo de texto
		JPanel panelContenido = new JPanel();
		panelInterno.add(panelContenido, BorderLayout.CENTER);

		GridBagLayout gbl_panelContenido = new GridBagLayout();
		gbl_panelContenido.columnWidths = new int[] { 0, 0, 0 };
		gbl_panelContenido.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panelContenido.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelContenido.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelContenido.setLayout(gbl_panelContenido);

		JButton btnSeleccionar = new JButton("Seleccionar Curso");
		GridBagConstraints gbc_btnSeleccionar = new GridBagConstraints();
		gbc_btnSeleccionar.insets = new Insets(0, 0, 5, 0);
		gbc_btnSeleccionar.gridx = 1;
		gbc_btnSeleccionar.gridy = 0;
		panelContenido.add(btnSeleccionar, gbc_btnSeleccionar);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		panelContenido.add(textField, gbc_textField);

		// Evento para botón
		btnSeleccionar.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos YAML y JSON (*.yaml, *.json)", "yml", "yaml", "json");
			fileChooser.setFileFilter(filter);

			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				textField.setText(selectedFile.getAbsolutePath());
			}
		});

		return contentPanel;
	}

	private JPanel crearPanelBotones() {
		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("Subir");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancelar");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> dispose());
		buttonPane.add(cancelButton);

		return buttonPane;
	}


}
