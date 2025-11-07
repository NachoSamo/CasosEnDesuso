package gestor;

import boundary.PantallaRegistrarResultadoDeRevManual;
import imagenSismograma.GenerarSismograma; // Asegúrate de que este import sea correcto
import entidades.Empleado;
import entidades.EventoSismico;
import entidades.Sesion;
import entidades.Usuario;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import persistence.repositorios.EmpleadoRepository;
import persistence.repositorios.EmpleadoRepositoryPostgreSql;
import persistence.repositorios.EventoSismicoRepository;
import persistence.repositorios.EventoSismicoRepositoryPostgreSql;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GestorRevManual {

    private Empleado empleadoResponsable;
    private EventoSismico eventoSeleccionado;

    // Se reemplazan las listas en memoria por los repositorios
    private final EventoSismicoRepository eventoRepository;
    private final EmpleadoRepository empleadoRepository;

    private final GenerarSismograma controladorSismograma = new GenerarSismograma();
    private PantallaRegistrarResultadoDeRevManual boundaryRef;

    public void setBoundary(PantallaRegistrarResultadoDeRevManual boundary) {
        this.boundaryRef = boundary;
    }

    public GestorRevManual() {
        // Se inicializan los repositorios en lugar de cargar datos a memoria
        this.eventoRepository = new EventoSismicoRepositoryPostgreSql();
        this.empleadoRepository = new EmpleadoRepositoryPostgreSql();
    }

    public void registrarResultadoDeRevMan(Sesion sesionActiva) {
        this.empleadoResponsable = buscarEmpleado(sesionActiva);

        // La búsqueda y el ordenamiento ahora se delegan a la base de datos
        List<EventoSismico> eventos = buscarESAutodetectados();

        if (boundaryRef != null) {
            boundaryRef.mostrarES(eventos);
        }
    }

    public Empleado buscarEmpleado(Sesion sesionActiva) {
        if (sesionActiva == null || sesionActiva.getUsuario() == null) {
            return null;
        }
        String username = sesionActiva.getUsuario().getUsername();

        // Se busca directamente en la base de datos por username
        Optional<Empleado> empleadoOpt = empleadoRepository.findByUsername(username);
        return empleadoOpt.orElse(null);
    }

    public List<EventoSismico> buscarESAutodetectados() {
        // Se consulta directamente a la base de datos
        return eventoRepository.findAllAutoDetectados();
    }

    public void tomarSeleccionES(EventoSismico evento) {
        this.eventoSeleccionado = evento;
        LocalDateTime fechaActual = getFechaHoraActual();

        // La lógica de negocio no cambia: modifica el objeto en memoria
        evento.revisar(fechaActual, this.empleadoResponsable);

        // Persistimos el cambio de estado a "EnRevision" inmediatamente
        this.eventoRepository.save(this.eventoSeleccionado);

        System.out.println("Evento ID: " + evento.getId() + " ha sido puesto en estado 'EnRevision'.");

        buscarDetallesES();
    }

    public LocalDateTime getFechaHoraActual() {
        return LocalDateTime.now();
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

    public boolean tomarAccion(String accion) {
        if (eventoSeleccionado == null || accion == null) return false;
        LocalDateTime fechaActual = this.getFechaHoraActual();

        try {
            // La lógica de negocio modifica el estado del objeto en memoria
            switch (accion.toLowerCase()) {
                case "confirmar" -> eventoSeleccionado.confirmar(fechaActual, empleadoResponsable);
                case "rechazar" -> eventoSeleccionado.rechazar(fechaActual, empleadoResponsable);
                case "derivar" -> eventoSeleccionado.derivar(fechaActual, empleadoResponsable);
                default -> {
                    System.out.println("Accion no reconocida: " + accion);
                    return false;
                }
            }

            // --- PASO CLAVE: PERSISTIR EL CAMBIO ---
            // Se guardan los cambios del objeto modificado en la base de datos.
            this.eventoRepository.save(this.eventoSeleccionado);

            String estadoNombre = eventoSeleccionado.getEstado().getNombre();
            System.out.println("Evento ID: " + eventoSeleccionado.getId() + " ha sido cambiado al estado: " + estadoNombre + ". Persistido en la BD.");

            return true;
        } catch (UnsupportedOperationException | IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Operacion Invalida");
            alert.setHeaderText("La accion no se puede realizar.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return false;
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