package boundary;

import entidades.SeedDatos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.PersistenceManager;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // --- PASO 1: INICIALIZAR LA BASE DE DATOS ---
        // Se llama al seeder ANTES de que se cargue cualquier otra parte de la aplicación.
        // Esto asegura que los datos existan antes de que alguien intente consultarlos.
        try {
            java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("America/Argentina/Cordoba"));
            SeedDatos.inicializarDatosSiNecesario();
        } catch (Exception e) {
            System.err.println("FALLO CRITICO: No se pudieron inicializar los datos de la base de datos.");
            e.printStackTrace();
            // Opcionalmente, mostrar una alerta y cerrar la app.
            return;
        }

        // --- PASO 2: CARGAR LA INTERFAZ DE USUARIO ---
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PantallaPrincipal.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Sistema de Red Sismica");
            primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error al iniciar la aplicacion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        // --- PASO 3: CERRAR LA CONEXIÓN A LA BASE DE DATOS ---
        // Este método se llama automáticamente cuando la aplicación JavaFX se cierra.
        // Es el lugar perfecto para liberar los recursos de la base de datos.
        PersistenceManager.close();
        System.out.println("Conexion a la base de datos cerrada limpiamente.");
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}