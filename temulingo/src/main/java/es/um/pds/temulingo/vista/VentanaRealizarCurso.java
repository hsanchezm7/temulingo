package es.um.pds.temulingo.vista;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Pregunta;

/**
 * Ventana principal para realizar un curso de Temulingo. Permite al usuario
 * seleccionar una estrategia de aprendizaje, navegar por los bloques del curso
 * y responder preguntas.
 */
public class VentanaRealizarCurso extends JFrame {
	private static final long serialVersionUID = 1L;

	// ========================================
	// CONSTANTES DE DISEÑO Y COLORES
	// ========================================

	private static final Color COLOR_PRIMARIO = new Color(74, 144, 226); // azul
	private static final Color COLOR_FONDO = new Color(248, 249, 250);
	private static final Color COLOR_CARD = Color.WHITE;
	private static final int DELAY_FEEDBACK = 2500;
	private static final int TIMER_INTERVAL = 1000;

	// ========================================
	// COMPONENTES DE LA INTERFAZ
	// ========================================

	private JPanel panelContenidoDinamico; // Contiene los paneles de bloque, pregunta o finalización
	private JLabel etiquetaEnunciadoPregunta; // Etiqueta para el enunciado de la pregunta
	private JButton btnGuardarSalir;
	private JButton btnPausar;
	private JLabel lblTiempoTranscurrido;
	private VentanaMain parentMainFrame; // Referencia a la VentanaMain padre

	// Paneles específicos (instancias de las nuevas clases)
	private PanelInformacionBloque panelInfoBloque;
	private JPanel panelBasePregunta; // Contenedor que alberga el PanelPreguntaDinamico
	private PanelPreguntaDinamico panelPreguntaDinamico; // El único panel para todos los tipos de preguntas
	private PanelFinalizacionCurso panelCursoFinalizado;

	// ========================================
	// VARIABLES DE ESTADO
	// ========================================

	private Curso curso;
	private Pregunta preguntaActual;
	private long tiempoInicio;
	private Timer cronometro;
	private boolean cursoPausado = false;

	private ControladorTemulingo controlador = ControladorTemulingo.getInstance();

	// ========================================
	// CONSTRUCTOR
	// ========================================

	/**
	 * Crea una nueva ventana para realizar el curso especificado.
	 * 
	 * @param curso El curso a realizar
	 */
	public VentanaRealizarCurso(VentanaMain parentFrame, Curso curso) {
		this.parentMainFrame = parentFrame;
		this.curso = curso;

		inicializarVentana();
		inicializarComponentes();
		iniciarCursoAutomaticamente();

	}

	// ========================================
	// MÉTODOS DE INICIALIZACIÓN
	// ========================================

	private void inicializarVentana() {
		setTitle("Temulingo - " + curso.getTitulo());
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(parentMainFrame);
		getContentPane().setBackground(COLOR_FONDO);
		
		setVisible(true); // Hacer visible primero
		setExtendedState(JFrame.NORMAL); // Asegurar que no esté minimizada
		setAlwaysOnTop(true); // Temporalmente siempre encima
		toFront(); // Traer al frente
		requestFocus(); // Dar foco

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (VentanaRealizarCurso.this.parentMainFrame != null) {
					VentanaRealizarCurso.this.parentMainFrame.ventanaRealizarCursoCerrada();
				}
			}
		});
	}

	/**
	 * Inicializa todos los componentes de la ventana.
	 */
	private void inicializarComponentes() {
		panelContenidoDinamico = new JPanel(new BorderLayout());
		panelContenidoDinamico.setOpaque(false);
		add(panelContenidoDinamico, BorderLayout.CENTER);

		inicializarPaneles();
		crearComponentesControl();
		add(crearPanelControl(), BorderLayout.SOUTH);

	}

	private void inicializarPaneles() {
		// Panel de información del bloque
		panelInfoBloque = new PanelInformacionBloque();
		panelInfoBloque.agregarListenerContinuar(e -> iniciarBloque());

		// Panel de preguntas
		panelPreguntaDinamico = new PanelPreguntaDinamico();
		panelPreguntaDinamico.agregarListenerAccion(e -> procesarRespuestaGeneral());
		panelBasePregunta = crearPanelBasePregunta();

		// Panel de finalización
		panelCursoFinalizado = new PanelFinalizacionCurso();
		panelCursoFinalizado.agregarListenerCerrar(e -> dispose());
	}

	private void crearComponentesControl() {
		btnGuardarSalir = crearBotonEstilizado("Guardar y Salir", COLOR_PRIMARIO);
		btnPausar = crearBotonEstilizado("Pausar", COLOR_PRIMARIO);
		lblTiempoTranscurrido = new JLabel("Tiempo: 00:00:00");

		btnGuardarSalir.addActionListener(e -> guardarYSalir());
		btnPausar.addActionListener(e -> pausarCurso());

		cronometro = new Timer(TIMER_INTERVAL, e -> actualizarTiempo());
	}

	private JButton crearBotonEstilizado(String texto, Color bgColor) {
		JButton button = new JButton(texto);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(bgColor);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setPreferredSize(new Dimension(150, 40));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(bgColor.brighter());
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(bgColor);
			}
		});
		return button;
	}

	private JPanel crearPanelControl() {
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		controlPanel.setBackground(COLOR_FONDO);
		controlPanel.add(lblTiempoTranscurrido);
		controlPanel.add(btnPausar);
		controlPanel.add(btnGuardarSalir);
		return controlPanel;
	}

	/**
	 * Crea el panel base que contendrá el enunciado de la pregunta y el panel de
	 * respuesta específico.
	 */
	private JPanel crearPanelBasePregunta() {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(new EmptyBorder(30, 30, 30, 30));

		JPanel tarjetaPregunta = new JPanel(new BorderLayout(15, 15));
		tarjetaPregunta.setBackground(COLOR_CARD);
		tarjetaPregunta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(230, 230, 230), 1), new EmptyBorder(25, 25, 25, 25)));

		etiquetaEnunciadoPregunta = new JLabel();
		etiquetaEnunciadoPregunta.setFont(new Font("Segoe UI", Font.BOLD, 20));
		etiquetaEnunciadoPregunta.setHorizontalAlignment(SwingConstants.CENTER);
		etiquetaEnunciadoPregunta.setForeground(new Color(51, 51, 51));
		tarjetaPregunta.add(etiquetaEnunciadoPregunta, BorderLayout.NORTH);

		// El panel dinámico de pregunta se añade aquí
		tarjetaPregunta.add(panelPreguntaDinamico, BorderLayout.CENTER);

		panel.add(tarjetaPregunta, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Inicia automáticamente la visualización del curso. Se llama al constructor.
	 */
	private void iniciarCursoAutomaticamente() {
		tiempoInicio = System.currentTimeMillis();
		cronometro.start();
		mostrarBloque();
	}

	// ========================================
	// NAVEGACIÓN DEL CURSO
	// ========================================

	/**
	 * Muestra la información del bloque actual y permite iniciarlo.
	 */
	private void mostrarBloque() {
		Pregunta siguientePregunta = controlador.getSiguientePregunta();

		if (siguientePregunta == null) {
			mostrarFinalizacion();
			return;
		}

		Bloque bloque = controlador.encontrarBloqueParaPregunta(siguientePregunta);
		if (bloque == null) {
			mostrarError("No se pudo encontrar el bloque para la siguiente pregunta.");
			return;
		}

		String progresoResumen = controlador.obtenerResumenProgreso(curso);
		panelInfoBloque.establecerInformacionBloque(bloque, progresoResumen);

		cambiarPanelContenidoPrincipal(panelInfoBloque);
	}

	/**
	 * Inicia las preguntas del bloque actual.
	 */
	private void iniciarBloque() {
		cambiarPanelContenidoPrincipal(panelBasePregunta);
		mostrarSiguientePregunta();
	}

	/**
	 * Muestra la siguiente pregunta del bloque actual. Si no hay más preguntas,
	 * avanza al siguiente bloque.
	 */
	public void mostrarSiguientePregunta() {
		preguntaActual = controlador.getSiguientePregunta();

		if (preguntaActual == null) {
			mostrarFinalizacion();
			return;
		}

		etiquetaEnunciadoPregunta.setText("<html><div style='text-align: center; width: 600px;'>"
				+ preguntaActual.getEnunciado() + "</div></html>");

		panelPreguntaDinamico.establecerPregunta(preguntaActual);
		panelPreguntaDinamico.habilitarEntrada(); // Asegurarse de que los controles estén habilitados
		cambiarPanelContenidoPrincipal(panelBasePregunta);
	}

	/**
	 * Muestra la pantalla de finalización del curso.
	 */
	private void mostrarFinalizacion() {
		panelCursoFinalizado.establecerMensajeFinalizacion(curso.getTitulo());
		cambiarPanelContenidoPrincipal(panelCursoFinalizado);
		cronometro.stop();
	}

	/**
	 * Cambia el panel visible en el área de contenido principal de la ventana.
	 * 
	 * @param nuevoPanel El panel a mostrar.
	 */
	private void cambiarPanelContenidoPrincipal(JPanel nuevoPanel) {
		panelContenidoDinamico.removeAll();
		panelContenidoDinamico.add(nuevoPanel, BorderLayout.CENTER);
		panelContenidoDinamico.revalidate();
		panelContenidoDinamico.repaint();
	}

	// ========================================
	// PROCESAMIENTO DE RESPUESTA
	// ========================================

	/**
	 * Procesa la respuesta del usuario obtenida del panel de pregunta actual.
	 */
	private void procesarRespuestaGeneral() {
		String respuestaUsuario = panelPreguntaDinamico.obtenerRespuesta();

		if (respuestaUsuario == null || respuestaUsuario.isEmpty()) {
			mostrarAdvertencia("Por favor, introduce una respuesta o selecciona una opción.");
			return;
		}

		ControladorTemulingo.RespuestaProcesada resultado = 
		        controlador.procesarRespuestaCompleta(preguntaActual, respuestaUsuario);


		panelPreguntaDinamico.mostrarFeedback(resultado.getFeedbackTexto(), resultado.isEsCorrecta());
		panelPreguntaDinamico.deshabilitarEntrada(); // Deshabilita los controles del panel de pregunta

		// Timer para avanzar a la siguiente pregunta después de un retraso
		Timer timerAvance = new Timer(DELAY_FEEDBACK, e -> {
			((Timer) e.getSource()).stop();
			determinarSiguienteAccion();
		});
		timerAvance.setRepeats(false);
		timerAvance.start();
	}

	private void determinarSiguienteAccion() {
		ControladorTemulingo.ResultadoNavegacion resultado = 
				controlador.determinarSiguienteAccion(preguntaActual);
			
			switch(resultado.getTipo()) {
				case SIGUIENTE_PREGUNTA:
					actualizarProgresoBloque();
					mostrarSiguientePregunta();
					break;
				case NUEVO_BLOQUE:
					mostrarBloque();
					break;
				case FINALIZAR:
					mostrarFinalizacion();
					break;
			}
	}

	// ========================================
	// MÉTODOS PARA GUARDAR/PAUSAR Y CRONÓMETRO
	// ========================================

	private void guardarYSalir() {
		detenerCronometro();
		controlador.actualizarTiempoSesion(tiempoInicio);

		if (controlador.guardarEstadoCurso()) {
			mostrarInformacion("Estado del curso guardado correctamente.\nPuedes continuar más tarde.");
			dispose();
		} else {
			mostrarError("Error al guardar el estado del curso.");
			iniciarCronometro();
		}
	}

	private void pausarCurso() {
		detenerCronometro();
		controlador.actualizarTiempoSesion(tiempoInicio);

		if (controlador.guardarEstadoCurso()) {
			cursoPausado = true;
			panelPreguntaDinamico.deshabilitarEntrada(); // Deshabilita los controles del panel de pregunta
			btnPausar.setText("Reanudar");
			for (java.awt.event.ActionListener al : btnPausar.getActionListeners()) {
				btnPausar.removeActionListener(al);
			}
			btnPausar.addActionListener(e -> reanudarCurso());
			mostrarInformacion("Curso pausado y guardado correctamente.");
		} else {
			mostrarError("Error al pausar el curso.");
			iniciarCronometro();
		}
	}

	private void reanudarCurso() {
		tiempoInicio = System.currentTimeMillis();
		iniciarCronometro();
		cursoPausado = false;
		panelPreguntaDinamico.habilitarEntrada(); // Habilitar los controles del panel de pregunta
		btnPausar.setText("Pausar");
		for (java.awt.event.ActionListener al : btnPausar.getActionListeners()) {
			btnPausar.removeActionListener(al);
		}
		btnPausar.addActionListener(e -> pausarCurso());
		mostrarInformacion("Curso reanudado.");
	}

	private void actualizarTiempo() {
		String tiempoFormateado = controlador.formatearTiempoTranscurrido(tiempoInicio);
		lblTiempoTranscurrido.setText(tiempoFormateado);
	}

	private void actualizarProgresoBloque() {
		if (panelInfoBloque != null) {
			Pregunta preguntaActual = controlador.getSiguientePregunta();
			if (preguntaActual != null) {
				Bloque bloqueActual = controlador.encontrarBloqueParaPregunta(preguntaActual);
				if (bloqueActual != null) {
					String progresoResumen = controlador.obtenerResumenProgreso(curso);
					panelInfoBloque.establecerInformacionBloque(bloqueActual, progresoResumen);
				}
			}
		}
	}

	private void detenerCronometro() {
		if (cronometro != null && cronometro.isRunning()) {
			cronometro.stop();
		}
	}

	private void iniciarCronometro() {
		if (cronometro != null && !cronometro.isRunning()) {
			cronometro.start();
		}
	}

	// Métodos para mostrar mensajes
	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void mostrarAdvertencia(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Campo vacío", JOptionPane.WARNING_MESSAGE);
	}

	private void mostrarInformacion(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void dispose() {
		if (cronometro != null) {
			cronometro.stop();
		}
		super.dispose();
	}

	public JFrame getParentFrame() {
		return parentMainFrame;
	}
}