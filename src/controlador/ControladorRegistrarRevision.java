package controlador;

import entidades.Empleado;
import entidades.EventoSismico;
import entidades.Sesion;
import entidades.Usuario;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ControladorRegistrarRevision {

    private Empleado empleadoResponsable;
    private List<Empleado> empleadosSistema; // lista simulada que se debe inicializar

    public ControladorRegistrarRevision() {
        // Se puede poblar con empleados simulados acá
        empleadosSistema = FXCollections.observableArrayList(
                new Empleado("Juan", "Pérez", "jperez@email.com", "123456789", new Usuario("jperez", "admin")),
                new Empleado("Lucía", "Gómez", "lgomez@email.com", "987654321", new Usuario("lgomez", "admin"))
        );
    }

    public void cargarEventos(
            TableView<EventoSismico> tabla,
            TableColumn<EventoSismico, String> colFechaHora,
            TableColumn<EventoSismico, String> colEpicentro,
            TableColumn<EventoSismico, String> colHipocentro,
            TableColumn<EventoSismico, Double> colMagnitud,
            TableColumn<EventoSismico, Boolean> colSeleccionar
    ) {
        colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrencia"));
        colEpicentro.setCellValueFactory(new PropertyValueFactory<>("latitudEpicentro"));
        colHipocentro.setCellValueFactory(new PropertyValueFactory<>("latitudHipocentro"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("valorMagnitud"));
        colSeleccionar.setCellValueFactory(new PropertyValueFactory<>("seleccionado"));

        ObservableList<EventoSismico> datos = FXCollections.observableArrayList();
        tabla.setItems(datos);
    }

    public void confirmar(EventoSismico ev) {}
    public void rechazar(EventoSismico ev) {}
    public void derivar(EventoSismico ev) {}
    public void generarSismograma(EventoSismico ev) {}
    public void modificarEvento(EventoSismico ev) {}

    public void registrarResultadoDeRevMan() {
        this.empleadoResponsable = buscarEmpleado();
    }

    private Empleado buscarEmpleado() {
        Usuario usuarioSesion = obtenerUsuarioSesion();
        for (Empleado emp : empleadosSistema) {
            if (emp.esTuUsuario(usuarioSesion.getUsername())) {
                return emp;
            }
        }
        return null;
    }

    private Usuario obtenerUsuarioSesion() {
        Sesion sesionActual = new Sesion(null, null, new Usuario("jperez", "admin"));
        return sesionActual.getUsuario();
    }
}
