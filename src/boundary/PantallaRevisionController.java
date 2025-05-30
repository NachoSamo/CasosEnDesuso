package boundary;

import controlador.ControladorRegistrarRevision;
import entidades.EventoSismico;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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
    @FXML private Label txtusername;
    @FXML private HBox panelDetalles;
    @FXML private ImageView imagenSismograma;

    @FXML private Button btnMapa;
    @FXML private Button btnModificar;
    @FXML private ComboBox<String> comboAcciones;

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

        panelDetalles.setDisable(true);
        comboAcciones.getItems().addAll("Confirmar", "Rechazar", "Derivar");
    }

    @FXML
    private void confirmarSeleccion() {
        eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            tablaEventos.setDisable(true);
            panelDetalles.setDisable(false);

            txtEventoSeleccionado.setText(eventoSeleccionado.toString());
            txtAlcance.setText(eventoSeleccionado.getAlcanceSismo().getNombre());
            txtClasificacion.setText(eventoSeleccionado.getClasificacionSismo().getNombre());
            txtOrigen.setText(eventoSeleccionado.getOrigenGeneracion().getNombre());

            Image sismograma = controladorCU.obtenerImagenSismograma(eventoSeleccionado);
            if (sismograma != null) imagenSismograma.setImage(sismograma);
        }
    }

    @FXML
    private void tomarOptMapaSismico() {
        controladorCU.habOptMapa();
    }

    @FXML
    private void tomarOptModificarDatos() {
        controladorCU.habOptModificarDatos();
        controladorCU.habComboAcciones();
    }

    @FXML
    private void tomarAccion() {
        String accion = comboAcciones.getValue();
        if (accion != null) {
            controladorCU.tomarAccion(accion, eventoSeleccionado);
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
    private void modificarDatos() {
        controladorCU.modificarEvento(eventoSeleccionado);
    }
}
