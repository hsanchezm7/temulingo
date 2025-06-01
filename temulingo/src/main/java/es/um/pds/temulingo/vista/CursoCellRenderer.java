package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.logic.Curso;

public class CursoCellRenderer extends JPanel implements ListCellRenderer<Curso> {

	private static final long serialVersionUID = 1L;

	private static final int TAMANO_IMAGEN = 50;
	private static final int MARGEN = 10;
	private static final int MARGEN_VERTICAL = 4;
	private static final Color COLOR_AUTOR = new Color(100, 100, 100);
	private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 16);
	private static final Font FUENTE_NORMAL = new Font("SansSerif", Font.PLAIN, 12);

	private JLabel lblImagen;
	private JLabel lblTitulo;
	private JLabel lblAutor;
	private JTextArea txtDescripcion;
	private JPanel panelTexto;

	private static ImageIcon iconoCurso;

	public CursoCellRenderer() {
		inicializarComponentes();

		cargarIconoCurso();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(MARGEN, MARGEN));
		setBorder(BorderFactory.createEmptyBorder(MARGEN, MARGEN, MARGEN, MARGEN));
		setOpaque(true);

		// Imagen del curso
		lblImagen = new JLabel();
		lblImagen.setPreferredSize(new Dimension(TAMANO_IMAGEN, TAMANO_IMAGEN));
		lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
		lblImagen.setVerticalAlignment(SwingConstants.CENTER);
		lblImagen.setOpaque(false);

		// Panel derecho
		panelTexto = new JPanel();
		panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
		panelTexto.setBorder(BorderFactory.createEmptyBorder(0, MARGEN, 0, 0));
		panelTexto.setOpaque(false);

		lblTitulo = new JLabel();
		lblTitulo.setFont(FUENTE_TITULO);
		lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblTitulo.setOpaque(false);

		lblAutor = new JLabel();
		lblAutor.setFont(FUENTE_NORMAL);
		lblAutor.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblAutor.setOpaque(false);

		txtDescripcion = new JTextArea();
		txtDescripcion.setFont(FUENTE_NORMAL);
		txtDescripcion.setLineWrap(true);
		txtDescripcion.setWrapStyleWord(true);
		txtDescripcion.setOpaque(false);
		txtDescripcion.setEditable(false);
		txtDescripcion.setFocusable(false);
		txtDescripcion.setMargin(new Insets(0, 0, 0, 0));
		txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtDescripcion.setBorder(null);

		panelTexto.add(lblTitulo);
		panelTexto.add(Box.createVerticalStrut(MARGEN_VERTICAL));
		panelTexto.add(lblAutor);
		panelTexto.add(Box.createVerticalStrut(MARGEN_VERTICAL * 2));
		panelTexto.add(txtDescripcion);

		add(lblImagen, BorderLayout.WEST);
		add(panelTexto, BorderLayout.CENTER);
	}

	private void cargarIconoCurso() {
		if (iconoCurso == null) {
			String rutaIconoCurso = ConfiguracionTemulingo.getRutaIcono("curso.icono");
			ImageIcon icono = new ImageIcon(getClass().getResource(rutaIconoCurso));
			Image iconoEscalado = icono.getImage().getScaledInstance(TAMANO_IMAGEN, TAMANO_IMAGEN, Image.SCALE_SMOOTH);
			iconoCurso = new ImageIcon(iconoEscalado);
		}
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Curso> list, Curso curso, int index,
			boolean isSelected, boolean cellHasFocus) {

		if (curso == null) {
			return this;
		}

		lblImagen.setIcon(iconoCurso);
		lblTitulo.setText(curso.getTitulo() != null ? curso.getTitulo() : "Sin título");
		lblAutor.setText("Autor: " + (curso.getId() != null ? curso.getId() : "Desconocido"));
		txtDescripcion.setText(curso.getDescripcion() != null ? curso.getDescripcion() : "Sin descripción");

		Color backgroundColor;
		Color foregroundColor;
		Color authorColor;

		if (isSelected) {
			backgroundColor = list.getSelectionBackground();
			foregroundColor = list.getSelectionForeground();
			authorColor = list.getSelectionForeground();
		} else {
			backgroundColor = list.getBackground();
			foregroundColor = list.getForeground();
			authorColor = COLOR_AUTOR;
		}

		// Aplicar colores a todos los componentes
		setBackground(backgroundColor);
		panelTexto.setBackground(backgroundColor);

		lblTitulo.setForeground(foregroundColor);
		lblAutor.setForeground(authorColor);
		txtDescripcion.setForeground(foregroundColor);
		txtDescripcion.setBackground(backgroundColor);

		// Efecto visual para el foco
		if (cellHasFocus) {
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(list.getSelectionBackground().darker(), 1),
					BorderFactory.createEmptyBorder(MARGEN - 1, MARGEN - 1, MARGEN - 1, MARGEN - 1)));
		} else {
			setBorder(BorderFactory.createEmptyBorder(MARGEN, MARGEN, MARGEN, MARGEN));
		}

		return this;
	}

}