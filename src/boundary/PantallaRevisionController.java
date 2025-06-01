package boundary;

import controlador.ControladorRegistrarRevision;
import entidades.EventoSismico;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



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
    @FXML private ComboBox<String> comboAcciones;
    @FXML private Button btnEjecutar;
    @FXML private Button btnCancelar;

    private EventoSismico eventoSeleccionado;
    private final ControladorRegistrarRevision controladorCU = new ControladorRegistrarRevision();
    @FXML private ImageView imagenSismograma;

    @FXML
    public void initialize() {
        controladorCU.registrarResultadoDeRevMan();
        controladorCU.cargarEventos(tablaEventos, colFechaHora, colEpicentro, colHipocentro, colMagnitud);

        if (txtusername != null && controladorCU.getEmpleadoResponsable() != null) {
            String usuario = controladorCU.getEmpleadoResponsable().getUsuario().getUsername();
            txtusername.setText(usuario);
        }

        comboAcciones.getItems().addAll("Confirmar", "Rechazar", "Derivar a experto");
    }

    @FXML
    private void confirmarSeleccion() {
        eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            tablaEventos.setDisable(true);
            txtEventoSeleccionado.setText(eventoSeleccionado.toString());
            txtAlcance.setText(eventoSeleccionado.getAlcanceSismo().getNombre());
            txtClasificacion.setText(eventoSeleccionado.getClasificacionSismo().getNombre());
            txtOrigen.setText(eventoSeleccionado.getOrigenGeneracion().getNombre());

            // Mostrar por consola la información procesada del evento
            System.out.println("✅ Evento seleccionado: " + eventoSeleccionado.getFechaHoraOcurrencia());
            System.out.println("📍 Alcance: " + eventoSeleccionado.getAlcanceSismo().getNombre());
            System.out.println("🧭 Clasificación: " + eventoSeleccionado.getClasificacionSismo().getNombre());
            System.out.println("🔬 Origen: " + eventoSeleccionado.getOrigenGeneracion().getNombre());

            // Generar imagen a partir del CU usando detalles de muestras
            Image sismograma = controladorCU.generarSismograma(eventoSeleccionado);
            if (sismograma != null) {
                imagenSismograma.setImage(sismograma);
                imagenSismograma.setVisible(true);
                System.out.println("🖼 Imagen del sismograma generada correctamente.");
            } else {
                System.out.println("⚠ No se pudo generar el sismograma para el evento seleccionado.");
            }
        }
    }



    @FXML
    private void cancelar() {
        controladorCU.cancelar(eventoSeleccionado);
        tablaEventos.setDisable(false);
        mostrarAlerta("Evento retornado a estado Autodetectado.");
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
        // Por ahora, solo imprimir para confirmar que funciona
        System.out.println("🗺 Se seleccionó la opción de ver el mapa sísmico.");
    }

    @FXML
    private void tomarOptModificarDatos() {
        System.out.println("📝 Se seleccionó la opción de modificar datos.");
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
    }

    @FXML
    private void confirmarEvento() {
        if (eventoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un evento sísmico.");
            return;
        }

        controladorCU.confirmar(eventoSeleccionado);
    }

    @FXML
    private void derivarEvento() {
        if (eventoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un evento sísmico.");
            return;
        }

        controladorCU.derivar(eventoSeleccionado);
    }

    @FXML
    private void ejecutarAccion() {
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

        // El controlador se encarga de finalizar el caso de uso
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

        // Cierra la ventana
        Stage stage = (Stage) tablaEventos.getScene().getWindow();
        stage.close();
    }

}
