package es.um.pds.temulingo.vista; // Cambiado el paquete para estar al mismo nivel que VentanaRealizarCurso, por simplicidad

import es.um.pds.temulingo.logic.Pregunta;
import es.um.pds.temulingo.logic.PreguntaTest;
import es.um.pds.temulingo.logic.PreguntaHuecos;
import es.um.pds.temulingo.logic.PreguntaTraduccion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class PanelPreguntaDinamico extends JPanel {

    // Constantes de diseño (duplicadas para mantener la lógica de UI independiente, aunque podríamos centralizarlas más)
    private static final Color COLOR_PRIMARIO = new Color(74, 144, 226); //azul
    private static final Color COLOR_SECUNDARIO = new Color(52, 120, 199);
    private static final Color COLOR_EXITO = new Color(46, 174, 52);// verde
    private static final Color COLOR_ERROR = new Color(220, 53, 69);// rojo
    private static final Color[] COLORES_OPCIONES = { new Color(255, 107, 107), new Color(54, 162, 235),
            new Color(255, 206, 84), new Color(75, 192, 192) };

    private Pregunta preguntaActual;
    private JPanel panelContenidoEspecifico; // Aquí irá el contenido para cada tipo de pregunta
    private JLabel etiquetaFeedback;
    private JButton botonAccion; // Botón genérico de acción (Responder, Traducir, Completar)

    // Componentes específicos para cada tipo de pregunta (para poder acceder a ellos)
    private ButtonGroup grupoOpcionesTest; // Para PreguntaTest
    private JTextField campoRespuestaTexto; // Para PreguntaHuecos y PreguntaTraduccion
    
    private ActionListener listenerEnvioGeneral; // Para notificar a la VentanaRealizarCurso

    public PanelPreguntaDinamico() {
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        panelContenidoEspecifico = new JPanel(); // Se inicializa vacío, el layout se establece dinámicamente
        panelContenidoEspecifico.setOpaque(false);
        add(panelContenidoEspecifico, BorderLayout.CENTER);

        etiquetaFeedback = new JLabel("");
        etiquetaFeedback.setFont(new Font("Segoe UI", Font.BOLD, 16));
        etiquetaFeedback.setHorizontalAlignment(SwingConstants.CENTER);
        add(etiquetaFeedback, BorderLayout.SOUTH);
    }

    /**
     * Establece la pregunta actual y renderiza el panel de acuerdo a su tipo.
     * @param pregunta La pregunta a mostrar.
     */
    public void establecerPregunta(Pregunta pregunta) {
        this.preguntaActual = pregunta;
        panelContenidoEspecifico.removeAll(); // Limpiar el contenido anterior
        etiquetaFeedback.setText(""); // Limpiar feedback

        if (pregunta instanceof PreguntaTest) {
            renderizarPreguntaTest((PreguntaTest) pregunta);
        } else if (pregunta instanceof PreguntaHuecos) {
            renderizarPreguntaHuecos((PreguntaHuecos) pregunta);
        } else if (pregunta instanceof PreguntaTraduccion) {
            renderizarPreguntaTraduccion((PreguntaTraduccion) pregunta);
        } else {
            // Manejo de tipo de pregunta no soportado
            panelContenidoEspecifico.setLayout(new BorderLayout());
            JLabel errorLabel = new JLabel("<html><div style='text-align: center; color: red;'>"
                    + "Tipo de pregunta no soportado.</div></html>");
            errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelContenidoEspecifico.add(errorLabel, BorderLayout.CENTER);
            botonAccion = crearBotonEstilizado("Saltar Pregunta", COLOR_ERROR);
            JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelBoton.setOpaque(false);
            panelBoton.add(botonAccion);
            panelContenidoEspecifico.add(panelBoton, BorderLayout.SOUTH);
            // Si el botón ya tiene un listener, lo removemos y agregamos el nuevo
            if (listenerEnvioGeneral != null) {
                botonAccion.removeActionListener(listenerEnvioGeneral);
            }
            botonAccion.addActionListener(e -> {
                 if (listenerEnvioGeneral != null) {
                     listenerEnvioGeneral.actionPerformed(new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "SALTAR_ERROR"));
                 }
            });
        }
        
        revalidate();
        repaint();
        
        // Enfocar el campo de texto si es una pregunta de texto
        if (campoRespuestaTexto != null) {
            SwingUtilities.invokeLater(() -> campoRespuestaTexto.requestFocusInWindow());
        }
    }

    /**
     * Renderiza la UI para una pregunta de tipo Test.
     */
    private void renderizarPreguntaTest(PreguntaTest pregunta) {
        panelContenidoEspecifico.setLayout(new BorderLayout(10, 10)); // Layout para este tipo de pregunta

        grupoOpcionesTest = new ButtonGroup();
        JPanel panelOpciones = new JPanel(new GridLayout(pregunta.getOpciones().size(), 1, 0, 15));
        panelOpciones.setOpaque(false);

        for (int i = 0; i < pregunta.getOpciones().size(); i++) {
            String opcion = pregunta.getOpciones().get(i);
            Color colorOpcion = COLORES_OPCIONES[i % COLORES_OPCIONES.length];

            JRadioButton radioButton = new JRadioButton();
            radioButton.setOpaque(false);
            radioButton.setActionCommand(opcion);
            grupoOpcionesTest.add(radioButton);

            JPanel panelOpcion = new JPanel(new BorderLayout());
            panelOpcion.setBackground(colorOpcion);
            panelOpcion.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorOpcion.darker(), 2), new EmptyBorder(15, 15, 15, 15)));
            panelOpcion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panelOpcion.setPreferredSize(new Dimension(500, 50));

            JLabel etiquetaOpcion = new JLabel(opcion);
            etiquetaOpcion.setFont(new Font("Segoe UI", Font.BOLD, 16));
            etiquetaOpcion.setForeground(Color.WHITE);

            panelOpcion.add(radioButton, BorderLayout.WEST);
            panelOpcion.add(etiquetaOpcion, BorderLayout.CENTER);

            panelOpcion.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    radioButton.setSelected(true);
                }
                @Override
                public void mouseEntered(MouseEvent evt) {
                    panelOpcion.setBackground(colorOpcion.brighter());
                }
                @Override
                public void mouseExited(MouseEvent evt) {
                    panelOpcion.setBackground(colorOpcion);
                }
            });
            panelOpciones.add(panelOpcion);
        }
        
        botonAccion = crearBotonEstilizado("Responder", COLOR_PRIMARIO);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setOpaque(false);
        panelBoton.add(botonAccion);

        panelContenidoEspecifico.add(panelOpciones, BorderLayout.CENTER);
        panelContenidoEspecifico.add(panelBoton, BorderLayout.SOUTH);
        
        // Re-añadir listener si existe, para asegurar que el botón correcto lo tiene
        if(listenerEnvioGeneral != null) {
            botonAccion.addActionListener(listenerEnvioGeneral);
        }
    }

    /**
     * Renderiza la UI para una pregunta de tipo Huecos.
     */
    private void renderizarPreguntaHuecos(PreguntaHuecos pregunta) {
        panelContenidoEspecifico.setLayout(new BorderLayout(10, 10));

        campoRespuestaTexto = new JTextField();
        campoRespuestaTexto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        campoRespuestaTexto.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                new EmptyBorder(12, 15, 12, 15)));
        campoRespuestaTexto.setBackground(Color.WHITE);

        JLabel etiquetaPista = new JLabel("Completa el hueco con la palabra correcta");
        etiquetaPista.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        etiquetaPista.setForeground(Color.GRAY);
        etiquetaPista.setHorizontalAlignment(SwingConstants.CENTER);

        botonAccion = crearBotonEstilizado("Completar", COLOR_PRIMARIO);
        
        campoRespuestaTexto.addActionListener(e -> {
            if (listenerEnvioGeneral != null) {
                listenerEnvioGeneral.actionPerformed(e);
            }
        });

        JPanel panelEntrada = new JPanel(new BorderLayout(10, 10));
        panelEntrada.setOpaque(false);
        panelEntrada.add(etiquetaPista, BorderLayout.NORTH);
        panelEntrada.add(campoRespuestaTexto, BorderLayout.CENTER);

        panelContenidoEspecifico.add(panelEntrada, BorderLayout.CENTER);
        panelContenidoEspecifico.add(botonAccion, BorderLayout.SOUTH);
        
        // Re-añadir listener si existe
        if(listenerEnvioGeneral != null) {
            botonAccion.addActionListener(listenerEnvioGeneral);
        }
    }

    /**
     * Renderiza la UI para una pregunta de tipo Traduccion.
     */
    private void renderizarPreguntaTraduccion(PreguntaTraduccion pregunta) {
        panelContenidoEspecifico.setLayout(new BorderLayout(10, 10));

        campoRespuestaTexto = new JTextField();
        campoRespuestaTexto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        campoRespuestaTexto.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                new EmptyBorder(12, 15, 12, 15)));
        campoRespuestaTexto.setBackground(Color.WHITE);

        JLabel etiquetaPista = new JLabel("Escribe la traducción correcta");
        etiquetaPista.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        etiquetaPista.setForeground(Color.GRAY);
        etiquetaPista.setHorizontalAlignment(SwingConstants.CENTER);

        botonAccion = crearBotonEstilizado("Traducir", COLOR_PRIMARIO);
        
        campoRespuestaTexto.addActionListener(e -> {
            if (listenerEnvioGeneral != null) {
                listenerEnvioGeneral.actionPerformed(e);
            }
        });

        JPanel panelEntrada = new JPanel(new BorderLayout(10, 10));
        panelEntrada.setOpaque(false);
        panelEntrada.add(etiquetaPista, BorderLayout.NORTH);
        panelEntrada.add(campoRespuestaTexto, BorderLayout.CENTER);

        panelContenidoEspecifico.add(panelEntrada, BorderLayout.CENTER);
        panelContenidoEspecifico.add(botonAccion, BorderLayout.SOUTH);
        
        // Re-añadir listener si existe
        if(listenerEnvioGeneral != null) {
            botonAccion.addActionListener(listenerEnvioGeneral);
        }
    }

    /**
     * Obtiene la respuesta del usuario según el tipo de pregunta actual.
     * @return La respuesta del usuario.
     */
    public String obtenerRespuesta() {
        if (preguntaActual instanceof PreguntaTest) {
            ButtonModel seleccionado = grupoOpcionesTest.getSelection();
            return (seleccionado != null) ? seleccionado.getActionCommand() : null;
        } else if (preguntaActual instanceof PreguntaHuecos || preguntaActual instanceof PreguntaTraduccion) {
            return campoRespuestaTexto.getText().trim();
        }
        return null; // O lanzar una excepción si el tipo no es manejado
    }

    /**
     * Muestra feedback al usuario.
     * @param feedback El mensaje a mostrar.
     * @param esCorrecta Indica si la respuesta fue correcta.
     */
    public void mostrarFeedback(String feedback, boolean esCorrecta) {
        etiquetaFeedback.setForeground(esCorrecta ? COLOR_EXITO : COLOR_ERROR);
        etiquetaFeedback.setText(feedback);
    }

    /**
     * Deshabilita los controles de entrada del usuario.
     */
    public void deshabilitarEntrada() {
        if (preguntaActual instanceof PreguntaTest) {
            Enumeration<AbstractButton> botones = grupoOpcionesTest.getElements();
            while (botones.hasMoreElements()) {
                botones.nextElement().setEnabled(false);
            }
        } else if (preguntaActual instanceof PreguntaHuecos || preguntaActual instanceof PreguntaTraduccion) {
            campoRespuestaTexto.setEnabled(false);
        }
        if (botonAccion != null) {
            botonAccion.setEnabled(false);
        }
    }

    /**
     * Habilita los controles de entrada del usuario.
     */
    public void habilitarEntrada() {
        if (preguntaActual instanceof PreguntaTest) {
            Enumeration<AbstractButton> botones = grupoOpcionesTest.getElements();
            while (botones.hasMoreElements()) {
                botones.nextElement().setEnabled(true);
            }
        } else if (preguntaActual instanceof PreguntaHuecos || preguntaActual instanceof PreguntaTraduccion) {
            campoRespuestaTexto.setEnabled(true);
        }
        if (botonAccion != null) {
            botonAccion.setEnabled(true);
        }
        etiquetaFeedback.setText(""); // Limpiar feedback
    }

    /**
     * Agrega un listener para el botón de acción principal.
     * @param listener El ActionListener a agregar.
     */
    public void agregarListenerAccion(ActionListener listener) {
        // Primero, remover cualquier listener previo del botón de acción
        if (botonAccion != null && listenerEnvioGeneral != null) {
            botonAccion.removeActionListener(listenerEnvioGeneral);
        }
        this.listenerEnvioGeneral = listener;
        if (botonAccion != null) {
            botonAccion.addActionListener(this.listenerEnvioGeneral);
        }
    }
    
    /**
     * Remueve el listener para el botón de acción principal.
     * @param listener El ActionListener a remover.
     */
    public void removerListenerAccion(ActionListener listener) {
        if (botonAccion != null) {
            botonAccion.removeActionListener(listener);
        }
        this.listenerEnvioGeneral = null;
    }

    /**
     * Método auxiliar para crear botones estilizados, centralizado aquí.
     */
    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorFondo);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(colorFondo.brighter());
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(colorFondo);
            }
        });
        return btn;
    }
}