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
//        Map<String, String> detalles = buscarDetallesES();
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
//    public Map<String, String> buscarDetallesES() {
//        if (eventoSeleccionado == null) return Collections.emptyMap();
//        Map<String, String> aco = eventoSeleccionado.getACO();
//        System.out.println("✅ ACO del evento:");
//        aco.forEach((k, v) -> System.out.println(" • " + k + ": " + v));
//        Map<SerieTemporal, List<String>> datosMuestrasSismicasXSerie = eventoSeleccionado.getDatosMuestrasSismicas();
//
//
//
//        // Acumular seriesXEstacion desde todos los sismógrafos
//        Map<SerieTemporal, EstacionSismologica> seriesXEstacion = new HashMap<>();
//        for (Sismografo sis : eventoSeleccionado.getSismografos()) {
//            seriesXEstacion.putAll(sis.getSeriesPorEstacion());
//        }
//
//        Map<String, List<String>> datosAgrupados = ordenarPorEstacionSismologica(datosMuestrasSismicasXSerie, seriesXEstacion);
//
//        System.out.println("📊 Muestras agrupadas por estación:");
//        datosAgrupados.forEach((estacion, muestras) -> {
//            System.out.println("🛰 Estación: " + estacion);
//            muestras.forEach(m -> System.out.println("   - " + m));
//        });
//
//
//        // 5. Mostrar en consola para debug
//        System.out.println("📊 Muestras agrupadas por estación:");
//        ordenarPorEstacionSismologica.forEach((estacion, muestras) -> {
//            System.out.println("🛰 Estación: " + estacion);
//            muestras.forEach(detalle -> System.out.println("   - " + detalle));
//        });
//        Image sismograma = generarSismograma(eventoSeleccionado);
//        if (sismograma != null) {
//            System.out.println("🖼 Imagen del sismograma generada correctamente.");
//        } else {
//            System.out.println("⚠ No se pudo generar el sismograma.");
//        }
//    }

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



    // ✅ Buscar estado "Rechazado"
    public Estado buscarRechazado(List<Estado> estados) {
        for (Estado estado : estados) {
            if (estado.esAmbitoEvento() && estado.esRechazado()) {
                return estado;
            }
        }
        return null;
    }


    public void confirmar(EventoSismico ev) {
        Estado estadoConfirmado = estadosDisponibles.stream()
                .filter(e -> e.getNombre().equals("Confirmado") && e.getAmbito().equals("Revisión manual"))
                .findFirst()
                .orElse(new Estado("Confirmado", "Revisión manual"));

        cerrarCambioActual(ev);
        ev.revisar(estadoConfirmado, LocalDateTime.now());
        ev.setResponsableRevision(empleadoResponsable);
        System.out.println("✔ Evento confirmado");
    }

    public void rechazar(EventoSismico ev) {
        Estado estadoRechazado = buscarRechazado(estadosDisponibles);
        if (estadoRechazado == null) {
            estadoRechazado = new Estado("Rechazado", "Revisión manual");
        }

        cerrarCambioActual(ev);
        ev.revisar(estadoRechazado, LocalDateTime.now());
        ev.setResponsableRevision(empleadoResponsable);
        System.out.println("✖ Evento rechazado");
    }

    public void derivar(EventoSismico ev) {
        Estado estadoDerivado = estadosDisponibles.stream()
                .filter(e -> e.getNombre().equals("Derivado") && e.getAmbito().equals("Revisión manual"))
                .findFirst()
                .orElse(new Estado("Derivado", "Revisión manual"));

        cerrarCambioActual(ev);
        ev.revisar(estadoDerivado, LocalDateTime.now());
        ev.setResponsableRevision(empleadoResponsable);
        System.out.println("➡ Evento derivado a experto");
    }

    private void cerrarCambioActual(EventoSismico ev) {
        if (ev == null) return;

        for (CambioEstado ce : ev.getCambiosEstado()) {
            if (ce.sosActual()) {
                ce.setFechaHoraFin(LocalDateTime.now());
                ce.setResponsable(empleadoResponsable);
                break;
            }
        }
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
