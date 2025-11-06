package entidades;

import entidades.estadosConcretos.AutoDetectado;
import entidades.estadosConcretos.Confirmado;
import entidades.estadosConcretos.EnRevision;
import javafx.collections.FXCollections;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockDatos {

    public static List<Empleado> obtenerEmpleadosMock() {
        return FXCollections.observableArrayList(
                new Empleado("Juan", "Pérez", "jperez@email.com", "123456789", new Usuario("jperez", "admin")),
                new Empleado("Lucía", "Gómez", "lgomez@email.com", "987654321", new Usuario("lgomez", "admin"))
        );
    }

    public static List<Sesion> obtenerSesionesMock() {
        List<Empleado> empleados = obtenerEmpleadosMock();
        List<Sesion> sesiones = new ArrayList<>();
        sesiones.add(new Sesion(
                LocalDateTime.of(2024, 6, 1, 8, 0),
                LocalDateTime.of(2024, 6, 1, 16, 0),
                empleados.get(0).getUsuario()
        ));
        sesiones.add(new Sesion(
                LocalDateTime.of(2024, 6, 2, 9, 0),
                null,
                empleados.get(1).getUsuario()
        ));
        return sesiones;
    }

    public static Sesion obtenerSesionActiva(List<Sesion> sesiones) {
        return sesiones.stream()
                .filter(s -> s.getFechaHoraFin() == null)
                .findFirst()
                .orElse(null);
    }

    public static List<EventoSismico> obtenerEventosMock() {
        // Obtenemos un empleado para simular las acciones
        Empleado empleadoMock = obtenerEmpleadosMock().get(0);

        // Evento 1: Se crea y por defecto estará en estado "AutoDetectado".
        // Este evento SÍ aparecerá en la lista inicial del caso de uso.
        EventoSismico ev1 = new EventoSismico(
                LocalDateTime.of(2025, 5, 27, 14, 56),
                "32°54'S", "68°54'O", "12°54'S", "68°14'O", 3.5,
                new ClasificacionSismo("Leve", 0, 70),
                new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Tectónico", "Por movimiento de placas")
        );

        // Evento 2: También se crea en "AutoDetectado".
        // Este evento SÍ aparecerá en la lista inicial.
        EventoSismico ev2 = new EventoSismico(
                LocalDateTime.of(2025, 3, 27, 17, 16),
                "32°54'S", "68°54'O", "42°14'S", "68°52'O", 3.9,
                new ClasificacionSismo("Moderado", 71, 300),
                new AlcanceSismo("Mediano", "Región amplia"),
                new OrigenDeGeneracion("Volcánico", "Actividad magmática interna")
        );

        // Evento 3: Se crea en "AutoDetectado" y luego se simula una revisión.
        // Este evento NO aparecerá en la lista inicial porque su estado ya es "EnRevision".
        EventoSismico ev3 = new EventoSismico(
                LocalDateTime.of(2024, 8, 7, 23, 56),
                "32°54'S", "68°54'O", "22°34'S", "68°41'O", 1.4,
                new ClasificacionSismo("Leve", 0, 70),
                new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Inducido", "Intervención humana (fracking, presas)")
        );
        // Simulamos la acción de revisar
        ev3.revisar(LocalDateTime.now().minusDays(1), empleadoMock);


        // Evento 4: Simulamos un flujo completo hasta "Confirmado".
        // Este evento NO aparecerá en la lista inicial.
        EventoSismico ev4 = new EventoSismico(
                LocalDateTime.of(2023, 1, 10, 10, 0),
                "35°10'S", "70°00'O", "25°10'S", "70°00'O", 5.1,
                new ClasificacionSismo("Fuerte", 301, 700),
                new AlcanceSismo("Regional", "Afecta múltiples regiones"),
                new OrigenDeGeneracion("Tectónico", "Por movimiento de placas")
        );
        // Simulamos las acciones para llevarlo a "Confirmado"
        ev4.revisar(LocalDateTime.now().minusHours(5), empleadoMock);
        ev4.confirmar(LocalDateTime.now().minusHours(4), empleadoMock);


        List<EventoSismico> todosLosEventos = new ArrayList<>(List.of(ev1, ev2, ev3, ev4));

        System.out.println("📦 Mock de eventos creado con el patrón State:");
        for (EventoSismico ev : todosLosEventos) {
            System.out.println(" - " + ev.getFechaHoraOcurrencia() + " | Estado Actual: " + ev.getEstado().getNombre());
        }

        return FXCollections.observableArrayList(todosLosEventos);
    }

    // El método obtenerEstadosMock() ya no es necesario y se ha eliminado.
    // La lógica de estados ahora está encapsulada en las clases concretas de estado.
}