package es.um.pds.temulingo.vista;

import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.logic.Estadistica;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;


public class DialogoEstadisticas extends JDialog {

    private static final long serialVersionUID = 1L;

    // Constantes de ventana
    private static final String FUNCION = "Estadísticas";
    private static final String NOMBRE_VENTANA = ConfiguracionTemulingo.NOMBRE + " - " + FUNCION;
    private static final int ANCHO_DIALOGO = 700;
    private static final int ALTO_DIALOGO = 500;

    private static final Color COLOR_VERDE_ACIERTOS = new Color(34, 139, 34);
    private static final Color COLOR_ROJO_ERRORES = new Color(220, 20, 60);
    private static final Color COLOR_NARANJA_TASA = new Color(255, 140, 0);
    private static final Color COLOR_AZUL_CURSOS = new Color(70, 130, 180);
    private static final Color COLOR_VIOLETA_TIEMPO = new Color(138, 43, 226);
    private static final Color COLOR_GRIS_SIN_DATOS = new Color(200, 200, 200);

    private static final int ANCHO_PANEL_ESTADISTICAS = 300;
    private static final int ESPACIADO_VERTICAL = 8;
    private static final int ESPACIADO_GRANDE = 15;
    private static final int ESPACIADO_MEDIO = 10;
    private static final Dimension TAMANO_GRAFICO = new Dimension(250, 250);
    private static final Dimension TAMANO_BOTON = new Dimension(100, 30);

    private static final String TITULO_PROGRESO = "Estadísticas de Progreso";
    private static final String TITULO_APRENDIZAJE = "  Progreso de Aprendizaje  ";
    private static final String TITULO_USO = "  Estadísticas de Uso  ";
    private static final String TITULO_GRAFICO = "  Distribución de Respuestas  ";

    private static final DecimalFormat FORMATO_DECIMAL = new DecimalFormat("#.##");
    private static final DecimalFormat FORMATO_ENTERO = new DecimalFormat("0");
    private static final DecimalFormat FORMATO_PORCENTAJE = new DecimalFormat("0.0%");

    private final Frame owner;
    private final Estadistica estadisticas;

    private JLabel lblPreguntasRespondidas;
    private JLabel lblPreguntasAcertadas;
    private JLabel lblTasaAcierto;
    private JLabel lblCursosCompletados;
    private JLabel lblRachaDias;
    private JLabel lblTiempoUso;
    private ChartPanel chartPanel;

    public DialogoEstadisticas(Frame parent, Estadistica estadisticas) {
        super(parent, NOMBRE_VENTANA, true);
        this.owner = parent;
        this.estadisticas = estadisticas;
        inicializarComponentes();
        actualizarEstadisticas();
    }

    private void inicializarComponentes() {
        configurarVentana();
        construirInterfaz();
    }

    private void configurarVentana() {
        setSize(ANCHO_DIALOGO, ALTO_DIALOGO);
        setResizable(true);
        setLocationRelativeTo(owner);
        getContentPane().setLayout(new BorderLayout());
    }

    private void construirInterfaz() {
        // Título principal
        JLabel lblTitulo = crearTituloPrincipal();
        getContentPane().add(lblTitulo, BorderLayout.NORTH);

        // Panel principal con estadísticas y gráfico
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(crearPanelEstadisticas(), BorderLayout.WEST);
        panelPrincipal.add(crearPanelChart(), BorderLayout.CENTER);
        getContentPane().add(panelPrincipal, BorderLayout.CENTER);

        // Panel de botones
        getContentPane().add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JLabel crearTituloPrincipal() {
        JLabel lblTitulo = new JLabel(TITULO_PROGRESO, SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        lblTitulo.setBorder(new EmptyBorder(ESPACIADO_GRANDE, ESPACIADO_GRANDE, ESPACIADO_MEDIO, ESPACIADO_GRANDE));
        return lblTitulo;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(ESPACIADO_GRANDE, ESPACIADO_GRANDE, ESPACIADO_GRANDE, ESPACIADO_MEDIO));
        panel.setPreferredSize(new Dimension(ANCHO_PANEL_ESTADISTICAS, 0));

        panel.add(crearPanelAprendizaje());
        panel.add(Box.createVerticalStrut(ESPACIADO_GRANDE));
        panel.add(crearPanelUso());

        return panel;
    }

    private JPanel crearPanelAprendizaje() {
        JPanel panel = crearPanelConTitulo(TITULO_APRENDIZAJE);

        // Crear etiquetas con sus colores específicos
        lblPreguntasRespondidas = new JLabel("0", SwingConstants.RIGHT);
        lblPreguntasAcertadas = crearEtiquetaConColor("0", COLOR_VERDE_ACIERTOS);
        lblTasaAcierto = crearEtiquetaConColor("0%", COLOR_NARANJA_TASA);
        lblCursosCompletados = crearEtiquetaConColor("0", COLOR_AZUL_CURSOS);

        // Añadir filas de estadísticas
        anadirFilaEstadistica(panel, "Preguntas respondidas:", lblPreguntasRespondidas);
        anadirFilaEstadistica(panel, "Preguntas acertadas:", lblPreguntasAcertadas);
        anadirFilaEstadistica(panel, "Tasa de acierto:", lblTasaAcierto);
        anadirFilaEstadistica(panel, "Cursos completados:", lblCursosCompletados);

        return panel;
    }

    private JPanel crearPanelUso() {
        JPanel panel = crearPanelConTitulo(TITULO_USO);

        lblRachaDias = crearEtiquetaConColor("0 días", COLOR_ROJO_ERRORES);
        lblTiempoUso = crearEtiquetaConColor("0h 0m", COLOR_VIOLETA_TIEMPO);

        anadirFilaEstadistica(panel, "Racha de días:", lblRachaDias);
        anadirFilaEstadistica(panel, "Tiempo total de uso:", lblTiempoUso);

        return panel;
    }

    private JPanel crearPanelConTitulo(String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(titulo),
                new EmptyBorder(ESPACIADO_MEDIO, ESPACIADO_MEDIO, ESPACIADO_MEDIO, ESPACIADO_MEDIO)
        ));
        return panel;
    }

    private JLabel crearEtiquetaConColor(String texto, Color color) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.RIGHT);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD));
        etiqueta.setForeground(color);
        return etiqueta;
    }

    private void anadirFilaEstadistica(JPanel panel, String etiqueta, JLabel valor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.add(new JLabel(etiqueta), BorderLayout.WEST);
        fila.add(valor, BorderLayout.EAST);
        panel.add(fila);
        panel.add(Box.createVerticalStrut(ESPACIADO_VERTICAL));
    }

    private JPanel crearPanelChart() {
        JPanel panelChart = new JPanel(new BorderLayout());
        panelChart.setBorder(new EmptyBorder(ESPACIADO_GRANDE, ESPACIADO_MEDIO, ESPACIADO_GRANDE, ESPACIADO_GRANDE));

        JPanel panelConChart = new JPanel(new BorderLayout());
        panelConChart.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(TITULO_GRAFICO),
                new EmptyBorder(ESPACIADO_GRANDE, ESPACIADO_GRANDE, ESPACIADO_GRANDE, ESPACIADO_GRANDE)
        ));

        chartPanel = crearGraficoCircular();
        panelConChart.add(chartPanel, BorderLayout.CENTER);
        panelChart.add(panelConChart, BorderLayout.CENTER);

        return panelChart;
    }

    private ChartPanel crearGraficoCircular() {
        DefaultPieDataset<String> dataset = crearDatasetGrafico();

        @SuppressWarnings("deprecation")
        JFreeChart chart = ChartFactory.createPieChart3D(null, dataset, true, true, false);
        personalizarGrafico(chart);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(TAMANO_GRAFICO);
        panel.setMouseWheelEnabled(true);
        panel.setOpaque(false);
        panel.setBackground(null);

        return panel;
    }

    private DefaultPieDataset<String> crearDatasetGrafico() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        if (estadisticas != null && estadisticas.getPreguntasRespondidas() > 0) {
            int acertadas = estadisticas.getPreguntasAcertadas();
            int falladas = estadisticas.getPreguntasRespondidas() - acertadas;
            dataset.setValue("Acertadas (" + acertadas + ")", acertadas);
            dataset.setValue("Falladas (" + falladas + ")", falladas);
        } else {
            dataset.setValue("Sin datos", 1);
        }

        return dataset;
    }

    private void personalizarGrafico(JFreeChart chart) {
        @SuppressWarnings("deprecation")
        org.jfree.chart.plot.PiePlot3D plot = (org.jfree.chart.plot.PiePlot3D) chart.getPlot();

        // Configurar colores
        plot.setSectionPaint("Acertadas", COLOR_VERDE_ACIERTOS);
        plot.setSectionPaint("Falladas", COLOR_ROJO_ERRORES);
        plot.setSectionPaint("Sin datos", COLOR_GRIS_SIN_DATOS);

        // Configurar apariencia
        plot.setBackgroundPaint(null);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setDepthFactor(0.15);
        plot.setDarkerSides(true);
        plot.setStartAngle(90);

        // Configurar etiquetas
        plot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator(
                "{0}: {2}", FORMATO_ENTERO, FORMATO_PORCENTAJE));

        // Configurar chart y leyenda
        chart.setBackgroundPaint(null);
        chart.setBorderVisible(false);
        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(null);
            chart.getLegend().setBorder(0, 0, 0, 0);
        }
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(0, ESPACIADO_GRANDE, ESPACIADO_GRANDE, ESPACIADO_GRANDE));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setPreferredSize(TAMANO_BOTON);
        btnCerrar.addActionListener(e -> dispose());
        panel.add(btnCerrar);
        getRootPane().setDefaultButton(btnCerrar);

        return panel;
    }

    private void actualizarEstadisticas() {
        if (estadisticas == null) return;

        lblPreguntasRespondidas.setText(String.valueOf(estadisticas.getPreguntasRespondidas()));
        lblPreguntasAcertadas.setText(String.valueOf(estadisticas.getPreguntasAcertadas()));
        lblCursosCompletados.setText(String.valueOf(estadisticas.getCursosCompletados()));

        double tasaAcierto = estadisticas.getTasaAcierto() * 100;
        lblTasaAcierto.setText(FORMATO_DECIMAL.format(tasaAcierto) + "%");

        // Valores por defecto para campos no implementados
        lblRachaDias.setText("0 días");
        lblTiempoUso.setText("0h 0m");

        actualizarGrafico();
    }

    private void actualizarGrafico() {
        if (chartPanel == null) return;

        ChartPanel nuevoChart = crearGraficoCircular();
        Container parent = chartPanel.getParent();

        if (parent != null) {
            parent.remove(chartPanel);
            parent.add(nuevoChart, BorderLayout.CENTER);
            chartPanel = nuevoChart;
            parent.revalidate();
            parent.repaint();
        }
    }
}