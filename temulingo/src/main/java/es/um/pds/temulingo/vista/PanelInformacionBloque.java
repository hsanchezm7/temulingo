package es.um.pds.temulingo.vista;

import es.um.pds.temulingo.logic.Bloque;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelInformacionBloque extends JPanel {

    private static final Color COLOR_PRIMARIO = new Color(74, 144, 226); // azul
    private static final Color COLOR_SECUNDARIO = new Color(52, 120, 199); // No se usa aquí, pero se mantiene la referencia a los colores generales.
    private static final Color COLOR_EXITO = new Color(46, 174, 52);// verde
    private static final Color COLOR_CARD = Color.WHITE;

    private JLabel etiquetaTitulo;
    private JLabel etiquetaDescripcion;
    private JLabel etiquetaProgreso;
    private JButton botonContinuar;

    public PanelInformacionBloque() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel tarjeta = new JPanel(new BorderLayout(20, 20));
        tarjeta.setBackground(COLOR_CARD);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(30, 30, 30, 30)));

        etiquetaTitulo = new JLabel();
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        etiquetaTitulo.setForeground(COLOR_PRIMARIO);
        etiquetaTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        etiquetaDescripcion = new JLabel();
        etiquetaDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        etiquetaDescripcion.setForeground(Color.GRAY);
        etiquetaDescripcion.setHorizontalAlignment(SwingConstants.CENTER);

        etiquetaProgreso = new JLabel();
        etiquetaProgreso.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        etiquetaProgreso.setForeground(Color.GRAY);
        etiquetaProgreso.setHorizontalAlignment(SwingConstants.CENTER);

        botonContinuar = crearBotonContinuar("Comenzar Bloque");

        JPanel panelTexto = new JPanel(new BorderLayout(10, 10));
        panelTexto.setOpaque(false);
        panelTexto.add(etiquetaTitulo, BorderLayout.NORTH);
        panelTexto.add(etiquetaDescripcion, BorderLayout.CENTER);
        panelTexto.add(etiquetaProgreso, BorderLayout.SOUTH);

        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.setOpaque(false);
        panelBoton.add(botonContinuar);

        tarjeta.add(panelTexto, BorderLayout.CENTER);
        tarjeta.add(panelBoton, BorderLayout.SOUTH);

        add(tarjeta, BorderLayout.CENTER);
    }

    public void establecerInformacionBloque(Bloque bloque, String progresoResumen) {
        etiquetaTitulo.setText(bloque.getNombre());
        etiquetaDescripcion.setText("<html><div style='text-align: center; width: 500px;'>"
                + bloque.getDescripcion() + "</div></html>");
        etiquetaProgreso.setText("Progreso del curso: " + progresoResumen);
        
        etiquetaTitulo.revalidate();
        etiquetaDescripcion.revalidate();
        etiquetaProgreso.revalidate();
        revalidate();
        repaint();
    }

    public void agregarListenerContinuar(ActionListener listener) {
        botonContinuar.addActionListener(listener);
    }

    public void removerListenerContinuar(ActionListener listener) {
        botonContinuar.removeActionListener(listener);
    }

    private JButton crearBotonContinuar(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_EXITO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(COLOR_EXITO.brighter());
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(COLOR_EXITO);
            }
        });
        return btn;
    }
}