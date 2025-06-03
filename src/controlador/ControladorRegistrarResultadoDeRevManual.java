package controlador;

import boundary.PantallaRegistrarResultadoDeRevManual;
import casosDeUso.GenerarSismograma;
import entidades.*;
import javafx.scene.control.*;
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
    private PantallaRegistrarResultadoDeRevManual boundaryRef;
    private List<Estado> estadosDisponibles;

    public void setBoundary(PantallaRegistrarResultadoDeRevManual boundary) {
        this.boundaryRef = boundary;
    }


    public ControladorRegistrarResultadoDeRevManual() {
        estadosDisponibles = MockDatos.obtenerEstadosMock();
        empleadosSistema = MockDatos.obtenerEmpleadosMock();
        eventosSimulados = MockDatos.obtenerEventosMock();
    }




    // ✅ Paso inicial del caso de uso
    public List<EventoSismico> registrarResultadoDeRevMan(Sesion sesionActiva) {
        this.empleadoResponsable = buscarEmpleado(sesionActiva);
        List<EventoSismico> eventos = buscarESSinRevisar(); // Buscar sin ordenar
        List<EventoSismico> ordenados = ordenarESPorFechaHoraOcurrencia(eventos); // Devuelvo lista de eventos
        //        return pantallaRegistrarResultadoDeRevManual.mostrarESeventosOrdenados();

        if (boundaryRef != null) {
            boundaryRef.mostrarES(ordenados);
            boundaryRef.solicitarSeleccionES();
        }
        return ordenados;
    }

    public Empleado buscarEmpleado(Sesion sesionActiva) {
        if (sesionActiva == null) {
            System.out.println("⚠ Sesión activa no recibida.");
            return null;
        }

        Usuario usuarioSesion = sesionActiva.getUsuario();
        System.out.println("🔍 Buscando empleado para usuario: " + usuarioSesion.getUsername());

        for (Empleado emp : empleadosSistema) {
            System.out.println("🔁 Comparando con: " + emp.getUsuario().getUsername());
            if (emp.esTuUsuario(usuarioSesion)) {
                System.out.println("✅ Empleado encontrado: " + emp.getNombre());
                return emp;
            }
        }

        System.out.println("❌ Empleado no encontrado.");
        return null;
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
        Estado estadoEnRevision = buscarESEnRevisión();
        LocalDateTime fechaActual = getFechaHoraActual();
        revisar(evento, estadoEnRevision, fechaActual);
        Map<String, String> detalles = buscarDetallesES();
    }

    public Estado buscarESEnRevisión() {
        for (Estado estado : estadosDisponibles) {
            if (estado != null
                    && estado.esEnRevision()
                    && estado.esAmbitoEvento()) {
                return estado;
            }
        }
        return null; // Si no lo encuentra
    }


    public LocalDateTime getFechaHoraActual() {
        return LocalDateTime.now();
    }

    public void revisar(EventoSismico ev, Estado estadoEnRevision, LocalDateTime fechaActual) {
        if (estadoEnRevision != null && ev != null) {
            ev.revisar(estadoEnRevision, fechaActual);
        }
    }


    // ✅ Obtener estructura ACO (alcance, clasificación, origen)
    public Map<String, String> buscarDetallesES() {
        if (eventoSeleccionado == null) return Collections.emptyMap();
        Map<String, String> aco = eventoSeleccionado.getACO();
        Map<SerieTemporal, List<String>> datosMuestrasSismicas = eventoSeleccionado.getDatosMuestrasSismicas();
        ordenarPorEstacionSismologica()
        geneararSismograma()
    }



    // ✅ Generar imagen sismograma con detalle de estaciones
    public Image generarSismograma(EventoSismico evento) {
        if (evento == null) return null;

        Map<SerieTemporal, List<String>> datosMuestras = evento.getDatosMuestrasSismicas();
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

    // ✅ Buscar estado "Rechazado"
    public Estado buscarRechazado(List<Estado> estados) {
        for (Estado estado : estados) {
            if (estado.esAmbitoEvento() && estado.esRechazado()) {
                return estado;
            }
        }
        return null;
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
