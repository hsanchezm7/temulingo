package es.um.pds.learningapp.window;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VentanaMain extends Application {

    private final String WINDOW_TITLE = "TemuLingo";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(WINDOW_TITLE );

        // Create the main layout container
        BorderPane mainLayout = new BorderPane();
        
        // Create header
        HBox header = createHeader();
        mainLayout.setTop(header);
        
        // Create content area with cards
        ScrollPane contentScroll = new ScrollPane();
        contentScroll.setFitToWidth(true);
        contentScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        FlowPane cardsContainer = createCardsLayout();
        contentScroll.setContent(cardsContainer);
        mainLayout.setCenter(contentScroll);
        
        // Set background gradient
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8f9fa, #e8eaf6);");
        
        // Create scene and apply CSS
        Scene scene = new Scene(mainLayout, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setSpacing(20);
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        header.setAlignment(Pos.CENTER_LEFT);
        
        // App title
        Text appTitle = new Text("Dashboard");
        appTitle.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));
        appTitle.setStyle("-fx-fill: #333333;");
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Profile button
        Button profileButton = new Button("My Profile");
        profileButton.setStyle("-fx-font-size: 13px;");
        
        header.getChildren().addAll(appTitle, spacer, profileButton);
        return header;
    }
    
    private FlowPane createCardsLayout() {
        FlowPane cardsContainer = new FlowPane();
        cardsContainer.setPadding(new Insets(30));
        cardsContainer.setHgap(25);
        cardsContainer.setVgap(25);
        cardsContainer.setPrefWrapLength(850); // preferred width allows for 3 cards across
        
        // Agregar contenedores de cursos y funcionalidades relacionadas
        cardsContainer.getChildren().addAll(
            createCard("book", "Cursos Disponibles", "Explora y accede a todos los cursos disponibles en la plataforma."),
            createCard("plus-circle", "Crear Curso", "Crea un nuevo curso y personaliza su contenido y configuración."),
            createCard("bar-chart", "Estadísticas", "Consulta informes detallados sobre el rendimiento de los cursos y estudiantes.")
        );

        
        return cardsContainer;
    }
    
    private VBox createCard(String iconType, String title, String description) {
        // Card container
        VBox card = new VBox();
        card.setPadding(new Insets(25));
        card.setSpacing(15);
        card.setPrefWidth(250);
        card.setPrefHeight(200);
        card.setStyle("-fx-background-color: white; " +
                      "-fx-background-radius: 15; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2); " +
                      "-fx-cursor: hand;");
        
        // Add hover effect
        card.setOnMouseEntered(e -> 
            card.setStyle("-fx-background-color: white; " +
                          "-fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 4); " +
                          "-fx-cursor: hand;")
        );
        
        card.setOnMouseExited(e -> 
            card.setStyle("-fx-background-color: white; " +
                          "-fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2); " +
                          "-fx-cursor: hand;")
        );
        
        // Icon container (with gradient background)
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(50, 50);
        iconContainer.setStyle("-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); " +
                              "-fx-background-radius: 10;");
        
        // Create icon
        SVGPath icon = createIcon(iconType);
        icon.setFill(Color.WHITE);
        iconContainer.getChildren().add(icon);
        
        // Card title
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Montserrat", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #333333;");
        
        // Card description
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Montserrat", 13));
        descLabel.setStyle("-fx-text-fill: #757575;");
        descLabel.setWrapText(true);
        
        // Add all elements to card
        card.getChildren().addAll(iconContainer, titleLabel, descLabel);
        
        return card;
    }
    
    private SVGPath createIcon(String iconType) {
        SVGPath icon = new SVGPath();
        
        // Define SVG paths for different icon types
        switch (iconType) {
            case "book":
                icon.setContent("M18,2H6A2,2 0 0,0 4,4V20A2,2 0 0,0 6,22H18A2,2 0 0,0 20,20V4A2,2 0 0,0 18,2M18,20H6V4H18V20M8,6H16V8H8V6M8,10H14V12H8V10Z");
                break;
            case "plus-circle":
                icon.setContent("M12,2C6.48,2,2,6.48,2,12s4.48,10,10,10s10-4.48,10-10S17.52,2,12,2z M17,13h-4v4h-2v-4H7v-2h4V7h2v4h4V13z");
                break;
            
            case "bar-chart":
                icon.setContent("M3,20H21V18H3V20z M6,14v4h2v-4H6z M12,4v14h2V4H12z M18,10v8h2v-8H18z");
                break;
            case "analytics":
                icon.setContent("M3,16H8V14H3V16M9.5,16H14.5V14H9.5V16M16,16H21V14H16V16M3,20H5V18H3V20M7,20H9V18H7V20M11,20H13V18H11V20M15,20H17V18H15V20M19,20H21V18H19V20M3,12H11V10H3V12M13,12H21V10H13V12M3,4V8H21V4H3Z");
                break;
            case "settings":
                icon.setContent("M12,15.5A3.5,3.5 0 0,1 8.5,12A3.5,3.5 0 0,1 12,8.5A3.5,3.5 0 0,1 15.5,12A3.5,3.5 0 0,1 12,15.5M19.43,12.97C19.47,12.65 19.5,12.33 19.5,12C19.5,11.67 19.47,11.34 19.43,11L21.54,9.37C21.73,9.22 21.78,8.95 21.66,8.73L19.66,5.27C19.54,5.05 19.27,4.96 19.05,5.05L16.56,6.05C16.04,5.66 15.5,5.32 14.87,5.07L14.5,2.42C14.46,2.18 14.25,2 14,2H10C9.75,2 9.54,2.18 9.5,2.42L9.13,5.07C8.5,5.32 7.96,5.66 7.44,6.05L4.95,5.05C4.73,4.96 4.46,5.05 4.34,5.27L2.34,8.73C2.21,8.95 2.27,9.22 2.46,9.37L4.57,11C4.53,11.34 4.5,11.67 4.5,12C4.5,12.33 4.53,12.65 4.57,12.97L2.46,14.63C2.27,14.78 2.21,15.05 2.34,15.27L4.34,18.73C4.46,18.95 4.73,19.03 4.95,18.95L7.44,17.94C7.96,18.34 8.5,18.68 9.13,18.93L9.5,21.58C9.54,21.82 9.75,22 10,22H14C14.25,22 14.46,21.82 14.5,21.58L14.87,18.93C15.5,18.67 16.04,18.34 16.56,17.94L19.05,18.95C19.27,19.03 19.54,18.95 19.66,18.73L21.66,15.27C21.78,15.05 21.73,14.78 21.54,14.63L19.43,12.97Z");
                break;
            case "messages":
                icon.setContent("M20,2H4A2,2 0 0,0 2,4V22L6,18H20A2,2 0 0,0 22,16V4C22,2.89 21.1,2 20,2Z");
                break;
            case "calendar":
                icon.setContent("M19,19H5V8H19M16,1V3H8V1H6V3H5C3.89,3 3,3.89 3,5V19A2,2 0 0,0 5,21H19A2,2 0 0,0 21,19V5C21,3.89 20.1,3 19,3H18V1M17,12H12V17H17V12Z");
                break;
            case "documents":
                icon.setContent("M13,9H18.5L13,3.5V9M6,2H14L20,8V20A2,2 0 0,1 18,22H6C4.89,22 4,21.1 4,20V4C4,2.89 4.89,2 6,2M15,18V16H6V18H15M18,14V12H6V14H18Z");
                break;
            case "users":
                icon.setContent("M16,13C15.71,13 15.38,13 15.03,13.05C16.19,13.89 17,15 17,16.5V19H23V16.5C23,14.17 18.33,13 16,13M8,13C5.67,13 1,14.17 1,16.5V19H15V16.5C15,14.17 10.33,13 8,13M8,11A3,3 0 0,0 11,8A3,3 0 0,0 8,5A3,3 0 0,0 5,8A3,3 0 0,0 8,11M16,11A3,3 0 0,0 19,8A3,3 0 0,0 16,5A3,3 0 0,0 13,8A3,3 0 0,0 16,11Z");
                break;
            default:
                icon.setContent("M12,2A10,10 0 0,1 22,12A10,10 0 0,1 12,22A10,10 0 0,1 2,12A10,10 0 0,1 12,2Z");
        }
        
        // Scale the icon to fit the container
        icon.setScaleX(0.8);
        icon.setScaleY(0.8);
        
        return icon;
    }

    public static void main(String[] args) {
        launch(args);
    }
}