package boundary;

import gestor.GestorRevManual;
import entidades.EventoSismico;
import entidades.Sesion;
import entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PantallaRegistrarResultadoDeRevManual {

    @FXML private TableView<EventoSismico> tablaEventos;
    @FXML private TableColumn<EventoSismico, String> colFechaHora;
    @FXML private TableColumn<EventoSismico, String> colEpicentro;
    @FXML private TableColumn<EventoSismico, String> colHipocentro;
    @FXML private TableColumn<EventoSismico, Double> colMagnitud;
    @FXML private VBox vistaInicial;
    @FXML private VBox vistaCU;
    @FXML private TextField txtEventoSeleccionado;
    @FXML private TextField txtAlcance;
    @FXML private TextField txtClasificacion;
    @FXML private TextField txtOrigen;
    @FXML private Label txtusername;
    @FXML private ImageView imagenSismograma;
    @FXML private HBox seccionBottom;

    private Sesion sesionActiva;
    private EventoSismico eventoSeleccionado;
    private final GestorRevManual controladorCU = new GestorRevManual();

    @FXML
    public void initialize() {
        controladorCU.setBoundary(this);
        // Inicialización de columnas movida aquí para que esté disponible si se llama a mostrarES antes de iniciarCU
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrenciaTexto"));
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("coordEpicentro"));
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("coordHipocentro"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));
    }

    @FXML
    private void iniciarCU() {
        vistaInicial.setVisible(false);
        vistaInicial.setManaged(false);

        vistaCU.setVisible(true);
        vistaCU.setManaged(true);
        seccionBottom.setVisible(true);
        seccionBottom.setManaged(true);

        // --- SIMULACIÓN DE SESIÓN (Se mantiene la lógica original) ---
        Usuario usuarioLogueado = new Usuario("lgomez", null);
        this.sesionActiva = new Sesion(LocalDateTime.now(), null, usuarioLogueado);

        // Se verifica que el gestor encuentre el empleado para actualizar la etiqueta
        if (txtusername != null && controladorCU.buscarEmpleado(this.sesionActiva) != null) {
            txtusername.setText(controladorCU.buscarEmpleado(this.sesionActiva).getUsuario().getUsername());
        }

        // El gestor obtiene los datos de la base de datos.
        controladorCU.registrarResultadoDeRevMan(this.sesionActiva);
    }

    public void mostrarES(List<EventoSismico> eventos) {
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));
    }

    @FXML
    private void tomarSeleccionES() {
        eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado == null) {
            return;
        }

        tablaEventos.setDisable(true);
        controladorCU.tomarSeleccionES(eventoSeleccionado);
        mostrarSismograma();
    }

    public Map<String, String> mostrarDetalleES(Map<String, String> aco) {
        txtEventoSeleccionado.setText(eventoSeleccionado.getFechaHoraOcurrencia().toString());
        // Se asume que los objetos embeddable no son nulos
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
        } else {
            System.out.println("No se pudo generar el sismograma para el evento seleccionado.");
            imagenSismograma.setVisible(false); // Ocultar si no hay imagen
        }
    }

    @FXML private void confirmarEvento() { tomarAccion("confirmar"); }
    @FXML private void rechazarEvento() { tomarAccion("rechazar"); }
    @FXML private void derivarEvento() { tomarAccion("derivar"); }

    @FXML
    private void cancelarAccion() {
        refrescarVista();
        mostrarAlerta("Operación cancelada. Puede seleccionar otro evento.");
    }

    private void tomarAccion(String accion) {
        if (eventoSeleccionado == null) {
            mostrarAlerta("No hay ningún evento seleccionado para procesar.");
            return;
        }

        boolean exito = controladorCU.tomarAccion(accion);

        if (exito) {
            mostrarAlerta("El estado del evento sísmico ha sido modificado con éxito.");
            refrescarVista();
        }
    }

    private void refrescarVista() {
        txtEventoSeleccionado.clear();
        txtAlcance.clear();
        txtClasificacion.clear();
        txtOrigen.clear();
        imagenSismograma.setImage(null);
        imagenSismograma.setVisible(false); // Asegurar que se oculte

        tablaEventos.setDisable(false);
        tablaEventos.getSelectionModel().clearSelection();
        this.eventoSeleccionado = null;

        // Recargar la lista de eventos pendientes llamando al gestor,
        // quien consultará la base de datos.
        controladorCU.registrarResultadoDeRevMan(this.sesionActiva);
    }

    /**
     * Muestra una alerta con el estilo de easysmos.
     * Se ajusta el título y se carga el CSS para estilizar el DialogPane.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("easysmos - Revisión Manual");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // --- INYECCIÓN DE ESTILO CSS para la alerta ---
        try {
            DialogPane dialogPane = alert.getDialogPane();
            // Carga el CSS desde resources/css/easysmos.css
            dialogPane.getStylesheets().add(getClass().getResource("/css/easysmos.css").toExternalForm());
            dialogPane.getStyleClass().add("card-panel"); // Usar el estilo de card para la alerta
        } catch (Exception e) {
            System.err.println("Error al cargar el CSS para la alerta: " + e.getMessage());
        }

        alert.showAndWait();
    }

    @FXML
    private void tomarOptMapaSismico() {
        System.out.println("Se seleccionó la opción de ver el mapa sísmico.");
    }

    @FXML
    private void tomarOptModificarDatos() {
        System.out.println("Se seleccionó la opción de modificar datos.");
    }
}