package controlador;

import casosDeUso.GenerarSismograma;
import entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class ControladorRegistrarRevision {

    private Empleado empleadoResponsable;
    private List<Empleado> empleadosSistema;
    private List<EventoSismico> eventosSimulados;

    public ControladorRegistrarRevision() {
        empleadosSistema = MockDatos.obtenerEmpleadosMock();
        eventosSimulados = MockDatos.obtenerEventosMock();
    }

    public void cargarEventos(
            TableView<EventoSismico> tabla,
            TableColumn<EventoSismico, String> colFechaHora,
            TableColumn<EventoSismico, String> colEpicentro,
            TableColumn<EventoSismico, String> colHipocentro,
            TableColumn<EventoSismico, Double> colMagnitud
    ) {
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrencia"));
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("latitudEpicentro"));
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("latitudHipocentro"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));

        tabla.setItems(FXCollections.observableArrayList(eventosSimulados));
    }

    public void confirmar(EventoSismico ev) {
        System.out.println("Evento confirmado: " + ev);
    }

    public void rechazar(EventoSismico ev) {
        System.out.println("Evento rechazado: " + ev);
    }

    public void derivar(EventoSismico ev) {
        System.out.println("Evento derivado a experto: " + ev);
    }

    public void generarSismograma(EventoSismico ev) {
        // Método reservado para lógica futura, si se desea separar del llamado actual
    }

    public void modificarEvento(EventoSismico ev) {
        System.out.println("Evento listo para modificación: " + ev);
    }

    public void registrarResultadoDeRevMan() {
        this.empleadoResponsable = buscarEmpleado();
    }

    public Empleado buscarEmpleado() {
        Sesion sesionActual = new Sesion(LocalDateTime.now(), null, new Usuario("jperez", "admin"));
        Usuario usuarioSesion = sesionActual.getUsuario();
        for (Empleado emp : empleadosSistema) {
            if (emp.esTuUsuario(usuarioSesion)) {
                return emp;
            }
        }
        return null;
    }

    public List<EventoSismico> buscarESAutodetectado() {
        return eventosSimulados.stream()
                .filter(ev -> ev.getEstado() != null && ev.getEstado().getNombre().equals("Detectado"))
                .sorted(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia))
                .toList();
    }

    public Empleado getEmpleadoResponsable() {
        return this.empleadoResponsable;
    }

    public Image obtenerImagenSismograma(EventoSismico ev) {
        GenerarSismograma casoDeUso = new GenerarSismograma();
        return casoDeUso.ejecutar(ev);
    }

    // ==== NUEVOS MÉTODOS para interacción del diagrama de secuencia ====

    public void habOptMapa() {
        // En futuro: habilitar botón de mapa o mostrar mapa
    }

    public void habOptModificarDatos() {
        // En futuro: mostrar panel editable o habilitar campos
    }

    public void habComboAcciones() {
        // En futuro: permitir uso de comboBox
    }

    public void tomarAccion(String accion, EventoSismico evento) {
        if (accion == null || evento == null) return;

        switch (accion.toLowerCase()) {
            case "confirmar":
                confirmar(evento);
                break;
            case "rechazar":
                rechazar(evento);
                break;
            case "derivar":
                derivar(evento);
                break;
            default:
                System.out.println("⚠ Acción no reconocida: " + accion);
        }
    }
}
