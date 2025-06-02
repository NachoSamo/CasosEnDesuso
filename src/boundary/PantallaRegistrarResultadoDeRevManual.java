package boundary;

import controlador.ControladorRegistrarResultadoDeRevManual;
import entidades.EventoSismico;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;

public class PantallaRegistrarResultadoDeRevManual {

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
    @FXML private ComboBox<String> comboAcciones;
    @FXML private Button btnEjecutar;
    @FXML private Button btnCancelar;
    @FXML private ImageView imagenSismograma;

    private EventoSismico eventoSeleccionado;
    private final ControladorRegistrarResultadoDeRevManual controladorCU = new ControladorRegistrarResultadoDeRevManual();

    @FXML
    public void initialize() {
        habilitarPantalla();
    }


    @FXML
    public void habilitarPantalla() {
        controladorCU.registrarResultadoDeRevMan(); // Se le pasa la pantalla como referencia
        habComboAcciones();
        if (txtusername != null && controladorCU.getEmpleadoResponsable() != null) {
            txtusername.setText(controladorCU.getEmpleadoResponsable().getUsuario().getUsername());
        }
        controladorCU.buscarESAutodetectado(tablaEventos, colFechaHora, colEpicentro, colHipocentro, colMagnitud);

    }


    private void habComboAcciones() {
        comboAcciones.getItems().addAll("Confirmar", "Rechazar", "Derivar a experto");
    }

    private void mostrarES() {
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrenciaTexto"));
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("latitudEpicentro"));
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("latitudHipocentro"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));
    }

    @FXML
    private void solicitarSeleccionES() {
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
