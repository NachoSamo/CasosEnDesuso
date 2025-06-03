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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn.CellDataFeatures;
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
    @FXML private ComboBox<String> comboAcciones;
    @FXML private Button btnEjecutar;
    @FXML private Button btnCancelar;
    @FXML private ImageView imagenSismograma;
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
        initialize();
    }


    @FXML
    public void initialize() {
        // Obtener sesiones mockeadas
        List<Sesion> sesiones = MockDatos.obtenerSesionesMock();
        // Buscar la sesión activa
        Sesion sesionActiva = Sesion.obtenerSesionActiva(sesiones);
        controladorCU.setBoundary(this); // 👈 le paso el boundary al controlador
        controladorCU.registrarResultadoDeRevMan(sesionActiva); // él llama a mostrarES(
        habilitarPantalla(sesionActiva);

    }




    // paso 1: habilitar ventana
    @FXML
    public void habilitarPantalla(Sesion sesionActiva) {
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrenciaTexto"));
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("coordEpicentro"));
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("coordHipocentro"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));


        List<EventoSismico> eventos = controladorCU.registrarResultadoDeRevMan(sesionActiva);
        // 3. LLAMO A mostrarES
        habComboAcciones();
        if (txtusername != null && controladorCU.buscarEmpleado(sesionActiva) != null) {
            txtusername.setText(controladorCU.buscarEmpleado(sesionActiva).getUsuario().getUsername());
        }
    }




    private void habComboAcciones() {
        comboAcciones.getItems().addAll("Confirmar", "Rechazar", "Derivar a experto");
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
        txtEventoSeleccionado.setText(eventoSeleccionado.toString());
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
    private void tomarAccion() {
        String accion = comboAcciones.getValue();
        if (eventoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un evento sísmico.");
            return;
        }
        if (accion == null) {
            mostrarAlerta("Debe seleccionar una acción a ejecutar.");
            return;
        }
        switch (accion.toLowerCase()) {
            case "confirmar" -> controladorCU.confirmar(eventoSeleccionado);
            case "rechazar" -> {
                if (eventoSeleccionado.getValorMagnitud() == 0
                        || eventoSeleccionado.getAlcanceSismo() == null
                        || eventoSeleccionado.getOrigenGeneracion() == null) {
                    mostrarAlerta("Faltan datos necesarios (magnitud, alcance u origen).");
                    return;
                }
                controladorCU.rechazar(eventoSeleccionado);
            }
            case "derivar a experto" -> controladorCU.derivar(eventoSeleccionado);
            default -> mostrarAlerta("Acción no reconocida.");
        }
        controladorCU.finCU("Acción ejecutada: " + accion);
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
