package controlador;

import entidades.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class ControladorRegistrarRevision {

    private Empleado empleadoResponsable;
    private List<Empleado> empleadosSistema;
    private List<EventoSismico> eventosSimulados;
    private EventoSismico eventoSeleccionado;
    private GenerarSismograma controladorSismograma;

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


    public void confirmar(EventoSismico ev) {}
    public void rechazar(EventoSismico ev) {}
    public void derivar(EventoSismico ev) {}
    public void generarSismograma(EventoSismico ev) {}
    public void modificarEvento(EventoSismico ev) {}

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

    public Map<String, List<String>> ordenarPorEstacionSismologica(
        Map<SerieTemporal, List<String>> datosSeries,
        Map<SerieTemporal, EstacionSismologica> mapaSeriesEstacion) {

        // Se crea un TreeMap para mantener los datos ordenados alfabéticamente por nombre de estación
        Map<String, List<String>> datosPorEstacion = new TreeMap<>();

        // Recorremos cada entrada del mapa de series temporales con sus muestras
        for (Map.Entry<SerieTemporal, List<String>> entry : datosSeries.entrySet()) {
            SerieTemporal serie = entry.getKey();         // Obtenemos la serie temporal
            List<String> muestras = entry.getValue();     // Obtenemos la lista de muestras (como String)

        // Buscamos en el segundo mapa a qué estación sismológica pertenece esta serie
        EstacionSismologica estacion = mapaSeriesEstacion.get(serie);

        if (estacion != null) {
            // Obtenemos el nombre de la estación como clave de agrupamiento
            String nombreEstacion = estacion.getNombre(); // Suponiendo que existe el método getNombre()

            // Si la estación aún no tiene una entrada en el mapa, se crea la lista vacía.
            // Luego se agregan las muestras de esta serie a la lista correspondiente.
            datosPorEstacion.computeIfAbsent(nombreEstacion, k -> new ArrayList<>()).addAll(muestras);
        }
    }

    // Se retorna el mapa final, con los datos clasificados y ordenados por nombre de estación
    return datosPorEstacion;
    }

    public List<Object> buscarDetallesES() {
        res = eventoSeleccionado.getACO();
        mapaSerieXMuestra = eventoSeleccionado.getDatosST();
        mapaSeriesXEstacion = eventoSeleccionado.getSeriesPorEstacion();
        mapaFinal = ordenarPorEstacionSismologica(mapaSerieXMuestra, mapaSeriesXEstacion);
        sismograma = controladorSismograma.generarSismograma(mapaSerieXMuestra, mapaSeriesXEstacion);

        List<Object> resultado = new ArrayList<>();
        resultado.add(res);     // posición 0 → mapa
        resultado.add(sismograma); // posición 1 → imagen
        return resultado;
    }
}
