package gestor;

import boundary.PantallaRegistrarResultadoDeRevManual;
import imagenSismograma.GenerarSismograma;
import entidades.*;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GestorRevManual {

    private Empleado empleadoResponsable;
    private List<Empleado> empleadosSistema;
    private List<EventoSismico> eventosSimulados;
    private EventoSismico eventoSeleccionado;
    private final GenerarSismograma controladorSismograma = new GenerarSismograma();
    private PantallaRegistrarResultadoDeRevManual boundaryRef;

    public void setBoundary(PantallaRegistrarResultadoDeRevManual boundary) {
        this.boundaryRef = boundary;
    }

    public GestorRevManual() {
        empleadosSistema = MockDatos.obtenerEmpleadosMock();
        eventosSimulados = MockDatos.obtenerEventosMock();
    }

    public List<EventoSismico> registrarResultadoDeRevMan(Sesion sesionActiva) {
        this.empleadoResponsable = buscarEmpleado(sesionActiva);
        List<EventoSismico> eventos = buscarESAutodetectados();
        List<EventoSismico> ordenados = ordenarESPorFechaHoraOcurrencia(eventos);

        if (boundaryRef != null) {
            boundaryRef.mostrarES(ordenados);
        }
        return ordenados;
    }

    public Empleado buscarEmpleado(Sesion sesionActiva) {
        if (sesionActiva == null) return null;
        Usuario usuarioSesion = sesionActiva.getUsuario();
        for (Empleado emp : empleadosSistema) {
            if (emp.esTuUsuario(usuarioSesion)) {
                return emp;
            }
        }
        return null;
    }

    public List<EventoSismico> buscarESAutodetectados() {
        return eventosSimulados.stream()
                .filter(EventoSismico::soyAutoDetectado)
                .collect(Collectors.toList());
    }

    public List<EventoSismico> ordenarESPorFechaHoraOcurrencia(List<EventoSismico> eventos) {
        return eventos.stream()
                .sorted(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia))
                .collect(Collectors.toList());
    }

    public void tomarSeleccionES(EventoSismico evento) {
        this.eventoSeleccionado = evento;
        LocalDateTime fechaActual = getFechaHoraActual();
        revisar(evento, fechaActual);
        buscarDetallesES();
    }

    public LocalDateTime getFechaHoraActual() {
        return LocalDateTime.now();
    }

    public void revisar(EventoSismico ev, LocalDateTime fechaActual) {
        if (ev != null) {
            ev.revisar(fechaActual, this.empleadoResponsable);
        }
    }

    public Map<String, String> buscarDetallesES() {
        if (eventoSeleccionado == null) return Collections.emptyMap();
        Map<String, String> aco = eventoSeleccionado.getACO();
        return boundaryRef.mostrarDetalleES(aco);
    }

    public Image generarSismograma(EventoSismico evento) {
        if (evento == null) return null;
        return controladorSismograma.ejecutar(evento);
    }

    // --- METODO CLAVE MODIFICADO ---
    public boolean tomarAccion(String accion) {
        if (eventoSeleccionado == null || accion == null) return false;
        LocalDateTime fechaActual = this.getFechaHoraActual();

        try {
            switch (accion.toLowerCase()) {
                case "confirmar" -> {
                    eventoSeleccionado.confirmar(fechaActual, empleadoResponsable);
                    String estadoNombre = eventoSeleccionado.getEstado() != null ? eventoSeleccionado.getEstado().getNombre() : "UNKNOWN";
                    String msg = "Estado del evento sismico: " + eventoSeleccionado.toString() + " ha sido cambiado a " + estadoNombre;
                    System.out.println(msg);
                }
                case "rechazar" -> {
                    eventoSeleccionado.rechazar(fechaActual, empleadoResponsable);
                    String estadoNombre = eventoSeleccionado.getEstado() != null ? eventoSeleccionado.getEstado().getNombre() : "UNKNOWN";
                    String msg = "Estado del evento sismico: " + eventoSeleccionado.toString() + " ha sido cambiado a " + estadoNombre;
                    System.out.println(msg);
                }
                case "derivar" -> {
                    eventoSeleccionado.derivar(fechaActual, empleadoResponsable);
                    String estadoNombre = eventoSeleccionado.getEstado() != null ? eventoSeleccionado.getEstado().getNombre() : "UNKNOWN";
                    String msg = "Estado del evento sismico: " + eventoSeleccionado.toString() + " ha sido cambiado a " + estadoNombre;
                    System.out.println(msg);
                }
                default -> {
                    System.out.println("Accion no reconocida: " + accion);
                    return false; // Indica que la acción no se encontró
                }
            }
            return true; // Indica éxito
        } catch (UnsupportedOperationException | IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Operacion Invalida");
            alert.setHeaderText("La accion no se puede realizar.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return false; // Indica fracaso
        }
    }

    public void finCU(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finalizacion de Caso de Uso");
        alert.setHeaderText("Revision Finalizada");
        alert.setContentText(mensaje);
        alert.showAndWait();

        Stage stage = (Stage) Stage.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst()
                .orElse(null);
        if (stage != null) stage.close();
    }
}