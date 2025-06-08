package es.um.pds.temulingo.vista;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.logic.Estadistica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class DialogoEstadisticas extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final String FUNCION = "Estadísticas";
    private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE + " - " + FUNCION;

    private static final int ANCHO_DIALOGO = 450;

    private final Frame owner;
    private final Estadistica estadisticas;

    // Componentes para mostrar las estadísticas
    private JLabel lblPreguntasRespondidas;
    private JLabel lblPreguntasAcertadas;
    private JLabel lblTasaAcierto;
    private JLabel lblCursosCompletados;
    private JLabel lblRachaDias;
    private JLabel lblTiempoUso;

    public DialogoEstadisticas(Frame parent, Estadistica estadisticas) {
        super(parent, NOMBRE_VENTANA, true);

        this.owner = parent;
        this.estadisticas = estadisticas;

        inicializarComponentes();
        actualizarEstadisticas();
    }

    private void inicializarComponentes() {
        getContentPane().setLayout(new BorderLayout());

        JPanel panelCentro = crearPanelEstadisticas();
        getContentPane().add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        pack();
        setSize(ANCHO_DIALOGO, getPreferredSize().height);
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Estadísticas de Progreso", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Panel de estadísticas de aprendizaje
        JPanel panelAprendizaje = crearPanelAprendizaje();
        panelContenido.add(panelAprendizaje);

        panelContenido.add(Box.createVerticalStrut(15));

        // Panel de estadísticas de uso
        JPanel panelUso = crearPanelUso();
        panelContenido.add(panelUso);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        return panelPrincipal;
    }

    private JPanel crearPanelAprendizaje() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("  Progreso de Aprendizaje  "),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Preguntas respondidas
        JPanel panelPreguntasResp = new JPanel(new BorderLayout());
        panelPreguntasResp.add(new JLabel("Preguntas respondidas:"), BorderLayout.WEST);
        lblPreguntasRespondidas = new JLabel("0", SwingConstants.RIGHT);
        lblPreguntasRespondidas.setFont(lblPreguntasRespondidas.getFont().deriveFont(Font.BOLD));
        panelPreguntasResp.add(lblPreguntasRespondidas, BorderLayout.EAST);
        panel.add(panelPreguntasResp);
        panel.add(Box.createVerticalStrut(8));

        // Preguntas acertadas
        JPanel panelPreguntasAcert = new JPanel(new BorderLayout());
        panelPreguntasAcert.add(new JLabel("Preguntas acertadas:"), BorderLayout.WEST);
        lblPreguntasAcertadas = new JLabel("0", SwingConstants.RIGHT);
        lblPreguntasAcertadas.setFont(lblPreguntasAcertadas.getFont().deriveFont(Font.BOLD));
        lblPreguntasAcertadas.setForeground(new Color(34, 139, 34));
        panelPreguntasAcert.add(lblPreguntasAcertadas, BorderLayout.EAST);
        panel.add(panelPreguntasAcert);
        panel.add(Box.createVerticalStrut(8));

        // Tasa de acierto
        JPanel panelTasaAcierto = new JPanel(new BorderLayout());
        panelTasaAcierto.add(new JLabel("Tasa de acierto:"), BorderLayout.WEST);
        lblTasaAcierto = new JLabel("0%", SwingConstants.RIGHT);
        lblTasaAcierto.setFont(lblTasaAcierto.getFont().deriveFont(Font.BOLD));
        lblTasaAcierto.setForeground(new Color(255, 140, 0));
        panelTasaAcierto.add(lblTasaAcierto, BorderLayout.EAST);
        panel.add(panelTasaAcierto);
        panel.add(Box.createVerticalStrut(8));

        // Cursos completados
        JPanel panelCursos = new JPanel(new BorderLayout());
        panelCursos.add(new JLabel("Cursos completados:"), BorderLayout.WEST);
        lblCursosCompletados = new JLabel("0", SwingConstants.RIGHT);
        lblCursosCompletados.setFont(lblCursosCompletados.getFont().deriveFont(Font.BOLD));
        lblCursosCompletados.setForeground(new Color(70, 130, 180));
        panelCursos.add(lblCursosCompletados, BorderLayout.EAST);
        panel.add(panelCursos);

        return panel;
    }

    private JPanel crearPanelUso() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("  Estadísticas de Uso  "),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Racha de días
        JPanel panelRacha = new JPanel(new BorderLayout());
        panelRacha.add(new JLabel("Racha de días:"), BorderLayout.WEST);
        lblRachaDias = new JLabel("0 días", SwingConstants.RIGHT);
        lblRachaDias.setFont(lblRachaDias.getFont().deriveFont(Font.BOLD));
        lblRachaDias.setForeground(new Color(220, 20, 60));
        panelRacha.add(lblRachaDias, BorderLayout.EAST);
        panel.add(panelRacha);
        panel.add(Box.createVerticalStrut(8));

        // Tiempo de uso
        JPanel panelTiempo = new JPanel(new BorderLayout());
        panelTiempo.add(new JLabel("Tiempo total de uso:"), BorderLayout.WEST);
        lblTiempoUso = new JLabel("0h 0m", SwingConstants.RIGHT);
        lblTiempoUso.setFont(lblTiempoUso.getFont().deriveFont(Font.BOLD));
        lblTiempoUso.setForeground(new Color(138, 43, 226));
        panelTiempo.add(lblTiempoUso, BorderLayout.EAST);
        panel.add(panelTiempo);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(new EmptyBorder(0, 15, 15, 15));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setPreferredSize(new Dimension(100, 30));
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        getRootPane().setDefaultButton(btnCerrar);

        return panelBotones;
    }

    private void actualizarEstadisticas() {
        if (estadisticas != null) {
            // Actualizar estadísticas existentes
            lblPreguntasRespondidas.setText(String.valueOf(estadisticas.getPreguntasRespondidas()));
            lblPreguntasAcertadas.setText(String.valueOf(estadisticas.getPreguntasAcertadas()));
            lblCursosCompletados.setText(String.valueOf(estadisticas.getCursosCompletados()));

            // Calcular y mostrar tasa de acierto
            double tasaAcierto = estadisticas.getTasaAcierto() * 100;
            DecimalFormat df = new DecimalFormat("#.##");
            lblTasaAcierto.setText(df.format(tasaAcierto) + "%");

            // Valores por defecto para las estadísticas no implementadas aún
            lblRachaDias.setText("0 días");
            lblTiempoUso.setText("0h 0m");
        }
    }

    // Método para actualizar las estadísticas cuando se implementen
    public void actualizarRachaDias(int rachaDias) {
        lblRachaDias.setText(rachaDias + (rachaDias == 1 ? " día" : " días"));
    }

    // Método para actualizar el tiempo de uso cuando se implemente
    public void actualizarTiempoUso(long tiempoMinutos) {
        long horas = tiempoMinutos / 60;
        long minutos = tiempoMinutos % 60;
        lblTiempoUso.setText(horas + "h " + minutos + "m");
    }

    // Método para refrescar todas las estadísticas
    public void refrescarEstadisticas(Estadistica nuevasEstadisticas) {
        // Actualizar la referencia y refrescar la vista
        actualizarEstadisticas();
    }
}