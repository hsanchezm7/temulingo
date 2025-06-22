package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import es.um.pds.temulingo.config.ConfiguracionImagenes;
import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.config.ConfiguracionUI;
import es.um.pds.temulingo.config.ConfiguracionUI.CategoriasTema;
import es.um.pds.temulingo.config.ConfiguracionUI.Tema;

public class BarraMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;

	private final VentanaMain padre;

	public BarraMenu(VentanaMain padre) {
		this.padre = padre;

		crearMenus();
	}

	private void crearMenus() {
		// Menú Archivo
		JMenu menuArchivo = crearMenuArchivo();

		// Menú Ver
		JMenu menuVer = crearMenuVer();

		// Menú Ayuda
		JMenu menuAyuda = crearMenuAyuda();

		this.add(menuArchivo);
		this.add(menuVer);
		this.add(menuAyuda);
	}

	private JMenu crearMenuArchivo() {
		JMenu menuArchivo = new JMenu("Archivo");
		menuArchivo.setMnemonic(KeyEvent.VK_A);

		JMenuItem itemImportarCurso = new JMenuItem("Importar Curso");
		itemImportarCurso.setMnemonic(KeyEvent.VK_I);
		itemImportarCurso.addActionListener(e -> abrirImportarCurso());

		JMenuItem itemExportarCurso = new JMenuItem("Exportar Curso");
		itemExportarCurso.setMnemonic(KeyEvent.VK_E);
		itemExportarCurso.addActionListener(e -> abrirExportarCurso());

		menuArchivo.add(itemImportarCurso);
		menuArchivo.add(itemExportarCurso);

		return menuArchivo;
	}

	private JMenu crearMenuVer() {
		JMenu menuVer = new JMenu("Ver");
		menuVer.setMnemonic(KeyEvent.VK_V);

		JMenu submenuTema = crearSubmenuTemas();
		menuVer.add(submenuTema);

		return menuVer;
	}

	private JMenu crearSubmenuTemas() {
		JMenu submenuTema = new JMenu("Tema");
		ButtonGroup grupoTemas = new ButtonGroup();

		Tema temaActual = ConfiguracionUI.getTemaActual();
		CategoriasTema categoriaAnterior = null;

		for (Tema tema : Tema.values()) {
			if (categoriaAnterior != tema.getCategoria()) {
				if (categoriaAnterior != null) {
					submenuTema.add(new JSeparator());
				}

				JMenuItem tituloCategoria = new JMenuItem(tema.getCategoria().getTitulo());
				tituloCategoria.setEnabled(false);
				tituloCategoria.setFont(tituloCategoria.getFont().deriveFont(java.awt.Font.ITALIC,
						tituloCategoria.getFont().getSize() - 1));
				submenuTema.add(tituloCategoria);

				categoriaAnterior = tema.getCategoria();
			}

			JRadioButtonMenuItem itemTema = new JRadioButtonMenuItem("   " + tema.getNombre());
			itemTema.addActionListener(new TemaActionListener(tema));
			grupoTemas.add(itemTema);
			submenuTema.add(itemTema);

			if (tema.equals(temaActual)) {
				itemTema.setSelected(true);
			}
		}

		return submenuTema;
	}

	private JMenu crearMenuAyuda() {
		JMenu menuAyuda = new JMenu("Ayuda");
		menuAyuda.setMnemonic(KeyEvent.VK_Y);

		// Manual de usuario
		JMenuItem itemManual = new JMenuItem("Manual de usuario");
		itemManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		itemManual.addActionListener(e -> {
			try {
				URI uri = new URI("https://github.com/hsanchezm7/temulingo/blob/main/docs/manualUsuario.md");
				Desktop.getDesktop().browse(uri);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "No se pudo abrir el manual de usuario.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// Acerca de
		JMenuItem itemAcercaDe = new JMenuItem("Acerca de Temulingo");
		itemAcercaDe.addActionListener(e -> accionAcercaDe());

		menuAyuda.add(itemManual);
		menuAyuda.add(new JSeparator());
		menuAyuda.add(itemAcercaDe);

		return menuAyuda;
	}

	// Clase interna para manejar acciones de tema
	private class TemaActionListener implements ActionListener {
		private final Tema tema;

		public TemaActionListener(Tema tema) {
			this.tema = tema;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			cambiarTema(tema);
		}
	}

	public void cambiarTema(Tema tema) {
		try {
			ConfiguracionUI.cambiarTema(tema, padre);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(padre, "Error al cambiar el tema: " + e.getMessage(), "Error de Tema",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void accionAcercaDe() {
		// Cargar el logo
		ImageIcon logo = new ImageIcon(getClass().getResource(ConfiguracionImagenes.getRutaIcono("logo.icono")));
		JLabel labelLogo = new JLabel(logo);

		// Crear panel principal
		JPanel panel = new JPanel(new BorderLayout(10, 10));

		// Crear panel de texto con BoxLayout vertical
		JPanel panelTexto = new JPanel();
		panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));

		JLabel info = new JLabel(String.format("<html><h2>%s %s</h2><p>%s</p><p>%s</p></html>",
				ConfiguracionTemulingo.NOMBRE, ConfiguracionTemulingo.VERSION, ConfiguracionTemulingo.DESCRIPCION,
				ConfiguracionTemulingo.AUTOR));

		JLabel enlace = new JLabel("<html><a href=''>" + ConfiguracionTemulingo.URL + "</a></html>");
		enlace.setCursor(new Cursor(Cursor.HAND_CURSOR));
		enlace.setForeground(Color.BLUE);
		enlace.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(ConfiguracionTemulingo.URL));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		// Añadir texto y enlace al panel de texto
		panelTexto.add(info);
		panelTexto.add(Box.createVerticalStrut(5)); // espacio entre texto y enlace
		panelTexto.add(enlace);

		// Montar panel principal
		panel.add(labelLogo, BorderLayout.WEST);
		panel.add(panelTexto, BorderLayout.CENTER);

		// Mostrar el diálogo
		JOptionPane.showMessageDialog(padre, panel, "Acerca de Temulingo", JOptionPane.PLAIN_MESSAGE);

	}

	private void abrirExportarCurso() {
		DialogoExportarCurso ventanaExportar = new DialogoExportarCurso(padre);
		ventanaExportar.setVisible(true);
	}

	private void abrirImportarCurso() {
		DialogoImportarCurso ventanaImportar = new DialogoImportarCurso(padre);
		ventanaImportar.setVisible(true);

		padre.actualizarListaCursos();
	}

}