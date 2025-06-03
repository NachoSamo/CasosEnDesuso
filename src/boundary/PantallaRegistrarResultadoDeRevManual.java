package boundary;

import controlador.ControladorRegistrarResultadoDeRevManual;
import entidades.EventoSismico;
import entidades.MockDatos;
import entidades.Sesion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PantallaRegistrarResultadoDeRevManual {

    @FXML private TableView<EventoSismico> tablaEventos;
    @FXML private TableColumn<EventoSismico, String> colFechaHora;
    @FXML private TableColumn<EventoSismico, String> colEpicentro;
    @FXML private TableColumn<EventoSismico, String> colHipocentro;
    @FXML private TableColumn<EventoSismico, Double> colMagnitud;
    @FXML private StackPane contenedorCentral;
    @FXML private VBox vistaInicial;
    @FXML private VBox vistaCU;
    @FXML private TextField txtEventoSeleccionado;
    @FXML private TextField txtAlcance;
    @FXML private TextField txtClasificacion;
    @FXML private TextField txtOrigen;
    @FXML private Label txtusername;
    @FXML private Button btnCancelar;
    @FXML private ImageView imagenSismograma;
    @FXML private HBox seccionBottom;

    private Sesion sesionActiva;
    private EventoSismico eventoSeleccionado;
    private final ControladorRegistrarResultadoDeRevManual controladorCU = new ControladorRegistrarResultadoDeRevManual();

    public void setSesion(Sesion sesion) {
        this.sesionActiva = sesion;
    }

    @FXML
    private void iniciarCU() {
        vistaInicial.setVisible(false);
        vistaInicial.setManaged(false);

        vistaCU.setVisible(true);
        vistaCU.setManaged(true);
        seccionBottom.setVisible(true);
        seccionBottom.setManaged(true);

        initialize();
    }

    @FXML
    public void initialize() {
        List<Sesion> sesiones = MockDatos.obtenerSesionesMock();
        Sesion sesionActiva = Sesion.obtenerSesionActiva(sesiones);
        controladorCU.setBoundary(this);
        controladorCU.registrarResultadoDeRevMan(sesionActiva);
        habilitarPantalla(sesionActiva);
    }

    @FXML
    public void habilitarPantalla(Sesion sesionActiva) {
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrenciaTexto"));
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("coordEpicentro"));
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("coordHipocentro"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));

        List<EventoSismico> eventos = controladorCU.registrarResultadoDeRevMan(sesionActiva);
        if (txtusername != null && controladorCU.buscarEmpleado(sesionActiva) != null) {
            txtusername.setText(controladorCU.buscarEmpleado(sesionActiva).getUsuario().getUsername());
        }
    }

    public void mostrarES(List<EventoSismico> eventos) {
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));
    }

    @FXML
    public void solicitarSeleccionES() {
        eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            tomarSeleccionES();
        }
    }

    @FXML
    private void tomarSeleccionES() {
        if (eventoSeleccionado == null) {
            eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
            if (eventoSeleccionado == null) {
                mostrarAlerta("⚠ Debe seleccionar un evento sísmico antes de continuar.");
                return;
            }
        }

        tablaEventos.setDisable(true);
        mostrarDetalleES();
        mostrarSismograma();
    }

    private void mostrarDetalleES() {
        txtEventoSeleccionado.setText(eventoSeleccionado.getFechaHoraOcurrencia().toString());
        txtAlcance.setText(eventoSeleccionado.getAlcanceSismo().getNombre());
        txtClasificacion.setText(eventoSeleccionado.getClasificacionSismo().getNombre());
        txtOrigen.setText(eventoSeleccionado.getOrigenGeneracion().getNombre());
    }

    private void mostrarSismograma() {
        Image sismograma = controladorCU.generarSismograma(eventoSeleccionado);
        if (sismograma != null) {
            imagenSismograma.setImage(sismograma);
            imagenSismograma.setVisible(true);
            System.out.println("🖼 Imagen del sismograma generada correctamente.");
        } else {
            System.out.println("⚠ No se pudo generar el sismograma para el evento seleccionado.");
        }
    }

    @FXML
    private void confirmarEvento() {
        if (eventoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un evento sísmico.");
            return;
        }
        controladorCU.confirmar(eventoSeleccionado);
        controladorCU.finCU("Acción ejecutada: Confirmar");
    }

    @FXML
    private void rechazarEvento() {
        if (eventoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un evento sísmico.");
            return;
        }
        if (eventoSeleccionado.getValorMagnitud() == 0
                || eventoSeleccionado.getAlcanceSismo() == null
                || eventoSeleccionado.getOrigenGeneracion() == null) {
            mostrarAlerta("Faltan datos necesarios (magnitud, alcance u origen).");
            return;
        }
        controladorCU.rechazar(eventoSeleccionado);
        controladorCU.finCU("Acción ejecutada: Rechazar");
    }

    @FXML
    private void derivarEvento() {
        if (eventoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un evento sísmico.");
            return;
        }
        controladorCU.derivar(eventoSeleccionado);
        controladorCU.finCU("Acción ejecutada: Derivar a experto");
    }

    @FXML
    private void cancelarAccion() {
        if (eventoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un evento sísmico.");
            return;
        }
        controladorCU.cancelar(eventoSeleccionado);
        mostrarDialogoYSalir("Evento restaurado al estado 'Auto Detectado'.");
    }

    private void mostrarDialogoYSalir(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finalización de Caso de Uso");
        alert.setHeaderText("Revisión Finalizada");
        alert.setContentText(mensaje);
        alert.showAndWait();
        Stage stage = (Stage) tablaEventos.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Revisión Manual");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void tomarOptMapaSismico() {
        System.out.println("🗺 Se seleccionó la opción de ver el mapa sísmico.");
    }

    @FXML
    private void habOptModificarDatos() {
        System.out.println("📝 Se seleccionó la opción de modificar datos.");
    }

    @FXML
    private void tomarOptModificarDatos() {
        habOptModificarDatos();
    }
}
