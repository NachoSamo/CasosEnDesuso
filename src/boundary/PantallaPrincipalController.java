package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class PantallaPrincipalController {
    @FXML private TableView<String> tablaCasos;
    @FXML private TableColumn<String, String> columnaId;
    @FXML private TableColumn<String, String> columnaDescripcion;
    @FXML private TableColumn<String, String> columnaEstado;
    private List<String> casosEnDesuso;
    private ObservableList<String> casosObservable;

    @FXML
    public void initialize() {
        casosEnDesuso = new ArrayList<>();
        casosObservable = FXCollections.observableArrayList();

        // Agregar algunos casos de ejemplo
        casosEnDesuso.add("Caso 1: Sistema de facturación antiguo");
        casosEnDesuso.add("Caso 2: Módulo de reportes obsoleto");

        // Configurar la tabla
        tablaCasos.setItems(casosObservable);
        actualizarTabla();
    }

    @FXML
    private void verCasos() {
        actualizarTabla();
    }

    @FXML
    private void registrarCaso() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Registrar Nuevo Caso");
        dialog.setHeaderText("Ingrese la descripción del caso");
        dialog.setContentText("Descripción:");

        dialog.showAndWait().ifPresent(descripcion -> {
            casosEnDesuso.add(descripcion);
            actualizarTabla();
        });
    }

    @FXML
    private void actualizarEstado() {
        String casoSeleccionado = tablaCasos.getSelectionModel().getSelectedItem();
        if (casoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Actualizar Estado");
            alert.setHeaderText("Estado actualizado");
            alert.setContentText("El estado del caso ha sido actualizado (mock)");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Ningún caso seleccionado");
            alert.setContentText("Por favor, seleccione un caso para actualizar su estado.");
            alert.showAndWait();
        }
    }

    @FXML
    private void generarReporte() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reporte");
        alert.setHeaderText("Reporte de Casos en Desuso");
        alert.setContentText("Total de casos: " + casosEnDesuso.size());
        alert.showAndWait();
    }

    private void actualizarTabla() {
        casosObservable.clear();
        casosObservable.addAll(casosEnDesuso);
    }
}
