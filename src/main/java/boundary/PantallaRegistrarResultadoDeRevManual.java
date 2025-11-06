package boundary;

import gestor.GestorRevManual;
import entidades.EventoSismico;
import entidades.MockDatos;
import entidades.Sesion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;

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

    public void setSesion(Sesion sesion) {
        this.sesionActiva = sesion;
    }

    // Este método es llamado automáticamente por JavaFX después de cargar el FXML.
    // Lo usamos para configuraciones iniciales que no dependen de la acción del usuario.
    @FXML
    public void initialize() {
        controladorCU.setBoundary(this);
    }

    // Este método es llamado por el botón "Registrar Resultado".
    // Ahora contiene toda la lógica de preparación de la pantalla.
    @FXML
    private void iniciarCU() {
        vistaInicial.setVisible(false);
        vistaInicial.setManaged(false);

        vistaCU.setVisible(true);
        vistaCU.setManaged(true);
        seccionBottom.setVisible(true);
        seccionBottom.setManaged(true);

        // Configurar la tabla una sola vez
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrenciaTexto"));
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("coordEpicentro"));
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("coordHipocentro"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));

        // Obtener sesión y cargar datos iniciales
        List<Sesion> sesiones = MockDatos.obtenerSesionesMock();
        this.sesionActiva = Sesion.obtenerSesionActiva(sesiones);

        if (txtusername != null && controladorCU.buscarEmpleado(this.sesionActiva) != null) {
            txtusername.setText(controladorCU.buscarEmpleado(this.sesionActiva).getUsuario().getUsername());
        }

        // Llamada UNICA al gestor para poblar la tabla
        controladorCU.registrarResultadoDeRevMan(this.sesionActiva);
    }

    public void mostrarES(List<EventoSismico> eventos) {
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));
    }

    @FXML
    private void tomarSeleccionES() {
        eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado == null) {
            // No mostrar alerta si solo se está haciendo clic en la tabla,
            // solo cuando se intenta realizar una acción sin selección.
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
        }
    }

    @FXML private void confirmarEvento() { tomarAccion("confirmar"); }
    @FXML private void rechazarEvento() { tomarAccion("rechazar"); }
    @FXML private void derivarEvento() { tomarAccion("derivar"); }

    @FXML
    private void cancelarAccion() {
        refrescarVista();
        mostrarAlerta("Operacion cancelada. Puede seleccionar otro evento.");
    }

    private void tomarAccion(String accion) {
        if (eventoSeleccionado == null) {
            mostrarAlerta("No hay ningun evento seleccionado para procesar.");
            return;
        }

        boolean exito = controladorCU.tomarAccion(accion);

        if (exito) {
            mostrarAlerta("El estado del evento sismico ha sido modificado con éxito.");
            refrescarVista();
        }
    }

    private void refrescarVista() {
        txtEventoSeleccionado.clear();
        txtAlcance.clear();
        txtClasificacion.clear();
        txtOrigen.clear();
        imagenSismograma.setImage(null);

        tablaEventos.setDisable(false);
        tablaEventos.getSelectionModel().clearSelection();
        this.eventoSeleccionado = null;

        // Recargar la lista de eventos pendientes llamando al gestor
        controladorCU.registrarResultadoDeRevMan(this.sesionActiva);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Revision Manual");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // --- METODOS FALTANTES REINCORPORADOS ---

    @FXML
    private void tomarOptMapaSismico() {
        System.out.println("Se selecciono la opcion de ver el mapa sismico.");
    }

    @FXML
    private void tomarOptModificarDatos() {
        System.out.println("Se selecciono la opcion de modificar datos.");
    }
}