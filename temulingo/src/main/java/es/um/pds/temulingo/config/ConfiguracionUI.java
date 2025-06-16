package es.um.pds.temulingo.config;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ConfiguracionUI {

	private static final Tema TEMA_POR_DEFECTO = Tema.FLAT_INTELLIJ;
	private static final int RADIO_BORDES = 10;

	private static Tema temaActual = null;

	public static void inicializar() {
		setTemaActual(TEMA_POR_DEFECTO);

		try {
			cambiarTema(TEMA_POR_DEFECTO, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Opciones en castellano
		UIManager.put("OptionPane.yesButtonText", "Sí");
		UIManager.put("OptionPane.noButtonText", "No");

		configurarFuente();

		// Aviso: puede no funcionar con ciertos temas
		ImageIcon iconoInfo = new ImageIcon(
				ConfiguracionUI.class.getResource(ConfiguracionTemulingo.getRutaIcono("info.icono")));
		ImageIcon iconoError = new ImageIcon(
				ConfiguracionUI.class.getResource(ConfiguracionTemulingo.getRutaIcono("error.icono")));

		UIManager.put("OptionPane.informationIcon", iconoInfo);
		UIManager.put("OptionPane.errorIcon", iconoError);

		UIManager.put("Component.arc", RADIO_BORDES);
		UIManager.put("TextComponent.arc", RADIO_BORDES);
	}

	private static void configurarFuente() {
		try {
			InputStream fontStream = ConfiguracionUI.class.getResourceAsStream("/fonts/Inter.ttf");
			Font interFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(13f);

			UIManager.put("defaultFont", interFont);
			System.setProperty("awt.useSystemAAFontSettings", "on"); // Anti-aliasing del texto
		} catch (NullPointerException | FontFormatException | IOException e) {
			e.printStackTrace();
		}

	}

	public enum CategoriasTema {
		FLATLAF("Temas de FlatLaf"), SISTEMA("Temas del Sistema"), CLASICOS("Temas Clásicos"),
		INTELLIJ("Temas de IntelliJ");

		private final String titulo;

		CategoriasTema(String titulo) {
			this.titulo = titulo;
		}

		public String getTitulo() {
			return titulo;
		}
	}

	public enum Tema {
		FLAT_LIGHT("Light", "com.formdev.flatlaf.FlatLightLaf", CategoriasTema.FLATLAF),
		FLAT_DARK("Dark", "com.formdev.flatlaf.FlatDarkLaf", CategoriasTema.FLATLAF),
		FLAT_INTELLIJ("IntelliJ", "com.formdev.flatlaf.FlatIntelliJLaf", CategoriasTema.FLATLAF),
		FLAT_DARCULA("Darcula", "com.formdev.flatlaf.FlatDarculaLaf", CategoriasTema.FLATLAF),
		SISTEMA("Sistema", UIManager.getSystemLookAndFeelClassName(), CategoriasTema.SISTEMA),
		METAL("Metal", "javax.swing.plaf.metal.MetalLookAndFeel", CategoriasTema.CLASICOS),
		NIMBUS("Nimbus", "javax.swing.plaf.nimbus.NimbusLookAndFeel", CategoriasTema.CLASICOS),
		ARC_ORANGE("Arc - Orange", "com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme", CategoriasTema.INTELLIJ),
		ARC_DARK_ORANGE("Arc Dark - Orange", "com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme",
				CategoriasTema.INTELLIJ),
		CARBON("Carbon", "com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme", CategoriasTema.INTELLIJ),
		COBALT2("Cobalt 2", "com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme", CategoriasTema.INTELLIJ),
		CYAN_LIGHT("Cyan light", "com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme", CategoriasTema.INTELLIJ),
		LIGHT_FLAT("Light Flat", "com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme", CategoriasTema.INTELLIJ),
		MATERIAL_DESIGN_DARK("Material Design Dark", "com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme",
				CategoriasTema.INTELLIJ),
		MONOCAI("Monocai", "com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme", CategoriasTema.INTELLIJ),
		SPACEGRAY("Spacegray", "com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme", CategoriasTema.INTELLIJ),
		VUESION("Vuesion", "com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme", CategoriasTema.INTELLIJ);

		private final String nombre;
		private final String className;
		private final CategoriasTema categoria;

		Tema(String nombre, String className, CategoriasTema categoria) {
			this.nombre = nombre;
			this.className = className;
			this.categoria = categoria;
		}

		public String getNombre() {
			return nombre;
		}

		public String getClassName() {
			return className;
		}

		public CategoriasTema getCategoria() {
			return categoria;
		}
	}

	public static void cambiarTema(Tema tema, Component parent) throws Exception {
		UIManager.setLookAndFeel(tema.getClassName());

		if (parent != null) {
			SwingUtilities.updateComponentTreeUI(parent);
		}

		setTemaActual(tema);
	}

	public static Tema getTemaActual() {
		return temaActual;
	}

	public static void setTemaActual(Tema temaActual) {
		ConfiguracionUI.temaActual = temaActual;
	}

}
