package controlador;

import casosDeUso.GenerarSismograma;
import entidades.*;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDateTime;
import java.util.*;

public class ControladorRegistrarResultadoDeRevManual {

    private Empleado empleadoResponsable;
    private List<Empleado> empleadosSistema;
    private List<EventoSismico> eventosSimulados;
    private EventoSismico eventoSeleccionado;
    private final GenerarSismograma controladorSismograma = new GenerarSismograma();

    public ControladorRegistrarResultadoDeRevManual() {
        empleadosSistema = MockDatos.obtenerEmpleadosMock();
        eventosSimulados = MockDatos.obtenerEventosMock();
    }

    // ✅ Paso inicial del caso de uso
    public List<EventoSismico> registrarResultadoDeRevMan() {
        this.empleadoResponsable = buscarEmpleado();
        List<EventoSismico> eventos = buscarESSinRevisar(); // Buscar sin ordenar
        return ordenarESPorFechaHoraOcurrencia(eventos); // Devuelvo lista de eventos
    }

    public Empleado buscarEmpleado() {
        Usuario usuarioActual = empleadosSistema.get(0).getUsuario(); // Simulación de usuario actual
        Sesion sesionActual = new Sesion(LocalDateTime.now(), null, usuarioActual);
        Usuario usuarioSesion = sesionActual.getUsuario();
        for (Empleado emp : empleadosSistema) {
            if (emp.esTuUsuario(usuarioSesion)) {
                return emp;
            }
        }
        return null;
    }

    public Empleado getEmpleadoResponsable() {
        return this.empleadoResponsable;
    }

    public List<EventoSismico> buscarESSinRevisar() {
        List<EventoSismico> sinRevisar = eventosSimulados.stream()
                .filter(EventoSismico::soySinRevisar)
                .toList();

        sinRevisar.forEach(ev -> ev.getDatos()); // Forzar ejecución del getDatos
        return sinRevisar; // Solo devolver la lista ordenada
    }


    public List<EventoSismico> ordenarESPorFechaHoraOcurrencia(List<EventoSismico> eventos) {
        return eventos.stream()
                .sorted(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia))
                .toList();
    }

    // ✅ Tomar evento seleccionado desde la pantalla
    public void tomarSeleccionES(EventoSismico evento) {
        this.eventoSeleccionado = evento;
    }

    // ✅ Buscar estado "En Revisión" en la lista de estados
    public Estado buscarEstadoEnRevision(EventoSismico ev) {
        for (CambioEstado ce : ev.getCambiosEstado()) {
            Estado estado = ce.getEstado();
            if (estado.esAmbitoEvento() && estado.esEnRevision()) {
                return estado;
            }
        }
        return null;
    }

    // ✅ Buscar estado "Rechazado"
    public Estado buscarRechazado(List<Estado> estados) {
        for (Estado estado : estados) {
            if (estado.esAmbitoEvento() && estado.esRechazado()) {
                return estado;
            }
        }
        return null;
    }

    // ✅ Ejecuta revisión del evento
    public void revisar(EventoSismico ev, Estado nuevoEstado) {
        ev.revisar(nuevoEstado, LocalDateTime.now());
    }

    // ✅ Obtener estructura ACO (alcance, clasificación, origen)
    public Map<String, String> buscarDetallesES() {
        if (eventoSeleccionado == null) return Collections.emptyMap();
        return eventoSeleccionado.getACO();
    }

    // ✅ Generar imagen sismograma con detalle de estaciones
    public Image generarSismograma(EventoSismico evento) {
        if (evento == null) return null;

        Map<SerieTemporal, List<String>> datosMuestras = evento.getDatosST();
        Map<SerieTemporal, EstacionSismologica> estaciones = evento.getSeriesPorEstacion();

        System.out.println("📊 Detalles de muestras por estación:");
        for (SerieTemporal serie : datosMuestras.keySet()) {
            EstacionSismologica estacion = estaciones.get(serie);
            System.out.println("🛰 Estación: " + (estacion != null ? estacion.getNombre() : "Desconocida"));
            for (String detalle : datosMuestras.get(serie)) {
                System.out.println("   - " + detalle);
            }
        }

        return controladorSismograma.ejecutar(evento);
    }

    // ✅ Acciones de transición de estado
    public void confirmar(EventoSismico ev) {
        System.out.println("✔ Confirmar evento");
    }

    public void rechazar(EventoSismico ev) {
        System.out.println("✖ Rechazar evento");
    }

    public void derivar(EventoSismico ev) {
        System.out.println("➡ Derivar evento");
    }

    public void cancelar(EventoSismico ev) {
        ev.cancelar(LocalDateTime.now(), empleadoResponsable);
        System.out.println("↩ Evento restaurado a estado AutoDetectado.");
    }

    public void finCU(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finalización de Caso de Uso");
        alert.setHeaderText("Revisión Finalizada");
        alert.setContentText(mensaje);
        alert.showAndWait();

        Stage stage = (Stage) Stage.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst()
                .orElse(null);
        if (stage != null) stage.close();
    }

}
