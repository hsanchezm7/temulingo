package es.um.pds.learningapp.window;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VentanaRegister extends Application {

    private static final String WINDOW_TITLE = "Learning App - Crear cuenta";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(WINDOW_TITLE);

        // Contenedor principal con fondo degradado
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        // Fondo degradado
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#6a11cb")),
                new Stop(1, Color.web("#2575fc"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        mainContainer.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Panel blanco con esquinas redondeadas y sombra
        VBox registerPanel = new VBox(15);
        registerPanel.setAlignment(Pos.CENTER);
        registerPanel.setPadding(new Insets(30));
        registerPanel.setMaxWidth(400);
        registerPanel.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(15), Insets.EMPTY
        )));

        // Efecto de sombra en el panel
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        registerPanel.setEffect(dropShadow);

        // Encabezado
        Text headerText = new Text("Crea tu Cuenta");
        headerText.setFont(Font.font("Montserrat", FontWeight.BOLD, 24));
        headerText.setStyle("-fx-fill: #333333;");

        // Descripción del formulario
        Text subHeaderText = new Text("Por favor completa tus datos para comenzar");
        subHeaderText.setFont(Font.font("Montserrat", 14));
        subHeaderText.setStyle("-fx-fill: #757575;");

        // Contenedor del formulario
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);

        // Campo nombre
        Label firstNameLabel = new Label("Nombre");
        firstNameLabel.setStyle("-fx-text-fill: #333333;");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Introduce tu nombre");
        formGrid.add(firstNameLabel, 0, 0);
        formGrid.add(firstNameField, 0, 1);

        // Campo apellido
        Label lastNameLabel = new Label("Apellido");
        lastNameLabel.setStyle("-fx-text-fill: #333333;");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Introduce tu apellido");
        formGrid.add(lastNameLabel, 1, 0);
        formGrid.add(lastNameField, 1, 1);

        // Campo correo electrónico
        Label emailLabel = new Label("Correo Electrónico");
        emailLabel.setStyle("-fx-text-fill: #333333;");
        TextField emailField = new TextField();
        emailField.setPromptText("ejemplo@correo.com");
        formGrid.add(emailLabel, 0, 2);
        formGrid.add(emailField, 0, 3, 2, 1);

        // Campo de contraseña
        Label passwordLabel = new Label("Contraseña");
        passwordLabel.setStyle("-fx-text-fill: #333333;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Crea una contraseña");
        formGrid.add(passwordLabel, 0, 4);
        formGrid.add(passwordField, 0, 5);

        // Campo de confirmación de contraseña
        Label confirmPasswordLabel = new Label("Confirmar Contraseña");
        confirmPasswordLabel.setStyle("-fx-text-fill: #333333;");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirma tu contraseña");
        formGrid.add(confirmPasswordLabel, 1, 4);
        formGrid.add(confirmPasswordField, 1, 5);

        // Términos y condiciones
        CheckBox termsCheckBox = new CheckBox("Acepto los Términos y Condiciones");
        HBox termsContainer = new HBox(termsCheckBox);
        termsContainer.setAlignment(Pos.CENTER_LEFT);
        termsContainer.setPadding(new Insets(5, 0, 10, 0));

        // Botón de registro
        Button registerButton = new Button("CREAR CUENTA");
        registerButton.setPrefWidth(200);

        // Enlace para iniciar sesión
        HBox loginLinkContainer = new HBox();
        loginLinkContainer.setAlignment(Pos.CENTER);
        Label loginText = new Label("¿Ya tienes una cuenta? ");
        loginText.setStyle("-fx-text-fill: #757575;");
        Hyperlink loginLink = new Hyperlink("Iniciar Sesión");
        loginLinkContainer.getChildren().addAll(loginText, loginLink);

        // Añadir elementos al panel de registro
        registerPanel.getChildren().addAll(
                headerText,
                subHeaderText,
                formGrid,
                termsContainer,
                registerButton,
                loginLinkContainer
        );

        // Añadir el panel al contenedor principal
        mainContainer.getChildren().add(registerPanel);

        // Crear escena y aplicar CSS
        Scene scene = new Scene(mainContainer, 600, 650);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
