package es.um.pds.temulingo.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.logic.Bloque;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.PreguntaTest;
import es.um.pds.temulingo.logic.PreguntaHuecos;
import es.um.pds.temulingo.logic.PreguntaTraduccion;
import es.um.pds.temulingo.logic.Curso.EstrategiaAprendizaje;

// TODO: En alguna parte de la vista, poner la el numero de pregunta actual y el numero totales de preguntas, o usar un JProgressBar

/**
 * Ventana principal para realizar un curso de Temulingo.
 * Permite al usuario seleccionar una estrategia de aprendizaje,
 * navegar por los bloques del curso y responder preguntas.
 */
public class VentanaRealizarCurso extends JFrame {
	private static final long serialVersionUID = 1L;

	 // ========================================
    // CONSTANTES DE DISEÑO Y COLORES
    // ========================================
	
	private static final Color COLOR_PRIMARIO = new Color(74, 144, 226); //azul
	private static final Color COLOR_SECUNDARIO = new Color(52, 120, 199);
	private static final Color COLOR_EXITO = new Color(46, 174, 52);//verde
	private static final Color COLOR_ERROR = new Color(220, 53, 69);//rojo
	private static final Color COLOR_FONDO = new Color(248, 249, 250);
	private static final Color COLOR_CARD = Color.WHITE;

	// Colores para opciones de test
	private static final Color[] COLORES_OPCIONES = { 
			new Color(255, 107, 107), // Rojo suave
			new Color(54, 162, 235), // Azul
			new Color(255, 206, 84), // Amarillo
			new Color(75, 192, 192) // Verde agua
	};
	
	// ========================================
    // COMPONENTES DE LA INTERFAZ
    // ========================================
	
	private JLabel lblPregunta;
	private JTextField txtRespuesta;
	private JButton btnResponder;
	private JLabel lblFeedback;
	private JPanel panelSeleccionEstrategia;
	private JPanel panelPregunta;
	private JPanel panelBloque;
	private JPanel panelFinalizacion;

	 // ========================================
    // VARIABLES DE ESTADO
    // ========================================
    
	private Curso curso;
	private Pregunta preguntaActual;
	private boolean cursoIniciado = false;
	private int bloqueActual = 0;
	private int preguntaActualEnBloque = 0;

	// ========================================
    // CONSTRUCTOR
    // ========================================
    
    /**
     * Crea una nueva ventana para realizar el curso especificado.
     * 
     * @param curso El curso a realizar
     */
	public VentanaRealizarCurso(Curso curso) {
		this.curso = curso;
		inicializarComponentes();
		mostrarSeleccionEstrategia();
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

		// Panels
		panelSeleccionEstrategia = crearPanelSeleccionEstrategia();
		panelBloque = crearPanelBloque();
		panelPregunta = crearPanelPregunta();
		panelFinalizacion = crearPanelFinalizacion();
	}

	/**
     * Muestra el panel de selección de estrategia de aprendizaje.
     */
	private void mostrarSeleccionEstrategia() {
		getContentPane().removeAll();
		add(panelSeleccionEstrategia, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	 // ========================================
    // CREACIÓN DE PANELES PRINCIPALES
    // ========================================
    
    /**
     * Crea el panel donde el usuario selecciona la estrategia de aprendizaje.
     * 
     * @return Panel de selección de estrategia
     */
	private JPanel crearPanelSeleccionEstrategia() {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(new EmptyBorder(40, 40, 40, 40));

		// Título
		JLabel lblTitulo = new JLabel("Selecciona tu estrategia de aprendizaje");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(COLOR_PRIMARIO);

		// Panel con botones para cada estrategia
		JPanel panelBotones = new JPanel(new GridLayout(0, 1, 15, 15));
		panelBotones.setOpaque(false);

		// Crear un botón para cada estrategia disponible
		for (EstrategiaAprendizaje estrategia : EstrategiaAprendizaje.values()) {
			JButton btnEstrategia = crearBotonEstrategia(estrategia);
			panelBotones.add(btnEstrategia);
		}

		panel.add(lblTitulo, BorderLayout.NORTH);
		panel.add(panelBotones, BorderLayout.CENTER);

		return panel;
	}

	/**
     * Crea un botón estilizado para una estrategia de aprendizaje.
     * 
     * @param estrategia La estrategia para la que crear el botón
     * @return Botón configurado para la estrategia
     */
	private JButton crearBotonEstrategia(EstrategiaAprendizaje estrategia) {
		JButton btn = new JButton(getTextoEstrategia(estrategia));
		btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btn.setForeground(Color.WHITE);
		btn.setBackground(COLOR_PRIMARIO);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setPreferredSize(new Dimension(400, 60));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Efecto hover
		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btn.setBackground(COLOR_SECUNDARIO);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				btn.setBackground(COLOR_PRIMARIO);
			}
		});

		btn.addActionListener(e -> seleccionarEstrategia(estrategia));
		return btn;
	}

	/**
     * Obtiene el texto descriptivo para mostrar en el botón de una estrategia.
     * 
     * @param estrategia La estrategia
     * @return Texto descriptivo con emoji
     */
	private String getTextoEstrategia(EstrategiaAprendizaje estrategia) {
		switch (estrategia) {
		case SECUENCIAL:
			return "SECUENCIAL - Orden establecido";
		case ALEATORIA:
			return "ALEATORIO - Orden aleatorio";
		case REPETICION_ESPACIADA:
			return "REPETICIÓN ESPACIADA - Repaso inteligente";
		default:
			return estrategia.toString();
		}
	}

	/**
     * Crea el panel base para mostrar información de bloques.
     * 
     * @return Panel de bloque vacío
     */
	private JPanel crearPanelBloque() {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(new EmptyBorder(40, 40, 40, 40));

		return panel;
	}

	/**
     * Muestra la información del bloque actual y permite iniciarlo.
     */
	private void mostrarBloque() {
		if (bloqueActual >= curso.getBloques().size()) {
			mostrarFinalizacion();
			return;
		}

		Bloque bloque = curso.getBloques().get(bloqueActual);
		panelBloque.removeAll();

		// Crear tarjeta principal del bloque
		JPanel card = new JPanel(new BorderLayout(20, 20));
		card.setBackground(COLOR_CARD);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
				new EmptyBorder(30, 30, 30, 30)));

		// Título del bloque
		JLabel lblTitulo = new JLabel(bloque.getNombre());
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblTitulo.setForeground(COLOR_PRIMARIO);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		// Descripción
		JLabel lblDescripcion = new JLabel(
				"<html><div style='text-align: center; width: 500px;'>" + bloque.getDescripcion() + "</div></html>");
		lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblDescripcion.setForeground(Color.GRAY);
		lblDescripcion.setHorizontalAlignment(SwingConstants.CENTER);

		// Información del progreso
		JLabel lblProgreso = new JLabel("Bloque " + (bloqueActual + 1) + " de " + curso.getBloques().size());
		lblProgreso.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblProgreso.setForeground(Color.GRAY);
		lblProgreso.setHorizontalAlignment(SwingConstants.CENTER);

		// Botón continuar
		JButton btnContinuar = new JButton("Comenzar Bloque");
		btnContinuar.setFont(new Font("Segoe UI", Font.BOLD, 18));
		btnContinuar.setForeground(Color.WHITE);
		btnContinuar.setBackground(COLOR_EXITO);
		btnContinuar.setFocusPainted(false);
		btnContinuar.setBorderPainted(false);
		btnContinuar.setPreferredSize(new Dimension(250, 50));
		btnContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnContinuar.addActionListener(e -> iniciarBloque());

		// Layout
		JPanel panelTexto = new JPanel(new BorderLayout(10, 10));
		panelTexto.setOpaque(false);
		panelTexto.add(lblTitulo, BorderLayout.NORTH);
		panelTexto.add(lblDescripcion, BorderLayout.CENTER);
		panelTexto.add(lblProgreso, BorderLayout.SOUTH);

		JPanel panelBoton = new JPanel(new FlowLayout());
		panelBoton.setOpaque(false);
		panelBoton.add(btnContinuar);

		card.add(panelTexto, BorderLayout.CENTER);
		card.add(panelBoton, BorderLayout.SOUTH);

		panelBloque.add(card, BorderLayout.CENTER);

		getContentPane().removeAll();
		add(panelBloque, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	/**
     * Crea el panel base para mostrar preguntas.
     * 
     * @return Panel de pregunta
     */
	private JPanel crearPanelPregunta() {
	    JPanel panel = new JPanel(new BorderLayout(20, 20));
	    panel.setBackground(COLOR_FONDO);
	    panel.setBorder(new EmptyBorder(30, 30, 30, 30));

	 // Crear tarjeta para la pregunta
	    JPanel cardPregunta = new JPanel(new BorderLayout(15, 15));
	    cardPregunta.setBackground(COLOR_CARD);
	    cardPregunta.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(new Color(230, 230, 230), 1), 
	            new EmptyBorder(25, 25, 25, 25)));

	    lblPregunta = new JLabel();
	    lblPregunta.setFont(new Font("Segoe UI", Font.BOLD, 20));
	    lblPregunta.setHorizontalAlignment(SwingConstants.CENTER);
	    lblPregunta.setForeground(new Color(51, 51, 51));

	    lblFeedback = new JLabel("");
	    lblFeedback.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    lblFeedback.setHorizontalAlignment(SwingConstants.CENTER);

	    cardPregunta.add(lblPregunta, BorderLayout.NORTH);
	    cardPregunta.add(lblFeedback, BorderLayout.SOUTH);
	 // El centro se añade dinámicamente en mostrarSiguientePregunta()

	    panel.add(cardPregunta, BorderLayout.CENTER);
	    return panel;
	}

	/**
     * Crea el panel de finalización del curso.
     * 
     * @return Panel de finalización
     */
	private JPanel crearPanelFinalizacion() {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(new EmptyBorder(40, 40, 40, 40));

		// Tarjeta de felicitación
		JPanel card = new JPanel(new BorderLayout(20, 20));
		card.setBackground(COLOR_CARD);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
				new EmptyBorder(40, 40, 40, 40)));

		JLabel lblTitulo = new JLabel("¡Enhorabuena!");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
		lblTitulo.setForeground(COLOR_EXITO);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblMensaje = new JLabel("<html><div style='text-align: center;'>Has completado <br><strong>"
				+ curso.getTitulo() + "</strong></div></html>");
		lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblMensaje.setForeground(new Color(51, 51, 51));

		JButton btnCerrar = new JButton("Finalizar");
		btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnCerrar.setForeground(Color.WHITE);
		btnCerrar.setBackground(COLOR_PRIMARIO);
		btnCerrar.setFocusPainted(false);
		btnCerrar.setBorderPainted(false);
		btnCerrar.setPreferredSize(new Dimension(150, 45));
		btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCerrar.addActionListener(e -> dispose());

		JPanel panelTexto = new JPanel(new BorderLayout(15, 15));
		panelTexto.setOpaque(false);
		panelTexto.add(lblTitulo, BorderLayout.NORTH);
		panelTexto.add(lblMensaje, BorderLayout.CENTER);

		JPanel panelBoton = new JPanel(new FlowLayout());
		panelBoton.setOpaque(false);
		panelBoton.add(btnCerrar);

		card.add(panelTexto, BorderLayout.CENTER);
		card.add(panelBoton, BorderLayout.SOUTH);

		panel.add(card, BorderLayout.CENTER);
		return panel;
	}
	
	// ========================================
    // LÓGICA DE NAVEGACIÓN
    // ========================================

	/**
     * Selecciona una estrategia de aprendizaje e inicia el curso.
     * 
     * @param estrategia La estrategia seleccionada
     */
	private void seleccionarEstrategia(EstrategiaAprendizaje estrategia) {
		ControladorTemulingo.getInstance().iniciarCurso(curso);
		ControladorTemulingo.getInstance().setEstrategiaAprendizaje(estrategia);

		// Inicializar estado del curso
		cursoIniciado = true;
		bloqueActual = 0;
		preguntaActualEnBloque = 0;

		mostrarBloque();
	}

	 /**
     * Inicia las preguntas del bloque actual.
     */
	private void iniciarBloque() {
		preguntaActualEnBloque = 0;
		getContentPane().removeAll();
		add(panelPregunta, BorderLayout.CENTER);
		mostrarSiguientePregunta();
		revalidate();
		repaint();
	}

	/**
     * Muestra la siguiente pregunta del bloque actual.
     * Si no hay más preguntas, avanza al siguiente bloque.
     */
	private void mostrarSiguientePregunta() {
	    if (!cursoIniciado)
	        return;

	    // Verificar si hemos terminado el bloque actual
	    Bloque bloqueActualObj = curso.getBloques().get(bloqueActual);
	    int totalPreguntasBloque = bloqueActualObj.getPreguntas().size();
	    
	 // En repetición espaciada, cada pregunta se repite 1 vez (total = preguntas * 2)
	    EstrategiaAprendizaje estrategiaActual = ControladorTemulingo.getInstance().getEstrategiaAprendizajeActual();
	    if (estrategiaActual == EstrategiaAprendizaje.REPETICION_ESPACIADA) {
	        totalPreguntasBloque *= 2;
	    }
	    
	    if (preguntaActualEnBloque >= totalPreguntasBloque) {
	        bloqueActual++;
	        mostrarBloque();
	        return;
	    }
	    // Obtener la pregunta actual
	    preguntaActual = ControladorTemulingo.getInstance().getSiguientePregunta();
	    
	 // Si no hay más preguntas, mostrar finalización
	    if (preguntaActual == null) {
	        mostrarFinalizacion();
	        return;
	    }
	    
	   /* // ===== DEBUGGING COMPLETO =====
	    System.out.println("=== DEBUG PREGUNTA ===");
	    System.out.println("Pregunta actual: " + preguntaActual.getClass().getSimpleName());
	    System.out.println("Enunciado: " + preguntaActual.getEnunciado());
	    System.out.println("Solución: " + preguntaActual.getSolucion());
	    
	    if (preguntaActual instanceof PreguntaTest) {
	        PreguntaTest preguntaTest = (PreguntaTest) preguntaActual;
	        System.out.println("Es PreguntaTest - Opciones disponibles: " + preguntaTest.getOpciones().size());
	        for (int i = 0; i < preguntaTest.getOpciones().size(); i++) {
	            System.out.println("  Opción " + i + ": '" + preguntaTest.getOpciones().get(i) + "'");
	        }
	        
	        // VERIFICAR SI LAS OPCIONES ESTÁN NULAS O VACÍAS
	        if (preguntaTest.getOpciones() == null) {
	            System.out.println("ERROR: getOpciones() devuelve NULL!");
	        } else if (preguntaTest.getOpciones().isEmpty()) {
	            System.out.println("ERROR: getOpciones() está vacía!");
	        }
	    }
	    System.out.println("=====================");
*/
	 // Recrear completamente el panel de pregunta
	    panelPregunta.removeAll();
	    
	 // Crear tarjeta para la pregunta
	    JPanel cardPregunta = new JPanel(new BorderLayout(15, 15));
	    cardPregunta.setBackground(COLOR_CARD);
	    cardPregunta.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(new Color(230, 230, 230), 1), 
	        new EmptyBorder(25, 25, 25, 25)));

	 // Actualizar el enunciado de la pregunta
	    lblPregunta.setText("<html><div style='text-align: center; width: 600px;'>" + 
	                       preguntaActual.getEnunciado() + "</div></html>");
	    lblFeedback.setText("");

	    // Agregar componentes al card
	    cardPregunta.add(lblPregunta, BorderLayout.NORTH);
	    
	    // Crear panel de respuesta según el tipo
	    JPanel panelRespuesta = crearPanelRespuestaSegunTipo(preguntaActual);
	    cardPregunta.add(panelRespuesta, BorderLayout.CENTER);
	    
	    cardPregunta.add(lblFeedback, BorderLayout.SOUTH);

	    // Agregar al panel principal
	    panelPregunta.add(cardPregunta, BorderLayout.CENTER);

	    revalidate();
	    repaint();
	}
	
	// ========================================
    // CREACIÓN DE PANELES DE RESPUESTA
    // ========================================
    
	 /**
     * Crea el panel de respuesta apropiado según el tipo de pregunta.
     * 
     * @param pregunta La pregunta para la que crear el panel
     * @return Panel de respuesta configurado
     */
	private JPanel crearPanelRespuestaSegunTipo(Pregunta pregunta) {
		if (pregunta instanceof PreguntaTest) {
			return crearPanelPreguntaTest((PreguntaTest) pregunta);
		} else if (pregunta instanceof PreguntaHuecos) {
			return crearPanelPreguntaHuecos((PreguntaHuecos) pregunta);
		} else if (pregunta instanceof PreguntaTraduccion) {
			return crearPanelPreguntaTraduccion((PreguntaTraduccion) pregunta);
		}
		return new JPanel();
	}

	/**
     * Crea el panel para una pregunta tipo test con opciones múltiples.
     * 
     * @param pregunta La pregunta test
     * @return Panel con opciones de respuesta
     */
	private JPanel crearPanelPreguntaTest(PreguntaTest pregunta) {
	    JPanel panel = new JPanel(new BorderLayout(20, 20));
	    panel.setOpaque(false);
	    
	    // Verificar que existen opciones
	    if (pregunta.getOpciones() == null || pregunta.getOpciones().isEmpty()) {
	    	return crearPanelError();
	    }
	    // Panel para las opciones
	    JPanel panelOpciones = new JPanel(new GridLayout(pregunta.getOpciones().size(), 1, 0, 15));
	    panelOpciones.setOpaque(false);

	    ButtonGroup grupo = new ButtonGroup();

	    for (int i = 0; i < pregunta.getOpciones().size(); i++) {
	        String opcion = pregunta.getOpciones().get(i);
	        Color colorOpcion = COLORES_OPCIONES[i % COLORES_OPCIONES.length];

	        // Crear RadioButton
	        JRadioButton radioButton = new JRadioButton();
	        radioButton.setOpaque(false);
	        radioButton.setActionCommand(opcion);

	        // Panel para cada opción
	        JPanel panelOpcion = new JPanel(new BorderLayout());
	        panelOpcion.setBackground(colorOpcion);
	        panelOpcion.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(colorOpcion.darker(), 2), 
	            new EmptyBorder(15, 15, 15, 15)));
	        panelOpcion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        panelOpcion.setPreferredSize(new Dimension(500, 50));

	        // Label para el texto
	        JLabel lblOpcion = new JLabel(opcion);
	        lblOpcion.setFont(new Font("Segoe UI", Font.BOLD, 16));
	        lblOpcion.setForeground(Color.WHITE);

	        // Agregar componentes al panel de opción
	        panelOpcion.add(radioButton, BorderLayout.WEST);
	        panelOpcion.add(lblOpcion, BorderLayout.CENTER);

	        // Event listeners
	        panelOpcion.addMouseListener(new java.awt.event.MouseAdapter() {
	            @Override
	            public void mouseClicked(java.awt.event.MouseEvent evt) {
	                radioButton.setSelected(true);
	                System.out.println("Seleccionado: " + opcion);
	            }

	            @Override
	            public void mouseEntered(java.awt.event.MouseEvent evt) {
	                panelOpcion.setBackground(colorOpcion.brighter());
	            }

	            @Override
	            public void mouseExited(java.awt.event.MouseEvent evt) {
	                panelOpcion.setBackground(colorOpcion);
	            }
	        });

	        // Agregar al grupo y al panel
	        grupo.add(radioButton);
	        panelOpciones.add(panelOpcion);
	    }

	    // Botón responder
	    JButton btnResponder = crearBotonResponder("Responder");
	    btnResponder.addActionListener(e -> {
	        ButtonModel seleccionado = grupo.getSelection();
	        if (seleccionado != null) {
	            String respuestaSeleccionada = seleccionado.getActionCommand();
	            System.out.println("Respuesta enviada: " + respuestaSeleccionada);
	            procesarRespuesta(respuestaSeleccionada);
	        } else {
	            mostrarAlerta("Por favor, selecciona una opción.", "Opción requerida");
	        }
	    });

	    // Panel para el botón
	    JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    panelBoton.setOpaque(false);
	    panelBoton.add(btnResponder);

	    // Ensamblar panel final
	    panel.add(panelOpciones, BorderLayout.CENTER);
	    panel.add(panelBoton, BorderLayout.SOUTH);

	    System.out.println("Panel TEST creado exitosamente con " + pregunta.getOpciones().size() + " opciones");
	    System.out.println("========================");

	    return panel;
	}
	
	/**
	 * Crea un panel de error cuando no hay opciones disponibles.
	 * 
	 * @return Panel con mensaje de error y botón para saltar pregunta
	 */
	private JPanel crearPanelError() {
		    JPanel panelError = new JPanel(new BorderLayout());
		    panelError.setOpaque(false);
		    
		    // Mensaje de error
		    JLabel lblError = new JLabel("<html><div style='text-align: center; color: red;'>" +
		                               "Error: Esta pregunta no tiene opciones disponibles.<br>" +
		                               "Por favor, verifica la base de datos.</div></html>");
		    lblError.setFont(new Font("Segoe UI", Font.BOLD, 16));
		    lblError.setHorizontalAlignment(SwingConstants.CENTER);
		    
		    // Botón para saltar la pregunta
		    JButton btnSaltar = new JButton("Saltar pregunta");
		    btnSaltar.addActionListener(e -> {
		        preguntaActualEnBloque++;
		        mostrarSiguientePregunta();
		    });
		    
		    panelError.add(lblError, BorderLayout.CENTER);
		    panelError.add(btnSaltar, BorderLayout.SOUTH);
		    
		    return panelError;
		}

	/**
	 * Crea el panel para preguntas de completar huecos.
	 * 
	 * @param pregunta La pregunta de huecos
	 * @return Panel configurado para entrada de texto
	 */
	private JPanel crearPanelPreguntaHuecos(PreguntaHuecos pregunta) {
		JPanel panel = new JPanel(new BorderLayout(15, 15));
		panel.setOpaque(false);

		// Campo de texto estilizado
		txtRespuesta = new JTextField();
		txtRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtRespuesta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
				new EmptyBorder(12, 15, 12, 15)));
		txtRespuesta.setBackground(Color.WHITE);

		JLabel lblHint = new JLabel("Completa el hueco con la palabra correcta");
		lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		lblHint.setForeground(Color.GRAY);
		lblHint.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnResponder = crearBotonResponder("Completar");
		btnResponder.addActionListener(e -> procesarRespuestaTexto());

		txtRespuesta.addActionListener(e -> btnResponder.doClick());

		JPanel panelInput = new JPanel(new BorderLayout(10, 10));
		panelInput.setOpaque(false);
		panelInput.add(lblHint, BorderLayout.NORTH);
		panelInput.add(txtRespuesta, BorderLayout.CENTER);

		panel.add(panelInput, BorderLayout.CENTER);
		panel.add(btnResponder, BorderLayout.SOUTH);

		SwingUtilities.invokeLater(() -> txtRespuesta.requestFocus());

		return panel;
	}
	
	/**
	 * Crea el panel para preguntas de traducción.
	 * 
	 * @param pregunta La pregunta de traducción
	 * @return Panel configurado para entrada de texto
	 */
	private JPanel crearPanelPreguntaTraduccion(PreguntaTraduccion pregunta) {
		JPanel panel = new JPanel(new BorderLayout(15, 15));
		panel.setOpaque(false);

		txtRespuesta = new JTextField();
		txtRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtRespuesta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
				new EmptyBorder(12, 15, 12, 15)));
		txtRespuesta.setBackground(Color.WHITE);

		JLabel lblHint = new JLabel("Escribe la traducción correcta");
		lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		lblHint.setForeground(Color.GRAY);
		lblHint.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnResponder = crearBotonResponder("Traducir");
		btnResponder.addActionListener(e -> procesarRespuestaTexto());

		txtRespuesta.addActionListener(e -> btnResponder.doClick());

		JPanel panelInput = new JPanel(new BorderLayout(10, 10));
		panelInput.setOpaque(false);
		panelInput.add(lblHint, BorderLayout.NORTH);
		panelInput.add(txtRespuesta, BorderLayout.CENTER);

		panel.add(panelInput, BorderLayout.CENTER);
		panel.add(btnResponder, BorderLayout.SOUTH);

		SwingUtilities.invokeLater(() -> txtRespuesta.requestFocus());

		return panel;
	}

	/**
	 * Crea un botón estilizado para responder.
	 * 
	 * @param texto Texto del botón
	 * @return JButton configurado
	 */
	private JButton crearBotonResponder(String texto) {
		JButton btn = new JButton(texto);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btn.setForeground(Color.WHITE);
		btn.setBackground(COLOR_PRIMARIO);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setPreferredSize(new Dimension(200, 45));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btn.setBackground(COLOR_SECUNDARIO);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				btn.setBackground(COLOR_PRIMARIO);
			}
		});

		return btn;
	}

	/**
	 * Procesa la respuesta ingresada en el campo de texto.
	 * Valida que no esté vacía antes de enviarla.
	 */
	private void procesarRespuestaTexto() {
		String respuesta = txtRespuesta.getText().trim();
		if (!respuesta.isEmpty()) {
			procesarRespuesta(respuesta);
		} else {
			mostrarAlerta("Por favor, introduce una respuesta.", "Campo vacío");
		}
	}

	/**
	 * Procesa la respuesta del usuario y muestra el feedback correspondiente.
	 * 
	 * @param respuesta Respuesta proporcionada por el usuario
	 */
	private void procesarRespuesta(String respuesta) {
		boolean esCorrecta = ControladorTemulingo.getInstance().resolverPregunta(preguntaActual, respuesta);

		if (esCorrecta) {
			lblFeedback.setForeground(COLOR_EXITO);
			lblFeedback.setText("¡Correcto! Muy bien.");
		} else {
			lblFeedback.setForeground(COLOR_ERROR);
			String respuestaCorrecta = obtenerRespuestaCorrecta(preguntaActual);
			lblFeedback.setText("Incorrecto. La respuesta correcta era: " + respuestaCorrecta);
		}

		deshabilitarControles();

		Timer timer = new Timer(2500, e -> {
			habilitarControles();
			preguntaActualEnBloque++;
			mostrarSiguientePregunta();
		});
		timer.setRepeats(false);
		timer.start();
	}

	/**
	 * Muestra la pantalla de finalización del cuestionario.
	 */
	private void mostrarFinalizacion() {
		getContentPane().removeAll();
		add(panelFinalizacion, BorderLayout.CENTER);
		revalidate();
		repaint();
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

	private void mostrarAlerta(String mensaje, String titulo) {
		JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
	}

	private void deshabilitarControles() {
		Component[] components = panelPregunta.getComponents();
		for (Component comp : components) {
			deshabilitarComponenteRecursivo(comp);
		}
	}

	private void habilitarControles() {
		Component[] components = panelPregunta.getComponents();
		for (Component comp : components) {
			habilitarComponenteRecursivo(comp);
		}
	}

	private void deshabilitarComponenteRecursivo(Component comp) {
		comp.setEnabled(false);
		if (comp instanceof Container) {
			for (Component child : ((Container) comp).getComponents()) {
				deshabilitarComponenteRecursivo(child);
			}
		}
	}

	private void habilitarComponenteRecursivo(Component comp) {
		comp.setEnabled(true);
		if (comp instanceof Container) {
			for (Component child : ((Container) comp).getComponents()) {
				habilitarComponenteRecursivo(child);
			}
		}
	}
}