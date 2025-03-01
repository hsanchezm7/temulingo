package es.um.pds.learningapp.window;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class QuestionWindowApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pregunta");

        // Crear contenedor principal con fondo degradado
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));
        
        // Establecer un fondo degradado
        Stop[] stops = new Stop[] {
            new Stop(0, Color.web("#6a11cb")),
            new Stop(1, Color.web("#2575fc"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        mainContainer.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Crear ventana de pregunta
        String question = "¿Cuál es la capital de Francia?";
        String[] answers = {
            "Londres",
            "Berlín",
            "París",
            "Madrid"
        };
        
        VBox questionWindow = createQuestionWindow(question, answers);
        
        // Añadir la ventana de pregunta al contenedor principal
        mainContainer.getChildren().add(questionWindow);
        
        // Crear escena y mostrar
        Scene scene = new Scene(mainContainer, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public VBox createQuestionWindow(String question, String[] answers) {
        // Contenedor principal
        VBox questionWindow = new VBox(20);
        questionWindow.setPadding(new Insets(25));
        questionWindow.setAlignment(Pos.TOP_LEFT);
        questionWindow.setPrefWidth(600);
        questionWindow.setStyle("-fx-background-color: white; " +
                               "-fx-background-radius: 15; " +
                               "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        
        // Etiqueta de pregunta
        Label questionLabel = new Label(question);
        questionLabel.setFont(Font.font("Montserrat", FontWeight.BOLD, 18));
        questionLabel.setStyle("-fx-text-fill: #333333;");
        questionLabel.setWrapText(true);
        
        // Icono para la pregunta
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(40, 40);
        iconContainer.setStyle("-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); " +
                              "-fx-background-radius: 10;");
        
        SVGPath questionIcon = createIcon("help-circle");
        questionIcon.setFill(Color.WHITE);
        iconContainer.getChildren().add(questionIcon);
        
        // Encabezado con icono y pregunta
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(iconContainer, questionLabel);
        
        // Contenedor de respuestas
        VBox answersContainer = new VBox(12);
        answersContainer.setPadding(new Insets(15, 0, 25, 0));
        
        ToggleGroup answerGroup = new ToggleGroup();
        
        for (String answer : answers) {
            RadioButton option = new RadioButton(answer);
            option.setFont(Font.font("Montserrat", FontWeight.NORMAL, 14));
            option.setStyle("-fx-text-fill: #555555;");
            option.setToggleGroup(answerGroup);
            option.setPadding(new Insets(8, 15, 8, 15));
            answersContainer.getChildren().add(option);
        }
        
        // Contenedor de botones
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        
        Button submitButton = new Button("Enviar Respuesta");
        submitButton.setFont(Font.font("Montserrat", FontWeight.BOLD, 14));
        submitButton.setStyle("-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); " +
                             "-fx-text-fill: white; " +
                             "-fx-background-radius: 8; " +
                             "-fx-padding: 10 20; " +
                             "-fx-cursor: hand;");
        
        Button skipButton = new Button("Omitir Pregunta");
        skipButton.setFont(Font.font("Montserrat", FontWeight.MEDIUM, 14));
        skipButton.setStyle("-fx-background-color: #f0f0f0; " +
                           "-fx-text-fill: #555555; " +
                           "-fx-background-radius: 8; " +
                           "-fx-padding: 10 20; " +
                           "-fx-cursor: hand;");
        
        // Añadir efectos de hover a los botones
        submitButton.setOnMouseEntered(e -> 
            submitButton.setStyle("-fx-background-color: linear-gradient(to right, #5910c0, #1a65e0); " +
                                 "-fx-text-fill: white; " +
                                 "-fx-background-radius: 8; " +
                                 "-fx-padding: 10 20; " +
                                 "-fx-cursor: hand;")
        );
        
        submitButton.setOnMouseExited(e -> 
            submitButton.setStyle("-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); " +
                                 "-fx-text-fill: white; " +
                                 "-fx-background-radius: 8; " +
                                 "-fx-padding: 10 20; " +
                                 "-fx-cursor: hand;")
        );
        
        skipButton.setOnMouseEntered(e -> 
            skipButton.setStyle("-fx-background-color: #e8e8e8; " +
                               "-fx-text-fill: #555555; " +
                               "-fx-background-radius: 8; " +
                               "-fx-padding: 10 20; " +
                               "-fx-cursor: hand;")
        );
        
        skipButton.setOnMouseExited(e -> 
            skipButton.setStyle("-fx-background-color: #f0f0f0; " +
                               "-fx-text-fill: #555555; " +
                               "-fx-background-radius: 8; " +
                               "-fx-padding: 10 20; " +
                               "-fx-cursor: hand;")
        );
        
        buttonContainer.getChildren().addAll(skipButton, submitButton);
        
        // Añadir todos los componentes al contenedor principal
        questionWindow.getChildren().addAll(header, answersContainer, buttonContainer);
        
        // Añadir manejadores de eventos a los botones
        submitButton.setOnAction(e -> {
            RadioButton selectedAnswer = (RadioButton) answerGroup.getSelectedToggle();
            if (selectedAnswer != null) {
                // Procesar la respuesta seleccionada
                System.out.println("Respuesta enviada: " + selectedAnswer.getText());
                
                // Mostrar resultado como una alerta
                boolean correct = "París".equals(selectedAnswer.getText());
                showResultAlert(correct, "París");
            } else {
                // Mostrar alerta de que no se seleccionó ninguna respuesta
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sin Selección");
                alert.setHeaderText("Por favor selecciona una respuesta");
                alert.setContentText("Debes seleccionar una respuesta antes de enviar.");
                alert.showAndWait();
            }
        });
        
        skipButton.setOnAction(e -> {
            // Manejar omisión de la pregunta
            System.out.println("Pregunta omitida");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pregunta Omitida");
            alert.setHeaderText("Pasando a la Siguiente Pregunta");
            alert.setContentText("Has omitido esta pregunta. La respuesta correcta era: París");
            alert.showAndWait();
        });
        
        return questionWindow;
    }
    
    private void showResultAlert(boolean correct, String correctAnswer) {
        Alert alert = new Alert(correct ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Resultado de la Respuesta");
        
        if (correct) {
            alert.setHeaderText("¡Respuesta Correcta!");
            alert.setContentText("¡Bien hecho! Tu respuesta es correcta.");
        } else {
            alert.setHeaderText("Respuesta Incorrecta");
            alert.setContentText("Lo siento, eso no es correcto. La respuesta correcta es: " + correctAnswer);
        }
        
        alert.showAndWait();
    }
    
    private SVGPath createIcon(String iconType) {
        SVGPath icon = new SVGPath();
        
        switch(iconType) {
            case "help-circle":
                icon.setContent("M12,2C6.48,2,2,6.48,2,12s4.48,10,10,10s10-4.48,10-10S17.52,2,12,2z M12,20c-4.41,0-8-3.59-8-8s3.59-8,8-8s8,3.59,8,8S16.41,20,12,20z M11,16h2v2h-2V16z M12.61,6.04c-2.06-0.3-3.88,0.97-4.43,2.79c-0.18,0.58,0.26,1.17,0.87,1.17h0.2c0.41,0,0.74-0.29,0.88-0.67c0.32-0.89,1.27-1.5,2.3-1.28c0.95,0.2,1.65,1.13,1.57,2.1c-0.1,1.34-1.62,1.63-2.45,2.88c-0.22,0.34-0.34,0.73-0.34,1.14v0.33c0,0.55,0.45,1,1,1h0.18c0.55,0,0.96-0.45,0.96-1v-0.12c0-0.46,0.24-0.89,0.64-1.1c0.69-0.36,1.85-1.08,1.97-2.47C15.52,8.09,14.34,6.35,12.61,6.04z");
                break;
            default:
                icon.setContent("");
        }
        
        return icon;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}