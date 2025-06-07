package es.um.pds.temulingo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import es.um.pds.temulingo.config.ConfiguracionImagenes;
import es.um.pds.temulingo.config.ConfiguracionTemulingo;
import es.um.pds.temulingo.config.ConfiguracionUI;
import es.um.pds.temulingo.config.ConfiguracionUI.CategoriasTema;
import es.um.pds.temulingo.config.ConfiguracionUI.Tema;

public class BarraMenu extends JMenuBar {

    private static final long serialVersionUID = 1L;
    
    private final Frame padre;
    
    public BarraMenu(Frame padre) {
        this.padre = padre;
        
        crearMenus();
    }
    
    private void crearMenus() {
        // Menú Archivo
        JMenu menuArchivo = crearMenuArchivo();
        
        // Menú Editar
        JMenu menuEditar = crearMenuEditar();
        
        // Menú Ver
        JMenu menuVer = crearMenuVer();
        
        // Menú Herramientas
        JMenu menuHerramientas = crearMenuHerramientas();
        
        // Menú Ayuda
        JMenu menuAyuda = crearMenuAyuda();
        
        // Agregar menús a la barra
        this.add(menuArchivo);
        this.add(menuEditar);
        this.add(menuVer);
        this.add(menuHerramientas);
        this.add(menuAyuda);
    }
    
    private JMenu crearMenuArchivo() {
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setMnemonic(KeyEvent.VK_A);
        
        // Nuevo
        JMenuItem itemNuevo = new JMenuItem("Nuevo");
        itemNuevo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        itemNuevo.setMnemonic(KeyEvent.VK_N);
        itemNuevo.addActionListener(e -> accionNuevo());
        
        // Abrir
        JMenuItem itemAbrir = new JMenuItem("Abrir...");
        itemAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemAbrir.setMnemonic(KeyEvent.VK_A);
        itemAbrir.addActionListener(e -> accionAbrir());
        
        // Guardar
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        itemGuardar.setMnemonic(KeyEvent.VK_G);
        itemGuardar.addActionListener(e -> accionGuardar());
        
        // Guardar como
        JMenuItem itemGuardarComo = new JMenuItem("Guardar como...");
        itemGuardarComo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
            ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        itemGuardarComo.addActionListener(e -> accionGuardarComo());
        
        // Separador
        JSeparator separador1 = new JSeparator();
        
        // Exportar
        JMenu submenuExportar = new JMenu("Exportar");
        JMenuItem itemExportarPDF = new JMenuItem("Como PDF...");
        JMenuItem itemExportarHTML = new JMenuItem("Como HTML...");
        itemExportarPDF.addActionListener(e -> accionExportarPDF());
        itemExportarHTML.addActionListener(e -> accionExportarHTML());
        submenuExportar.add(itemExportarPDF);
        submenuExportar.add(itemExportarHTML);
        
        // Separador
        JSeparator separador2 = new JSeparator();
        
        // Salir
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        itemSalir.setMnemonic(KeyEvent.VK_S);
        itemSalir.addActionListener(e -> accionSalir());
        
        // Agregar elementos al menú
        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemAbrir);
        menuArchivo.add(separador1);
        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemGuardarComo);
        menuArchivo.add(separador2);
        menuArchivo.add(submenuExportar);
        menuArchivo.add(new JSeparator());
        menuArchivo.add(itemSalir);
        
        return menuArchivo;
    }
    
    private JMenu crearMenuEditar() {
        JMenu menuEditar = new JMenu("Editar");
        menuEditar.setMnemonic(KeyEvent.VK_E);
        
        // Deshacer
        JMenuItem itemDeshacer = new JMenuItem("Deshacer");
        itemDeshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        itemDeshacer.addActionListener(e -> accionDeshacer());
        
        // Rehacer
        JMenuItem itemRehacer = new JMenuItem("Rehacer");
        itemRehacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        itemRehacer.addActionListener(e -> accionRehacer());
        
        // Separador
        JSeparator separador1 = new JSeparator();
        
        // Cortar
        JMenuItem itemCortar = new JMenuItem("Cortar");
        itemCortar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        itemCortar.addActionListener(e -> accionCortar());
        
        // Copiar
        JMenuItem itemCopiar = new JMenuItem("Copiar");
        itemCopiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        itemCopiar.addActionListener(e -> accionCopiar());
        
        // Pegar
        JMenuItem itemPegar = new JMenuItem("Pegar");
        itemPegar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        itemPegar.addActionListener(e -> accionPegar());
        
        // Separador
        JSeparator separador2 = new JSeparator();
        
        // Seleccionar todo
        JMenuItem itemSeleccionarTodo = new JMenuItem("Seleccionar todo");
        itemSeleccionarTodo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        itemSeleccionarTodo.addActionListener(e -> accionSeleccionarTodo());
        
        // Buscar
        JMenuItem itemBuscar = new JMenuItem("Buscar...");
        itemBuscar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        itemBuscar.addActionListener(e -> accionBuscar());
        
        // Reemplazar
        JMenuItem itemReemplazar = new JMenuItem("Reemplazar...");
        itemReemplazar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        itemReemplazar.addActionListener(e -> accionReemplazar());
        
        // Agregar elementos
        menuEditar.add(itemDeshacer);
        menuEditar.add(itemRehacer);
        menuEditar.add(separador1);
        menuEditar.add(itemCortar);
        menuEditar.add(itemCopiar);
        menuEditar.add(itemPegar);
        menuEditar.add(separador2);
        menuEditar.add(itemSeleccionarTodo);
        menuEditar.add(new JSeparator());
        menuEditar.add(itemBuscar);
        menuEditar.add(itemReemplazar);
        
        return menuEditar;
    }
    
    private JMenu crearMenuVer() {
        JMenu menuVer = new JMenu("Ver");
        menuVer.setMnemonic(KeyEvent.VK_V);
        
        // Zoom
        JMenu submenuZoom = new JMenu("Zoom");
        JMenuItem itemZoomIn = new JMenuItem("Acercar");
        itemZoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
        JMenuItem itemZoomOut = new JMenuItem("Alejar");
        itemZoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        JMenuItem itemZoomReset = new JMenuItem("Tamaño original");
        itemZoomReset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
        
        submenuZoom.add(itemZoomIn);
        submenuZoom.add(itemZoomOut);
        submenuZoom.add(itemZoomReset);
        
        // Pantalla completa
        JCheckBoxMenuItem itemPantallaCompleta = new JCheckBoxMenuItem("Pantalla completa");
        itemPantallaCompleta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        itemPantallaCompleta.addActionListener(e -> accionPantallaCompleta(itemPantallaCompleta.isSelected()));
        
        // Barra de estado
        JCheckBoxMenuItem itemBarraEstado = new JCheckBoxMenuItem("Barra de estado", true);
        itemBarraEstado.addActionListener(e -> accionMostrarBarraEstado(itemBarraEstado.isSelected()));
        
        // Tema - Sistema optimizado y escalable
        JMenu submenuTema = crearSubmenuTemas();
        
        // Agregar elementos
        menuVer.add(submenuZoom);
        menuVer.add(new JSeparator());
        menuVer.add(itemPantallaCompleta);
        menuVer.add(itemBarraEstado);
        menuVer.add(new JSeparator());
        menuVer.add(submenuTema);
        
        return menuVer;
    }
    
    private JMenu crearSubmenuTemas() {
        JMenu submenuTema = new JMenu("Tema");
        ButtonGroup grupoTemas = new ButtonGroup();

        Tema temaActual = ConfiguracionUI.getTemaActual();
        CategoriasTema categoriaAnterior = null;

        for (Tema tema : Tema.values()) {
            if (categoriaAnterior != tema.getCategoria()) {
                if (categoriaAnterior != null) {
                    submenuTema.add(new JSeparator());
                }

                JMenuItem tituloCategoria = new JMenuItem(tema.getCategoria().getTitulo());
                tituloCategoria.setEnabled(false);
                tituloCategoria.setFont(tituloCategoria.getFont().deriveFont(
                    java.awt.Font.ITALIC,
                    tituloCategoria.getFont().getSize() - 1));
                submenuTema.add(tituloCategoria);

                categoriaAnterior = tema.getCategoria();
            }

            JRadioButtonMenuItem itemTema = new JRadioButtonMenuItem("   " + tema.getNombre());
            itemTema.addActionListener(new TemaActionListener(tema));
            grupoTemas.add(itemTema);
            submenuTema.add(itemTema);

            // 👇 Marcar como seleccionado si coincide con el tema actual
            if (tema.equals(temaActual)) {
                itemTema.setSelected(true);
            }
        }

        return submenuTema;
    }

    
    private JMenu crearMenuHerramientas() {
        JMenu menuHerramientas = new JMenu("Herramientas");
        menuHerramientas.setMnemonic(KeyEvent.VK_H);
        
        // Configuración
        JMenuItem itemConfiguracion = new JMenuItem("Configuración...");
        itemConfiguracion.addActionListener(e -> accionConfiguracion());
        
        // Estadísticas
        JMenuItem itemEstadisticas = new JMenuItem("Estadísticas...");
        itemEstadisticas.addActionListener(e -> accionEstadisticas());
        
        // Personalizar
        JMenuItem itemPersonalizar = new JMenuItem("Personalizar...");
        itemPersonalizar.addActionListener(e -> accionPersonalizar());
        
        menuHerramientas.add(itemConfiguracion);
        menuHerramientas.add(itemEstadisticas);
        menuHerramientas.add(new JSeparator());
        menuHerramientas.add(itemPersonalizar);
        
        return menuHerramientas;
    }
    
    private JMenu crearMenuAyuda() {
        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.setMnemonic(KeyEvent.VK_Y);
        
        // Manual de usuario
        JMenuItem itemManual = new JMenuItem("Manual de usuario");
        itemManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        itemManual.addActionListener(e -> accionManual());
        
        // Atajos de teclado
        JMenuItem itemAtajos = new JMenuItem("Atajos de teclado");
        itemAtajos.addActionListener(e -> accionAtajos());
        
        // Separador
        JSeparator separador = new JSeparator();
        
        // Reportar problema
        JMenuItem itemReportar = new JMenuItem("Reportar problema...");
        itemReportar.addActionListener(e -> accionReportarProblema());
        
        // Acerca de
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de Temulingo");
        itemAcercaDe.addActionListener(e -> accionAcercaDe());
        
        menuAyuda.add(itemManual);
        menuAyuda.add(itemAtajos);
        menuAyuda.add(separador);
        menuAyuda.add(itemReportar);
        menuAyuda.add(new JSeparator());
        menuAyuda.add(itemAcercaDe);
        
        return menuAyuda;
    }
    
    // Clase interna para manejar acciones de tema
    private class TemaActionListener implements ActionListener {
        private final Tema tema;
        
        public TemaActionListener(Tema tema) {
            this.tema = tema;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            cambiarTema(tema);
        }
    }
    
    public void cambiarTema(Tema tema) {
        try {
        	ConfiguracionUI.cambiarTema(tema, padre);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(padre, 
                "Error al cambiar el tema: " + e.getMessage(),
                "Error de Tema", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Métodos de acción (implementar según necesidades de la aplicación)
    private void accionNuevo() {
        System.out.println("Acción: Nuevo archivo");
    }
    
    private void accionAbrir() {
        System.out.println("Acción: Abrir archivo");
    }
    
    private void accionGuardar() {
        System.out.println("Acción: Guardar");
    }
    
    private void accionGuardarComo() {
        System.out.println("Acción: Guardar como");
    }
    
    private void accionExportarPDF() {
        System.out.println("Acción: Exportar PDF");
    }
    
    private void accionExportarHTML() {
        System.out.println("Acción: Exportar HTML");
    }
    
    private void accionSalir() {
        System.exit(0);
    }
    
    private void accionDeshacer() {
        System.out.println("Acción: Deshacer");
    }
    
    private void accionRehacer() {
        System.out.println("Acción: Rehacer");
    }
    
    private void accionCortar() {
        System.out.println("Acción: Cortar");
    }
    
    private void accionCopiar() {
        System.out.println("Acción: Copiar");
    }
    
    private void accionPegar() {
        System.out.println("Acción: Pegar");
    }
    
    private void accionSeleccionarTodo() {
        System.out.println("Acción: Seleccionar todo");
    }
    
    private void accionBuscar() {
        System.out.println("Acción: Buscar");
    }
    
    private void accionReemplazar() {
        System.out.println("Acción: Reemplazar");
    }
    
    private void accionPantallaCompleta(boolean activar) {
        System.out.println("Acción: Pantalla completa - " + activar);
    }
    
    private void accionMostrarBarraEstado(boolean mostrar) {
        System.out.println("Acción: Mostrar barra de estado - " + mostrar);
    }
    
    private void accionConfiguracion() {
        System.out.println("Acción: Configuración");
    }
    
    private void accionEstadisticas() {
        System.out.println("Acción: Estadísticas");
    }
    
    private void accionPersonalizar() {
        System.out.println("Acción: Personalizar");
    }
    
    private void accionManual() {
        System.out.println("Acción: Manual de usuario");
    }
    
    private void accionAtajos() {
        System.out.println("Acción: Atajos de teclado");
    }
    
    private void accionReportarProblema() {
        System.out.println("Acción: Reportar problema");
    }
    
    private void accionAcercaDe() {
    	// Cargar el logo
    	ImageIcon logo = new ImageIcon(getClass().getResource(ConfiguracionImagenes.getRutaIcono("logo.icono"))); // o donde esté tu logo
    	JLabel labelLogo = new JLabel(logo);

    	// Crear panel principal
    	JPanel panel = new JPanel(new BorderLayout(10, 10));

    	// Crear panel de texto con BoxLayout vertical
    	JPanel panelTexto = new JPanel();
    	panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));

    	JLabel info = new JLabel(String.format(
    	    "<html><h2>%s %s</h2><p>%s</p><p>%s</p></html>",
    	    ConfiguracionTemulingo.NOMBRE,
    	    ConfiguracionTemulingo.VERSION,
    	    ConfiguracionTemulingo.DESCRIPCION,
    	    ConfiguracionTemulingo.AUTOR
    	));

    	JLabel enlace = new JLabel("<html><a href=''>" + ConfiguracionTemulingo.URL + "</a></html>");
    	enlace.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	enlace.setForeground(Color.BLUE);
    	enlace.addMouseListener(new MouseAdapter() {
    	    @Override
    	    public void mouseClicked(MouseEvent e) {
    	        try {
    	            Desktop.getDesktop().browse(new URI(ConfiguracionTemulingo.URL));
    	        } catch (Exception ex) {
    	            ex.printStackTrace();
    	        }
    	    }
    	});

    	// Añadir texto y enlace al panel de texto
    	panelTexto.add(info);
    	panelTexto.add(Box.createVerticalStrut(5)); // espacio entre texto y enlace
    	panelTexto.add(enlace);

    	// Montar panel principal
    	panel.add(labelLogo, BorderLayout.WEST);
    	panel.add(panelTexto, BorderLayout.CENTER);

    	// Mostrar el diálogo
    	JOptionPane.showMessageDialog(padre, panel, "Acerca de Temulingo", JOptionPane.INFORMATION_MESSAGE);

    }

}