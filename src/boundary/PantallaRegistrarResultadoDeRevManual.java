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
import java.util.Map;

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

        // 🟢 Esta línea es la clave
        controladorCU.tomarSeleccionES(eventoSeleccionado);

        mostrarSismograma();
    }


    public Map<String, String> mostrarDetalleES(Map<String, String> aco) {
        txtEventoSeleccionado.setText(eventoSeleccionado.getFechaHoraOcurrencia().toString());
        txtAlcance.setText(aco.getOrDefault("alcance", ""));
        txtClasificacion.setText(aco.getOrDefault("clasificacion", ""));
        txtOrigen.setText(aco.getOrDefault("origen", ""));
        return aco;
    }

    public void mostrarSismograma() {
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
        tomarAccion("confirmar");
    }

    @FXML
    private void rechazarEvento() {
        tomarAccion("rechazar");
    }

    @FXML
    private void derivarEvento() {
        tomarAccion("derivar");
    }

    @FXML
    private void cancelarAccion() {
        tomarAccion("cancelar");
    }


    public void habOptMapa() {
        System.out.println("🗺 Se habilitó la opción de ver el mapa sísmico.");
        // Aquí podrías agregar lógica para mostrar un mapa o realizar alguna acción relacionada.
    }


    /**
     * Método centralizado para manejar las acciones de los botones
     */
    private void tomarAccion(String accion) {

        switch (accion.toLowerCase()) {
            case "confirmar" -> {
                controladorCU.tomarAccion("confirmar");
                controladorCU.finCU("Evento confirmado correctamente.");
            }
            case "rechazar" -> {
                controladorCU.tomarAccion("rechazar");
                controladorCU.finCU("Evento rechazado.");
            }
            case "derivar" -> {
                controladorCU.tomarAccion("derivar");
                controladorCU.finCU("Evento derivado a experto.");
            }
            case "cancelar" -> {
                controladorCU.cancelar(eventoSeleccionado);
                mostrarDialogoYSalir("Evento restaurado al estado anterior.");
            }
            default -> mostrarAlerta("Acción no reconocida.");
        }
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
