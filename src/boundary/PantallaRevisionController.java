package boundary;

import controlador.ControladorRegistrarRevision;
import entidades.EventoSismico;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

public class PantallaRevisionController {

    @FXML private TableView<EventoSismico> tablaEventos;
    @FXML private TableColumn<EventoSismico, String> colFechaHora;
    @FXML private TableColumn<EventoSismico, String> colEpicentro;
    @FXML private TableColumn<EventoSismico, String> colHipocentro;
    @FXML private TableColumn<EventoSismico, Double> colMagnitud;
    @FXML private TableColumn<EventoSismico, Boolean> colSeleccionar;

    @FXML private TextField txtEventoSeleccionado;
    @FXML private TextField txtAlcance;
    @FXML private TextField txtClasificacion;
    @FXML private TextField txtOrigen;
    @FXML private CheckBox checkMapa;

    private EventoSismico eventoSeleccionado;
    private final ControladorRegistrarRevision controladorCU = new ControladorRegistrarRevision();

    @FXML
    public void initialize() {
        controladorCU.cargarEventos(tablaEventos, colFechaHora, colEpicentro, colHipocentro, colMagnitud, colSeleccionar);
    }

    @FXML
    private void confirmarSeleccion() {
        eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            txtEventoSeleccionado.setText(eventoSeleccionado.toString());
            txtAlcance.setText(eventoSeleccionado.getAlcanceSismo().getNombre());
            txtClasificacion.setText(eventoSeleccionado.getClasificacionSismo().getNombre());
            txtOrigen.setText(eventoSeleccionado.getOrigenGeneracion().getNombre());
        }
    }

    @FXML
    private void confirmarEvento() {
        controladorCU.confirmar(eventoSeleccionado);
    }

    @FXML
    private void rechazarEvento() {
        controladorCU.rechazar(eventoSeleccionado);
    }

    @FXML
    private void derivarEvento() {
        controladorCU.derivar(eventoSeleccionado);
    }

    @FXML
    private void generarSismograma() {
        controladorCU.generarSismograma(eventoSeleccionado);
    }

    @FXML
    private void modificarDatos() {
        controladorCU.modificarEvento(eventoSeleccionado);
    }
}
