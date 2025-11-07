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
    }

    @FXML
    private void iniciarCU() {
        // 1. Mostrar las vistas correctas
        vistaInicial.setVisible(false);
        vistaInicial.setManaged(false);
        vistaCU.setVisible(true);
        vistaCU.setManaged(true);
        seccionBottom.setVisible(true);
        seccionBottom.setManaged(true);

        // --- CAMBIO CLAVE: CONFIGURACIÓN DE COLUMNAS CON LAMBDAS ---
        // Este es el método moderno y a prueba de fallos.

        colFechaHora.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaHoraOcurrenciaTexto())
        );
        colEpicentro.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCoordEpicentro())
        );
        colHipocentro.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCoordHipocentro())
        );
        colMagnitud.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getValorMagnitud()).asObject()
        );

        // 3. Simular la sesión activa
        Usuario usuarioLogueado = new Usuario("lgomez", null);
        this.sesionActiva = new Sesion(LocalDateTime.now(), null, usuarioLogueado);

        // 4. Actualizar la UI con la información de la sesión
        if (txtusername != null) {
            txtusername.setText(usuarioLogueado.getUsername());
        }

        // 5. Pedir al gestor que cargue los datos iniciales desde la BD
        controladorCU.registrarResultadoDeRevMan(this.sesionActiva);
    }

    // --- VERIFICACIÓN ADICIONAL ---
    // Añade este print para estar 100% seguro de que los datos llegan.
    public void mostrarES(List<EventoSismico> eventos) {
        System.out.println("Numero de eventos recibidos para mostrar en la tabla: " + eventos.size());
        if (!eventos.isEmpty()) {
            System.out.println("Primer evento: " + eventos.get(0).getFechaHoraOcurrenciaTexto());
        }
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
            imagenSismograma.setImage(null);
            imagenSismograma.setVisible(false);
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
        imagenSismograma.setVisible(false);

        tablaEventos.setDisable(false);
        tablaEventos.getSelectionModel().clearSelection();
        this.eventoSeleccionado = null;

        // Recargar la lista de eventos pendientes llamando al gestor.
        controladorCU.registrarResultadoDeRevMan(this.sesionActiva);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("easysmos - Revisión Manual");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        try {
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/easysmos.css").toExternalForm());
            // dialogPanegetStyleClass().add("card-panel");
        } catch (Exception e) {
            // Ignorar si el CSS no se encuentra, no es crítico.
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