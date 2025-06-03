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


    public Map<String, String> buscarDetallesES() {
        if (eventoSeleccionado == null) return Collections.emptyMap();
        Map<String, String> aco = eventoSeleccionado.getACO();
        Map<SerieTemporal, List<String>> datosMuestrasSismicas = eventoSeleccionado.getDatosMuestrasSismicas();
//        ordenarPorEstacionSismologica()
//        geneararSismograma()
        return boundaryRef.mostrarDetalleES(aco);
    }



    // ✅ Combinar serie ↔ estación ↔ detalles
    public Map<String, List<String>> ordenarPorEstacionSismologica(
            Map<SerieTemporal, List<String>> datosSeries,
            Map<SerieTemporal, EstacionSismologica> mapaSeriesEstacion) {

        Map<String, List<String>> datosPorEstacion = new TreeMap<>();

        for (Map.Entry<SerieTemporal, List<String>> entry : datosSeries.entrySet()) {
            SerieTemporal serie = entry.getKey();
            List<String> muestras = entry.getValue();
            EstacionSismologica estacion = mapaSeriesEstacion.get(serie);

            if (estacion != null) {
                String nombreEstacion = estacion.getNombre();
                datosPorEstacion.computeIfAbsent(nombreEstacion, k -> new ArrayList<>()).addAll(muestras);
            }
        }

        return datosPorEstacion;
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






    public void tomarAccion(String accion) {
        EventoSismico evento = this.eventoSeleccionado;
        if (evento == null || accion == null) return;

        evento.validarExistencias(this.eventoSeleccionado);

        LocalDateTime fechaActual = this.getFechaHoraActual();

        switch (accion.toLowerCase()) {
            case "confirmar" -> {
                Estado estadoConfirmado = buscarConfirmado(estadosDisponibles);
                confirmar(estadoConfirmado, fechaActual);
            }
            case "rechazar" -> {
                Estado estadoRechazado = buscarRechazado(estadosDisponibles);
                rechazar(estadoRechazado, fechaActual);
                
            }
            case "derivar" -> {
                Estado estadoDerivado = buscarDerivado(estadosDisponibles);
                derivar(estadoDerivado, fechaActual);
            }
            default -> System.out.println("⚠ Acción no reconocida: " + accion);
        }
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


    public Estado buscarConfirmado(List<Estado> estados) {
        for (Estado estado : estados) {
            if ("Confirmado".equals(estado.getNombre()) && "Evento".equals(estado.getAmbito())) {
                return estado;
            }
        }
        return new Estado("Confirmado", "Evento"); // Fallback
    }

    public Estado buscarDerivado(List<Estado> estados) {
        for (Estado estado : estados) {
            if ("Derivado".equals(estado.getNombre()) && "Evento".equals(estado.getAmbito())) {
                return estado;
            }
        }
        return new Estado("Derivado", "Evento"); // Fallback
    }



    public void confirmar(Estado estadoConfirmado, LocalDateTime fechaActual) {
        EventoSismico ev = this.eventoSeleccionado;
        Empleado empleado = empleadoResponsable;
        ev.confirmar(estadoConfirmado, fechaActual);
        System.out.println("✔ Evento confirmado");
    }

    public void rechazar(Estado estadoRechazado, LocalDateTime fechaActual) {
        EventoSismico ev = this.eventoSeleccionado;
        Empleado empleado = empleadoResponsable;
        this.eventoSeleccionado.rechazar(estadoRechazado, fechaActual, empleado);
        System.out.println("✖ Evento rechazado");
    }






    public void derivar(Estado estadoDerivado, LocalDateTime fechaActual) {
        EventoSismico ev = this.eventoSeleccionado;
        Empleado empleado = empleadoResponsable;
        ev.derivar(estadoDerivado, fechaActual);
        System.out.println("➡ Evento derivado a experto");
    }





    public void cancelar(EventoSismico ev) {
        if (ev == null) return;

        // Obtener el último cambio de estado anterior
        List<CambioEstado> historial = ev.getCambiosEstado();
        if (historial.size() < 2) {
            System.out.println("⚠ No hay estado anterior para revertir.");
            return;
        }

        CambioEstado cambioAnterior = historial.get(historial.size() - 2);
        Estado estadoAnterior = cambioAnterior.getEstado();

        // Cerrar el cambio actual
        CambioEstado cambioActual = historial.get(historial.size() - 1);
        cambioActual.setFechaHoraFin(LocalDateTime.now());

        // Crear nuevo cambio con el estado anterior
        ev.revisar(estadoAnterior, LocalDateTime.now());
        System.out.println("↩ Evento restaurado a estado anterior: " + estadoAnterior.getNombre());
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
