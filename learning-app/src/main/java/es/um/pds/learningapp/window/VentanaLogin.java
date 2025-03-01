package es.um.pds.learningapp.window;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VentanaLogin extends Application {

    private static final String WINDOW_TITLE = "Learning App - Login";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(WINDOW_TITLE);
        
        // Create main container with background gradient
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));
        
        // Set a gradient background
        Stop[] stops = new Stop[] {
            new Stop(0, Color.web("#6a11cb")),
            new Stop(1, Color.web("#2575fc"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        mainContainer.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Create a white rounded panel for login form
        VBox loginPanel = new VBox(15);
        loginPanel.setAlignment(Pos.CENTER);
        loginPanel.setPadding(new Insets(30));
        loginPanel.setMaxWidth(350);
        loginPanel.setBackground(new Background(new BackgroundFill(
            Color.WHITE, new CornerRadii(15), Insets.EMPTY
        )));
        
        // Add shadow effect to panel
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        loginPanel.setEffect(dropShadow);
        
        // Create a logo using JavaFX shapes instead of loading an image
        StackPane logoPane = createLogo();
        
        // Create the application title
        Text sceneTitle = new Text("Bienvenido a Temulingo");
        sceneTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        sceneTitle.setFill(Color.web("#3a3a3a"));
        
        // Subtitle text
        Text subtitle = new Text("Inicia sesión para empezar a aprender");
        subtitle.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitle.setFill(Color.web("#757575"));
        
        // Nombre de usuario field
        TextField userTextField = new TextField();
        userTextField.setPromptText("Nombre de usuario");
        userTextField.setPrefHeight(40);
        userTextField.setStyle("-fx-font-size: 14px;");
        
        // Contraseña field
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("Contraseña");
        pwBox.setPrefHeight(40);
        pwBox.setStyle("-fx-font-size: 14px;");
        
        // Remember me checkbox
        CheckBox rememberMe = new CheckBox("Recuérdame");
        rememberMe.setTextFill(Color.web("#757575"));
        
        // Login button
        Button loginBtn = new Button("Iniciar sesión");
        loginBtn.setPrefHeight(45);
        loginBtn.setPrefWidth(200);
        loginBtn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Register link
        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);
        Text registerText = new Text("¿No tienes una cuenta?");
        registerText.setFill(Color.web("#757575"));
        Hyperlink registerLink = new Hyperlink("Crear una ahora");
        registerBox.getChildren().addAll(registerText, registerLink);
        
        // Message text for feedback
        Text actionMessage = new Text();
        actionMessage.setFill(Color.FIREBRICK);
        
        // Add everything to login panel
        loginPanel.getChildren().addAll(
            logoPane, sceneTitle, subtitle, 
            userTextField, pwBox, rememberMe, 
            loginBtn, registerBox, actionMessage
        );
        
        // Add login panel to main container
        mainContainer.getChildren().add(loginPanel);
        
        // Add event handler for login button
        loginBtn.setOnAction(e -> {
            if (userTextField.getText().isEmpty() || pwBox.getText().isEmpty()) {
                actionMessage.setText("Please enter Nombre de usuario and password");
                actionMessage.setFill(Color.FIREBRICK);
            } else {
                actionMessage.setText("Login successful! Redirecting...");
                actionMessage.setFill(Color.web("#4CAF50"));
                // Here you would call your authentication service
                // If successful, you would navigate to the main application window
            }
        });
        
        // Add event handler for register link
        registerLink.setOnAction(e -> {
            actionMessage.setText("Opening registration page...");
            actionMessage.setFill(Color.web("#2196F3"));
            // Here you would open the registration window or navigate to registration scene
        });
        
        // Create scene
        Scene scene = new Scene(mainContainer, 450, 600);
        
        // Apply CSS styling
        applyButtonStyles(loginBtn);
        applyTextFieldStyles(userTextField, pwBox);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private StackPane createLogo() {
        // Create a logo container
        StackPane logoPane = new StackPane();
        logoPane.setMinSize(80, 80);
        logoPane.setMaxSize(80, 80);
        
        // Create outer circle with gradient
        Circle outerCircle = new Circle(40);
        Stop[] stops = new Stop[] {
            new Stop(0, Color.web("#6a11cb")),
            new Stop(1, Color.web("#2575fc"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        outerCircle.setFill(gradient);
        
        // Create inner circle (white background for book)
        Circle innerCircle = new Circle(35);
        innerCircle.setFill(Color.WHITE);
        
        // Create book shape
        Rectangle book = new Rectangle(50, 40);
        book.setFill(gradient);
        book.setTranslateY(0);
        
        // Create book pages
        Rectangle pages = new Rectangle(40, 30);
        pages.setFill(Color.WHITE);
        pages.setTranslateY(0);
        
        // Create text for logo
        Text learnText = new Text("L");
        learnText.setFont(Font.font("System", FontWeight.BOLD, 24));
        learnText.setFill(gradient);
        
        // Add all elements to logo pane
        logoPane.getChildren().addAll(outerCircle, innerCircle, book, pages, learnText);
        
        // Add shadow effect
        DropShadow logoShadow = new DropShadow();
        logoShadow.setRadius(10);
        logoShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        logoPane.setEffect(logoShadow);
        
        return logoPane;
    }
    
    private void applyButtonStyles(Button button) {
        button.setStyle(
            "-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-cursor: hand;" +
            "-fx-font-size: 16px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        
        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to right, #5a0cb3, #1565c0);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-cursor: hand;" +
            "-fx-font-size: 16px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        ));
        
        // Reset style on mouse exit
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-cursor: hand;" +
            "-fx-font-size: 16px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        ));
    }
    
    private void applyTextFieldStyles(TextField... fields) {
        String style = 
            "-fx-background-color: #f8f9fa;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 10 15 10 15;" +
            "-fx-font-size: 14px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 0);";
        
        String focusedStyle =
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #2575fc;" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 2;" +
            "-fx-padding: 10 15 10 15;" +
            "-fx-font-size: 14px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 0);";
        
        for (TextField field : fields) {
            field.setStyle(style);
            
            // Add focus styles
            field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (isFocused) {
                    field.setStyle(focusedStyle);
                } else {
                    field.setStyle(style);
                }
            });
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
