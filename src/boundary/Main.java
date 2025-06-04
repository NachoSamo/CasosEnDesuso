package boundary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal que inicia la aplicación de Casos en Desuso.
 * Esta clase maneja la inicialización del sistema y el manejo de excepciones.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML desde /fxml/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PantallaPrincipal.fxml"));
            Parent root = loader.load();

            // Configurar la escena
            Scene scene = new Scene(root);
            // (Opcional) Cargar CSS si tenés uno:
            // scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            // Configurar la ventana principal
            primaryStage.setTitle("Sistema de Red Sismica");
            primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
