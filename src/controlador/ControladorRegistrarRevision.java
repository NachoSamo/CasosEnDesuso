package controlador;

import entidades.EventoSismico;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ControladorRegistrarRevision {

    public void cargarEventos(
            TableView<EventoSismico> tabla,
            TableColumn<EventoSismico, String> colFechaHora,
            TableColumn<EventoSismico, String> colEpicentro,
            TableColumn<EventoSismico, String> colHipocentro,
            TableColumn<EventoSismico, Double> colMagnitud,
            TableColumn<EventoSismico, Boolean> colSeleccionar
    ) {
        // Configuración de columnas
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrencia"));
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("latitudEpicentro"));
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("latitudHipocentro"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));
        colSeleccionar.setCellValueFactory(new PropertyValueFactory<>("seleccionado")); // requiere boolean en EventoSismico

        // Datos de prueba
        ObservableList<EventoSismico> datos = FXCollections.observableArrayList(
                // Agregá eventos reales desde BD o mocks
        );

        tabla.setItems(datos);
    }

    // Métodos vacíos para evitar más errores
    public void confirmar(EventoSismico ev) {}
    public void rechazar(EventoSismico ev) {}
    public void derivar(EventoSismico ev) {}
    public void generarSismograma(EventoSismico ev) {}
    public void modificarEvento(EventoSismico ev) {}
}
