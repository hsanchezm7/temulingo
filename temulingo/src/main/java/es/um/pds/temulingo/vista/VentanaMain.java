package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Estadistica;
import es.um.pds.temulingo.utils.CursoCellRenderer;

public class VentanaMain extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final String FUNCION = "Inicio";
	private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE + " - " + FUNCION;

	private static final int MARGEN = 10;
	private static final int ANCHO_BOTON = 170;
	private static final int ALTO_BOTON = 50;
	private static final int ESPACIO_ICONO_TEXTO = 15;
	private static final int FILAS_VISIBLES_LISTA = 5;
	private static final int ANCHO_SCROLL_PANE = 500;

	private DefaultListModel<Curso> modelo = new DefaultListModel<>();
	private JList<Curso> listaCursos;
	private JButton btnEstadisticas;
	private JButton btnActualizar;
	private JButton btnImportar;
	private JButton btnIniciar;
	private JButton btnSalir;
	private JPanel panelListaCursos;
	private VentanaRealizarCurso ventanaRealizarCursoActual; // Para mantener la referencia a la ventana de curso abierta
	private JPanel panelWrapper;
	
	
	public VentanaMain() {
		cargarModelo();
		inicializarComponentes();
		configurarEventListeners();
	}

	private void cargarModelo() {
		modelo = new DefaultListModel<>();
		modelo.addAll(ControladorTemulingo.getInstance().getAllCursos());
	}

	private void inicializarComponentes() {
		setTitle(NOMBRE_VENTANA);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setJMenuBar(new BarraMenu(this));

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

		panelWrapper = new JPanel(new BorderLayout());
		panelWrapper.setBorder(new TitledBorder(null, "  Menú  ", TitledBorder.CENTER, TitledBorder.TOP));

		panelWrapper.add(crearPanelBotonesAccion(), BorderLayout.NORTH);
		
		panelListaCursos = crearPanelListaCursos();
		panelWrapper.add(panelListaCursos, BorderLayout.CENTER);

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

	    if (modelo.getSize() == 0) {
	        JLabel labelVacio = new JLabel("No hay ningún curso en la librería");
	        labelVacio.setHorizontalAlignment(SwingConstants.CENTER);
	        labelVacio.setFont(labelVacio.getFont().deriveFont(Font.ITALIC));
	        labelVacio.setForeground(Color.GRAY);
	        panel.add(labelVacio, BorderLayout.CENTER);
	    } else {
	        listaCursos = new JList<>(modelo);
	        listaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        listaCursos.setCellRenderer(new CursoCellRenderer());
	        
	        int filasVisibles = Math.min(modelo.getSize(), FILAS_VISIBLES_LISTA);
	        listaCursos.setVisibleRowCount(filasVisibles);

	        JScrollPane scrollPane = new JScrollPane(listaCursos);
	        scrollPane.setPreferredSize(new Dimension(ANCHO_SCROLL_PANE, scrollPane.getPreferredSize().height - modelo.getSize()));

	        panel.add(scrollPane, BorderLayout.CENTER);
	    }

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
		getRootPane().setDefaultButton(btnIniciar);
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
		btnEstadisticas.addActionListener(e -> abrirEstadisticas());
		btnActualizar.addActionListener(e -> actualizarListaCursos());
		btnImportar.addActionListener(e -> abrirImportarCurso());
		
		btnSalir.addActionListener(e -> gestionarSalida());
		btnIniciar.addActionListener(e -> iniciarCurso());
	}

	private void abrirImportarCurso() {
		DialogoImportarCurso ventanaImportar = new DialogoImportarCurso(this);
		ventanaImportar.setVisible(true);
		
		actualizarListaCursos();
	}
	
	private void abrirEstadisticas() {
		Estadistica estadisticas = ControladorTemulingo.getInstance().generarEstadisticas();
		DialogoEstadisticas ventanaEstadisticas = new DialogoEstadisticas(this, estadisticas);
		ventanaEstadisticas.setVisible(true);
	}
	
	public void actualizarListaCursos() {
		modelo.clear();
		modelo.addAll(ControladorTemulingo.getInstance().getAllCursos());
		
		// Recrear y actualizar la vista del panel de cursos
		panelWrapper.remove(panelListaCursos);
		panelListaCursos = crearPanelListaCursos();
		panelWrapper.add(panelListaCursos, BorderLayout.CENTER);
		
		panelWrapper.revalidate();
		panelWrapper.repaint();
		
		pack();
		setLocationRelativeTo(null);
	}

	private void iniciarCurso() {
		Curso cursoSeleccionado = obtenerCursoSeleccionado();
	    
	    // Verificar que hay un curso seleccionado
	    if (cursoSeleccionado == null) {
	        JOptionPane.showMessageDialog(this, 
	            "Por favor, selecciona un curso de la lista.", 
	            "Ningún curso seleccionado", 
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    if (ControladorTemulingo.getInstance().tieneProgresoGuardado(cursoSeleccionado)) {
            int opcion = JOptionPane.showConfirmDialog(this,
                "Ya tienes progreso guardado para este curso. ¿Deseas reanudarlo?",
                "Progreso detectado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) { // Reanudar
                ControladorTemulingo.getInstance().reanudarCurso(cursoSeleccionado);
                abrirVentanaRealizarCurso(cursoSeleccionado);
            } else { // Empezar uno nuevo: Ahora se abre un diálogo para seleccionar estrategia
                int confirmarNuevo = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que quieres empezar un curso nuevo? Esto borrará el progreso anterior.",
                    "Empezar curso nuevo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (confirmarNuevo == JOptionPane.YES_OPTION) {
                    ControladorTemulingo.getInstance().empezarCursoNuevo(cursoSeleccionado);
                    // Abrir el diálogo para seleccionar la estrategia
                    DialogoSeleccionEstrategia dialogoEstrategia = new DialogoSeleccionEstrategia(this, cursoSeleccionado);
                    dialogoEstrategia.setVisible(true);
                    
                    // Si el usuario selecciona una estrategia y no cancela, se abrirá VentanaRealizarCurso desde el diálogo
                    // La lógica del diálogo será: si se selecciona estrategia, llamar a setEstrategiaAprendizaje y luego abrir VentanaRealizarCurso
                    setEnabled(false);
                }
            }
        } else { // No hay progreso guardado, iniciar nuevo directamente con selección de estrategia
            ControladorTemulingo.getInstance().empezarCursoNuevo(cursoSeleccionado);
            DialogoSeleccionEstrategia dialogoEstrategia = new DialogoSeleccionEstrategia(this, cursoSeleccionado);
            dialogoEstrategia.setVisible(true);
            
            setEnabled(false);
        }
	}
	
	// Nuevo método para abrir VentanaRealizarCurso
    public void abrirVentanaRealizarCurso(Curso curso) {
    	VentanaRealizarCurso ventanaIniciar = new VentanaRealizarCurso(this, curso); // Pasar 'this' (VentanaMain)
    	this.ventanaRealizarCursoActual = ventanaIniciar;
    	ventanaIniciar.setVisible(true);
        this.setEnabled(false);
    }
    
 // Necesitas un método para hacer visible/habilitar VentanaMain cuando VentanaRealizarCurso se cierre
    public void ventanaRealizarCursoCerrada() {
        // setVisible(true); // Opción 1: Mostrar
        setEnabled(true); // Opción 2: Habilitar
        toFront(); // Poner VentanaMain al frente
        repaint(); // Asegurar que se repinte correctamente
       // actualizarInfoProgresoCurso();
        this.ventanaRealizarCursoActual = null; // <--- NEW: Clear the reference
    }

	
	public Curso obtenerCursoSeleccionado() {
		return listaCursos.getSelectedValue();
	}

	public void limpiarSeleccion() {
		listaCursos.clearSelection();
	}
	
	private void gestionarSalida() {
		int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas salir?", "Confirmar salida",
				JOptionPane.YES_NO_OPTION);

		if (respuesta == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}