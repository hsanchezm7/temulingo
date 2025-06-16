package es.um.pds.temulingo.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelFinalizacionCurso extends JPanel {

    private static final Color COLOR_PRIMARIO = new Color(74, 144, 226);
    private static final Color COLOR_EXITO = new Color(46, 174, 52);
    private static final Color COLOR_CARD = Color.WHITE;

    private JLabel etiquetaMensajeCurso;
    private JButton botonCerrar;

    public PanelFinalizacionCurso() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel tarjeta = new JPanel(new BorderLayout(20, 20));
        tarjeta.setBackground(COLOR_CARD);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(40, 40, 40, 40)));

        JLabel etiquetaTitulo = new JLabel("¡Enhorabuena!");
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        etiquetaTitulo.setForeground(COLOR_EXITO);
        etiquetaTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        etiquetaMensajeCurso = new JLabel();
        etiquetaMensajeCurso.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        etiquetaMensajeCurso.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaMensajeCurso.setForeground(new Color(51, 51, 51));

        botonCerrar = crearBotonCerrar("Finalizar");

        JPanel panelTexto = new JPanel(new BorderLayout(15, 15));
        panelTexto.setOpaque(false);
        panelTexto.add(etiquetaTitulo, BorderLayout.NORTH);
        panelTexto.add(etiquetaMensajeCurso, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.setOpaque(false);
        panelBoton.add(botonCerrar);

        tarjeta.add(panelTexto, BorderLayout.CENTER);
        tarjeta.add(panelBoton, BorderLayout.SOUTH);

        add(tarjeta, BorderLayout.CENTER);
    }

    public void establecerMensajeFinalizacion(String nombreCurso) {
        etiquetaMensajeCurso.setText("<html><div style='text-align: center;'>Has completado <br><strong>"
                + nombreCurso + "</strong></div></html>");
    }

    public void agregarListenerCerrar(ActionListener listener) {
        botonCerrar.addActionListener(listener);
    }

    public void removerListenerCerrar(ActionListener listener) {
        botonCerrar.removeActionListener(listener);
    }

    private JButton crearBotonCerrar(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARIO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(COLOR_PRIMARIO.brighter());
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(COLOR_PRIMARIO);
            }
        });
        return btn;
    }
}