package es.um.pds.temulingo.vista;

import es.um.pds.temulingo.controlador.ControladorTemulingo;
import es.um.pds.temulingo.logic.Curso;
import es.um.pds.temulingo.logic.Curso.EstrategiaAprendizaje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogoSeleccionEstrategia extends JDialog {

    private static final Color COLOR_PRIMARIO = new Color(74, 144, 226); //azul
    private static final Color COLOR_SECUNDARIO = new Color(52, 120, 199);
    private static final Color COLOR_FONDO = new Color(248, 249, 250);
    
    private Curso curso;
    private VentanaRealizarCurso ventanaRealizarCursoParent;
    private boolean estrategiaSeleccionada = false;
    private JFrame originalMainFrame;

    /**
     * Constructor para cuando se abre desde VentanaMain (para iniciar un curso nuevo).
     * @param parentFrame El JFrame padre, que debe ser VentanaMain.
     * @param curso El curso para el que se seleccionará la estrategia.
     */
    public DialogoSeleccionEstrategia(JFrame parentFrame, Curso curso) {
        super(parentFrame, "Seleccionar Estrategia de Aprendizaje", true);
        this.originalMainFrame = parentFrame;
        this.curso = curso;
        init();
    }
    
    /**
     * Constructor para cuando se abre desde VentanaRealizarCurso (para cambiar la estrategia de un curso en curso).
     * @param parentWindow La VentanaRealizarCurso actual que ha invocado este diálogo.
     * @param curso El curso activo en VentanaRealizarCurso.
     */
    public DialogoSeleccionEstrategia(VentanaRealizarCurso parentWindow, Curso curso) {
        super(parentWindow, "Cambiar Estrategia de Aprendizaje", true);
        this.curso = curso;
        this.ventanaRealizarCursoParent = parentWindow;
        
        // El propietario del diálogo (parentWindow) será VentanaRealizarCurso.
        // Si VentanaRealizarCurso tiene a VentanaMain como su padre (owner),
        // se guarda la referencia a VentanaMain. De lo contrario, se usa la misma VentanaRealizarCurso como referencia.
        // Esto es para que el WindowListener pueda habilitar la ventana correcta al cerrar.
        if (parentWindow.getOwner() instanceof VentanaMain) { // Usar getOwner() que devuelve el Frame o Dialog que es propietario
            this.originalMainFrame = (VentanaMain) parentWindow.getOwner();
        } else {
            this.originalMainFrame = parentWindow; // Si no es VentanaMain, se asume que es la propia VentanaRealizarCurso
        }
        init();
    }

    private void init() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 400);
        
        setLocationRelativeTo(getParent()); 
        
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(COLOR_FONDO);

        JLabel lblTitulo = new JLabel("Selecciona tu estrategia de aprendizaje");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 15, 15));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(20, 50, 20, 50));

        for (EstrategiaAprendizaje estrategia : EstrategiaAprendizaje.values()) {
            JButton btnEstrategia = crearBotonEstrategia(estrategia);
            panelBotones.add(btnEstrategia);
        }
        add(panelBotones, BorderLayout.CENTER);

        // Añadir un WindowListener para manejar el cierre del diálogo.
        //Para re-habilitar la ventana padre si el usuario cierra el diálogo sin seleccionar estrategia.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                estrategiaSeleccionada = false; // Si se cierra con la 'X', no se ha seleccionado estrategia
                dispose();
            }
            @Override
            public void windowClosed(WindowEvent e) {
                if (originalMainFrame != null) {
                    originalMainFrame.setEnabled(true);
                    originalMainFrame.toFront();
                    originalMainFrame.repaint();
                } else if (ventanaRealizarCursoParent != null) {
                    ventanaRealizarCursoParent.setEnabled(true);
                    ventanaRealizarCursoParent.toFront();
                    ventanaRealizarCursoParent.repaint();
                }
            }
        });
    }

    private JButton crearBotonEstrategia(EstrategiaAprendizaje estrategia) {
        JButton btn = new JButton(getTextoEstrategia(estrategia));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARIO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(400, 60));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(COLOR_SECUNDARIO);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(COLOR_PRIMARIO);
            }
        });

        btn.addActionListener(e -> {
            seleccionarEstrategia(estrategia);
            dispose(); // Cerrar el diálogo después de la selección
        });
        return btn;
    }

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
     * Maneja la selección de una estrategia de aprendizaje.
     * Establece la estrategia en el controlador y abre/actualiza la ventana del curso.
     * @param estrategia La estrategia seleccionada.
     */
    private void seleccionarEstrategia(EstrategiaAprendizaje estrategia) {
        // Se asume que el `ControladorTemulingo.getInstance().getCursoActual()`
        // ya está establecido antes de que se muestre este diálogo.
        // Si no lo está, se muestra un mensaje de error.
        if (ControladorTemulingo.getInstance().getCursoActual() == null) {
            JOptionPane.showMessageDialog(this, 
                "Error: El curso actual no está cargado. Por favor, inténtalo de nuevo.", 
                "Error de curso", 
                JOptionPane.ERROR_MESSAGE);
            estrategiaSeleccionada = false;
            return;
        }

        ControladorTemulingo.getInstance().setEstrategiaAprendizaje(estrategia);
        estrategiaSeleccionada = true; // La estrategia se establecie con éxito

        // Si se abrió desde VentanaMain (curso nuevo), abrir VentanaRealizarCurso.
        if (ventanaRealizarCursoParent == null) { 
            if (originalMainFrame instanceof VentanaMain) {
                ((VentanaMain) originalMainFrame).abrirVentanaRealizarCurso(curso);
            }
        } 
        // Si se abrió desde VentanaRealizarCurso (cambio de estrategia en curso), solo actualizarla.
        else { 
            ventanaRealizarCursoParent.mostrarSiguientePregunta(); // Recarga la UI con la nueva estrategia  
        }
    }

    /**
     * Indica si se ha seleccionado una estrategia con éxito.
     * @return true si se seleccionó una estrategia y se procesó correctamente, false en caso contrario.
     */
    public boolean estrategiaSeleccionada() {
        return estrategiaSeleccionada;
    }
}