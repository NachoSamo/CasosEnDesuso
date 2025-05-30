package boundary;

import controlador.ControladorRegistrarRevision;
import entidades.EventoSismico;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class PantallaRevisionController {

    @FXML private TableView<EventoSismico> tablaEventos;
    @FXML private TableColumn<EventoSismico, String> colFechaHora;
    @FXML private TableColumn<EventoSismico, String> colEpicentro;
    @FXML private TableColumn<EventoSismico, String> colHipocentro;
    @FXML private TableColumn<EventoSismico, Double> colMagnitud;

    @FXML private TextField txtEventoSeleccionado;
    @FXML private TextField txtAlcance;
    @FXML private TextField txtClasificacion;
    @FXML private TextField txtOrigen;
    @FXML private CheckBox checkMapa;
    @FXML private Label txtusername;
    @FXML private VBox panelDetalles; // se agrega para poder habilitarlo/deshabilitarlo dinámicamente

    private EventoSismico eventoSeleccionado;
    private final ControladorRegistrarRevision controladorCU = new ControladorRegistrarRevision();

    @FXML
    public void initialize() {
        controladorCU.registrarResultadoDeRevMan();
        controladorCU.cargarEventos(tablaEventos, colFechaHora, colEpicentro, colHipocentro, colMagnitud);

        if (txtusername != null && controladorCU.getEmpleadoResponsable() != null) {
            String usuario = controladorCU.getEmpleadoResponsable().getUsuario().getUsername();
            txtusername.setText(usuario);
        }

        if (panelDetalles != null) {
            panelDetalles.setDisable(true);
        }
    }

    @FXML
    private void confirmarSeleccion() {
        eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            tablaEventos.setDisable(true); // bloquea la selección
            panelDetalles.setDisable(false); // habilita panel de datos
            txtEventoSeleccionado.setText(eventoSeleccionado.toString());
            txtAlcance.setText(eventoSeleccionado.getAlcanceSismo().getNombre());
            txtClasificacion.setText(eventoSeleccionado.getClasificacionSismo().getNombre());
            txtOrigen.setText(eventoSeleccionado.getOrigenGeneracion().getNombre());

            // controladorCU.bloquearEvento(eventoSeleccionado); // ← descomentá cuando implementes
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
