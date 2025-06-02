package controlador;

import casosDeUso.GenerarSismograma;
import entidades.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.beans.property.SimpleStringProperty;

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

    public void buscarESAutodetectado(
            TableView<EventoSismico> tabla,
            TableColumn<EventoSismico, String> colFechaHora,
            TableColumn<EventoSismico, String> colEpicentro,
            TableColumn<EventoSismico, String> colHipocentro,
            TableColumn<EventoSismico, Double> colMagnitud
    ) {
        // ⚠️ Asegurarse de que los nombres usados acá coincidan con los getters públicos de EventoSismico.
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrencia")); // getFechaHoraOcurrencia()
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("latitudEpicentro"));     // getLatitudEpicentro()
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("latitudHipocentro"));   // getLatitudHipocentro()
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));         // getValorMagnitud()

        // ✅ Filtrar los eventos cuyo estado sea AutoDetectado o PendienteDeRevision
        List<EventoSismico> sinRevisar = eventosSimulados.stream()
                .filter(EventoSismico::soySinRevisar) // ← Verifica que el estado no sea null y que sea "sin revisar"
                .toList();

        // ✅ Verificación por consola: revisa si realmente se detectaron eventos
        if (sinRevisar.isEmpty()) {
            System.out.println("⚠ No se encontraron eventos sin revisar.");
        } else {
            System.out.println("✅ Se encontraron " + sinRevisar.size() + " eventos sin revisar.");
        }

        // ✅ Mostrar los datos de cada evento filtrado
        sinRevisar.forEach(ev -> {
            System.out.println("📌 Evento:");
            ev.getDatos().forEach((clave, valor) -> System.out.println("  • " + clave + ": " + valor));
        });

        // ✅ Ordenar por fecha y cargar en tabla
        List<EventoSismico> ordenados = ordenarESPorFechaHoraOcurrencia(sinRevisar);
        tabla.setItems(FXCollections.observableArrayList(ordenados));
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

    public void modificarEvento(EventoSismico ev) {
        System.out.println("Evento listo para modificación: " + ev);
    }

    public Image generarSismograma(EventoSismico evento) {
        if (evento == null) {
            System.out.println("⚠ No se proporcionó evento para generar sismograma.");
            return null;
        }

        System.out.println("📡 Iniciando generación de sismograma para evento: " + evento.getFechaHoraOcurrencia());

        Map<SerieTemporal, List<String>> datosMuestras = evento.getDatosST();
        Map<SerieTemporal, EstacionSismologica> estaciones = evento.getSeriesPorEstacion();

        // Log por consola (clasificación por estación)
        System.out.println("📊 Detalles de muestras por estación:");
        for (SerieTemporal serie : datosMuestras.keySet()) {
            EstacionSismologica estacion = estaciones.get(serie);
            System.out.println("🛰 Estación: " + (estacion != null ? estacion.getNombre() : "Desconocida"));
            List<String> muestras = datosMuestras.get(serie);
            for (String detalle : muestras) {
                System.out.println("   - " + detalle);
            }
        }

        System.out.println("🎯 Enviando datos al caso de uso GenerarSismograma...");
        return controladorSismograma.ejecutar(evento); // Este método debe devolver una Image válida.
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


    public List<EventoSismico> ordenarESPorFechaHoraOcurrencia(List<EventoSismico> eventos) {
        // ⚠️ Asegúrate que getFechaHoraOcurrencia() no retorne null en ningún evento.
        return eventos.stream()
                .sorted(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia))
                .toList();
    }



    public Empleado getEmpleadoResponsable() {
        return this.empleadoResponsable;
    }

    public void setEventoSeleccionado(EventoSismico ev) {
        this.eventoSeleccionado = ev;
    }


    private void tomarOptMapaSismico() {
        // Por ahora, solo imprimir para confirmar que funciona
        System.out.println("🗺 Se seleccionó la opción de ver el mapa sísmico.");
    }

    private void tomarOptModificarDatos() {
        System.out.println("📝 Se seleccionó la opción de modificar datos.");
    }

    public void tomarAccion(String accion, EventoSismico evento) {
        if (accion == null || evento == null) return;

        switch (accion.toLowerCase()) {
            case "confirmar" -> confirmar(evento);
            case "rechazar" -> rechazar(evento);
            case "derivar" -> derivar(evento);
            default -> System.out.println("⚠ Acción no reconocida: " + accion);
        }
    }

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

    public List<Object> buscarDetallesES() {
        return null;
    }

    public void cancelar(EventoSismico ev) {
        ev.cancelar(LocalDateTime.now(), empleadoResponsable);
        System.out.println("↩ Evento restaurado al estado: Auto-detectado");
    }
    public void finCU(String mensaje) {
        // Mostrar alerta de finalización
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finalización de Caso de Uso");
        alert.setHeaderText("Revisión Finalizada");
        alert.setContentText(mensaje);
        alert.showAndWait();

        // Cerrar la ventana actual
        // Esto busca la ventana activa (típicamente la que tiene el foco)
        Stage stage = (Stage) Stage.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst()
                .orElse(null);

        if (stage != null) {
            stage.close();
        }
    }


}
