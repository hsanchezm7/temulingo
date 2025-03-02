package es.um.pds.learningapp;

import java.util.Objects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Aplicación de Temulingo
 */
public class App extends Application {

    private static final String APP_NAME = "Temulingo";
    private static final String STYLESHEET_PATH = Objects.requireNonNull(
        App.class.getResource("/css/styles.css"),
        "No se encontró el recurso /css/styles.css"
    ).toExternalForm();


    public static String buildWindowTitle(String titlePart) {
        return APP_NAME + " - " + titlePart;
    }

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}