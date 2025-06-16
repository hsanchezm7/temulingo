package es.um.pds.temulingo.vista;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.PreguntaTest;
import es.um.pds.temulingo.logic.PreguntaHuecos;
import es.um.pds.temulingo.logic.PreguntaTraduccion;

// TODO: En alguna parte de la vista, poner la el numero de pregunta actual y el numero totales de preguntas, o usar un JProgressBar

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
	private static final Color COLOR_SECUNDARIO = new Color(52, 120, 199);
	private static final Color COLOR_EXITO = new Color(46, 174, 52);// verde
	private static final Color COLOR_ERROR = new Color(220, 53, 69);// rojo
	private static final Color COLOR_FONDO = new Color(248, 249, 250);
	private static final Color COLOR_CARD = Color.WHITE;


	// ========================================
	// COMPONENTES DE LA INTERFAZ 
	// ========================================

	private JPanel panelContenidoDinamico; // Contiene los paneles de bloque, pregunta o finalización
	private JLabel etiquetaEnunciadoPregunta; // Etiqueta para el enunciado de la pregunta
	
	private JButton btnGuardarSalir;
	private JButton btnPausar;
	private JLabel lblTiempoTranscurrido; // Optional: To display elapsed time
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
	
	private final ActionListener listenerProcesarRespuesta = e -> procesarRespuestaGeneral();


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
		setLocationRelativeTo(parentFrame);
		this.curso = curso;
		inicializarComponentes();
		iniciarCursoAutomaticamente();

	}

	// ========================================
	// MÉTODOS DE INICIALIZACIÓN
	// ========================================

	/**
	 * Inicializa todos los componentes de la ventana.
	 */
	private void inicializarComponentes() {
		setTitle("Temulingo - " + curso.getTitulo());
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		getContentPane().setBackground(COLOR_FONDO);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (VentanaRealizarCurso.this.parentMainFrame != null) {
					VentanaRealizarCurso.this.parentMainFrame.ventanaRealizarCursoCerrada();
				}
			}
		});

		


		panelContenidoDinamico = new JPanel(new BorderLayout());
		panelContenidoDinamico.setOpaque(false);
		add(panelContenidoDinamico, BorderLayout.CENTER);

		panelInfoBloque = new PanelInformacionBloque();
		panelInfoBloque.agregarListenerContinuar(e -> iniciarBloque());

		// Inicializar el panel dinámico de preguntas y su contenedor
		panelPreguntaDinamico = new PanelPreguntaDinamico();
		panelPreguntaDinamico.agregarListenerAccion(listenerProcesarRespuesta); // Añadir el listener principal
		panelBasePregunta = crearPanelBasePregunta();
		
		panelCursoFinalizado = new PanelFinalizacionCurso();
		panelCursoFinalizado.agregarListenerCerrar(e -> dispose());

		
		btnGuardarSalir = crearBotonEstilizado("Guardar y Salir", COLOR_PRIMARIO);
		btnPausar = crearBotonEstilizado("Pausar", COLOR_PRIMARIO);
		lblTiempoTranscurrido = new JLabel("Tiempo: 00:00");

		btnGuardarSalir.addActionListener(e -> guardarYSalir());
		btnPausar.addActionListener(e -> pausarCurso());

		tiempoInicio = System.currentTimeMillis();
		cronometro = new Timer(1000, e -> actualizarTiempo());

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		controlPanel.setBackground(COLOR_FONDO);
		controlPanel.add(lblTiempoTranscurrido); 
		controlPanel.add(btnPausar);
		controlPanel.add(btnGuardarSalir);

		// Añadir un botón para cambiar la estrategia si es necesario durante el curso
		/*JButton btnCambiarEstrategia = crearBotonEstilizado("Cambiar Estrategia", new Color(100, 100, 100));
		btnCambiarEstrategia.addActionListener(e -> {
			if (cronometro != null && cronometro.isRunning()) {
				cronometro.stop();
			}
			long tiempoSesion = System.currentTimeMillis() - tiempoInicio;
            ControladorTemulingo.getInstance().pausarCurso(tiempoSesion);
            
			DialogoSeleccionEstrategia dialogoEstrategia = new DialogoSeleccionEstrategia(this, this.curso);
			dialogoEstrategia.setVisible(true);
			
			reanudarCurso(); 
			mostrarSiguientePregunta(); 
		});
		controlPanel.add(btnCambiarEstrategia, 0);
*/
		add(controlPanel, BorderLayout.SOUTH);

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
	
	/**
	 * Crea el panel base que contendrá el enunciado de la pregunta y el panel de respuesta específico.
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
	// LÓGICA DE NAVEGACIÓN ENTRE ESTADOS DEL CURSO
	// ========================================

	

	/**
	 * Muestra la información del bloque actual y permite iniciarlo.
	 */
	private void mostrarBloque() {
		Pregunta siguientePregunta = ControladorTemulingo.getInstance().getSiguientePregunta();
		
		if (siguientePregunta == null) {
			mostrarFinalizacion();
			return;
		}

		Bloque bloqueDelMomento = null;
		for (Bloque b : curso.getBloques()) {
			if (b.getPreguntas().contains(siguientePregunta)) {
				bloqueDelMomento = b;
				break;
			}
		}

		if (bloqueDelMomento == null) {
			JOptionPane.showMessageDialog(this, "Error: No se pudo encontrar el bloque para la siguiente pregunta.",
					"Error de Curso", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

	
		String progresoResumen = ControladorTemulingo.getInstance().obtenerResumenProgreso(curso);
		panelInfoBloque.establecerInformacionBloque(bloqueDelMomento, progresoResumen);

		cambiarPanelContenidoPrincipal(panelInfoBloque);
	}

	

	
	// ========================================
	// LÓGICA DE NAVEGACIÓN
	// ========================================

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
		preguntaActual = ControladorTemulingo.getInstance().getSiguientePregunta();

		if (preguntaActual == null) {
			mostrarFinalizacion();
			return;
		}

	
		etiquetaEnunciadoPregunta.setText("<html><div style='text-align: center; width: 600px;'>" + preguntaActual.getEnunciado()
				+ "</div></html>");
		
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
	 * @param nuevoPanel El panel a mostrar.
	 */
	private void cambiarPanelContenidoPrincipal(JPanel nuevoPanel) {
		panelContenidoDinamico.removeAll();
		panelContenidoDinamico.add(nuevoPanel, BorderLayout.CENTER);
		panelContenidoDinamico.revalidate();
		panelContenidoDinamico.repaint();
	}

	// ========================================
	// CREACIÓN DE PANELES DE RESPUESTA
	// ========================================

	
	/**
	 * Procesa la respuesta del usuario obtenida del panel de pregunta actual.
	 */
	private void procesarRespuestaGeneral() {
		String respuestaUsuario = panelPreguntaDinamico.obtenerRespuesta();
		
		if (respuestaUsuario == null || respuestaUsuario.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, introduce una respuesta o selecciona una opción.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
			return;
		}

		boolean esCorrecta = ControladorTemulingo.getInstance().resolverPregunta(preguntaActual, respuestaUsuario);

		String feedbackTexto;
		if (esCorrecta) {
			feedbackTexto = "¡Correcto! Muy bien.";
		} else {
			String respuestaCorrecta = obtenerRespuestaCorrecta(preguntaActual);
			feedbackTexto = "Incorrecto. La respuesta correcta era: " + respuestaCorrecta;
		}

		panelPreguntaDinamico.mostrarFeedback(feedbackTexto, esCorrecta);
		panelPreguntaDinamico.deshabilitarEntrada(); // Deshabilita los controles del panel de pregunta

		// Timer para avanzar a la siguiente pregunta después de un retraso
	    Timer timerAvance = new Timer(2500, e -> {
	        ((Timer) e.getSource()).stop();
	        
	        // Primero, obtenemos el bloque de la pregunta que acabamos de responder
	        Bloque bloqueActual = null;
	        for (Bloque b : curso.getBloques()) {
	            if (b.getPreguntas().contains(preguntaActual)) {
	                bloqueActual = b;
	                break;
	            }
	        }

	        // Ahora verificamos si hay más preguntas
	        Pregunta proximaPregunta = ControladorTemulingo.getInstance().getSiguientePregunta();

	        if (proximaPregunta == null) {
	            // No hay más preguntas, el curso ha terminado
	            mostrarFinalizacion(); 
	        } else {
	            // Hay una próxima pregunta, verificamos si está en el mismo bloque
	            Bloque bloqueProximaPregunta = null;
	            for (Bloque b : curso.getBloques()) {
	                if (b.getPreguntas().contains(proximaPregunta)) {
	                    bloqueProximaPregunta = b;
	                    break;
	                }
	            }

	            if (bloqueActual != null && bloqueProximaPregunta != null && 
	                bloqueActual != bloqueProximaPregunta) {
	                mostrarBloque(); // Muestra información del nuevo bloque
	            } else {
	                // Mismo bloque, continuamos con la siguiente pregunta
	            	actualizarProgresoBloque();
	            	mostrarSiguientePregunta();
	            }
	        }
	    });
		timerAvance.setRepeats(false);
		timerAvance.start();
	}


	/**
	 * Obtiene la respuesta correcta de una pregunta según su tipo.
	 * 
	 * @param pregunta La pregunta de la cual obtener la respuesta
	 * @return String con la respuesta correcta
	 */
	private String obtenerRespuestaCorrecta(Pregunta pregunta) {
		if (pregunta instanceof PreguntaTest) {
			return ((PreguntaTest) pregunta).getSolucion();
		} else if (pregunta instanceof PreguntaHuecos) {
			return ((PreguntaHuecos) pregunta).getSolucion();
		} else if (pregunta instanceof PreguntaTraduccion) {
			return ((PreguntaTraduccion) pregunta).getSolucion();
		}
		return "Respuesta no disponible";
	}


	// ========================================
	// MÉTODOS PARA GUARDAR/PAUSAR Y CRONÓMETRO
	// ========================================

	private void guardarYSalir() {
		// Detener el cronómetro antes de guardar
		if (cronometro != null && cronometro.isRunning()) {
			cronometro.stop();
		}

		long tiempoSesionActual = System.currentTimeMillis() - tiempoInicio;

		boolean guardado = ControladorTemulingo.getInstance().pausarCurso(tiempoSesionActual);

		if (guardado) {
			JOptionPane.showMessageDialog(this, "Estado del curso guardado correctamente.\nPuedes continuar más tarde.",
					"Guardado Exitoso", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Error al guardar el estado del curso.", "Error",
					JOptionPane.ERROR_MESSAGE);
			// Si falló el guardado, reiniciar el cronómetro
			if (cronometro != null && !cronometro.isRunning()) {
				cronometro.start();
			}
		}
	}

	private void pausarCurso() {
		if (cronometro != null && cronometro.isRunning()) {
			cronometro.stop();
		}

		// CORRECCIÓN: Calcular solo el tiempo de la sesión actual
		long tiempoSesionActual = System.currentTimeMillis() - tiempoInicio;

		boolean pausado = ControladorTemulingo.getInstance().pausarCurso(tiempoSesionActual);

		if (pausado) {
			panelPreguntaDinamico.deshabilitarEntrada(); // Deshabilita los controles del panel de pregunta

			JOptionPane.showMessageDialog(this, "Curso pausado y guardado correctamente.", "Curso Pausado",
					JOptionPane.INFORMATION_MESSAGE);

			btnPausar.setText("Reanudar");
			for (java.awt.event.ActionListener al : btnPausar.getActionListeners()) { // <--- MODIFICADO
				btnPausar.removeActionListener(al);
			}
			btnPausar.addActionListener(e -> reanudarCurso());

		} else {
			JOptionPane.showMessageDialog(this, "Error al pausar el curso.", "Error", JOptionPane.ERROR_MESSAGE);

			// Si falló la pausa, reiniciar el cronómetro
			if (cronometro != null && !cronometro.isRunning()) {
				cronometro.start();
			}
		}
	}

	private void reanudarCurso() {
		// Reiniciar el tiempo de inicio para la nueva sesión
		tiempoInicio = System.currentTimeMillis();

		// Reiniciar el cronómetro
		if (cronometro != null && !cronometro.isRunning()) {
			cronometro.start();
		}

		panelPreguntaDinamico.habilitarEntrada(); // Habilitar los controles del panel de pregunta


		// Cambiar el botón de vuelta a "Pausar"
		btnPausar.setText("Pausar");
		for (java.awt.event.ActionListener al : btnPausar.getActionListeners()) {
			btnPausar.removeActionListener(al);
		}
		btnPausar.addActionListener(e -> pausarCurso());

		JOptionPane.showMessageDialog(this, "Curso reanudado.", "Curso Reanudado", JOptionPane.INFORMATION_MESSAGE);
	}

	private void actualizarTiempo() {
		if (ControladorTemulingo.getInstance().getCursoActual() == null) {
			cronometro.stop();
			lblTiempoTranscurrido.setText("Tiempo: --:--:--");
			return;
		}

		long tiempoTranscurridoAcumulado = ControladorTemulingo.getInstance().getCursoActual().getTiempoTranscurrido();
		long tiempoActualSesion = System.currentTimeMillis() - tiempoInicio;
		long tiempoTotal = tiempoTranscurridoAcumulado + tiempoActualSesion;

		long segundos = (tiempoTotal / 1000) % 60;
		long minutos = (tiempoTotal / (1000 * 60)) % 60;
		long horas = (tiempoTotal / (1000 * 60 * 60));

		String tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos);
		lblTiempoTranscurrido.setText("Tiempo: " + tiempoFormateado);

	}
	
	private void actualizarProgresoBloque() {
	    if (panelInfoBloque != null) {
	        Pregunta preguntaActual = ControladorTemulingo.getInstance().getSiguientePregunta();
	        if (preguntaActual != null) {
	            Bloque bloqueActual = null;
	            for (Bloque b : curso.getBloques()) {
	                if (b.getPreguntas().contains(preguntaActual)) {
	                    bloqueActual = b;
	                    break;
	                }
	            }
	            
	            if (bloqueActual != null) {
	                String progresoResumen = ControladorTemulingo.getInstance().obtenerResumenProgreso(curso);
	                panelInfoBloque.establecerInformacionBloque(bloqueActual, progresoResumen);
	            }
	        }
	    }
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