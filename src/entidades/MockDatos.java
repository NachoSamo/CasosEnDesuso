package entidades;

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
                empleados.get(1).getUsuario() // ✅ Sesión activa para Lucía
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
        EventoSismico ev1 = new EventoSismico(LocalDateTime.of(2025, 5, 27, 14, 56), "32°54'S", "68°54'O", "12°54'S", "68°14'O", 3.5,
                new ClasificacionSismo("Leve", 0, 70),
                new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Tectónico", "Por movimiento de placas"));
        ev1.setEstado(new Estado("AutoDetectado", "Evento"));

        EventoSismico ev2 = new EventoSismico(LocalDateTime.of(2025, 3, 27, 17, 16), "32°54'S", "68°54'O", "42°14'S", "68°52'O", 3.9,
                new ClasificacionSismo("Moderado", 71, 300),
                new AlcanceSismo("Mediano", "Región amplia"),
                new OrigenDeGeneracion("Volcánico", "Actividad magmática interna"));
        ev2.setEstado(new Estado("PendienteDeRevision", "Evento"));

        EventoSismico ev3 = new EventoSismico(LocalDateTime.of(2024, 8, 7, 23, 56), "32°54'S", "68°54'O", "22°34'S", "68°41'O", 1.4,
                new ClasificacionSismo("Leve", 0, 70),
                new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Inducido", "Intervención humana (fracking, presas)"));
        ev3.setEstado(new Estado("Confirmado", "Revisión manual"));

        System.out.println("📦 Mock de eventos creado:");
        for (EventoSismico ev : List.of(ev1, ev2, ev3)) {
            System.out.println(" - " + ev.getFechaHoraOcurrencia() + " | Estado: " + ev.getEstado().getNombre());
        }

        return FXCollections.observableArrayList(ev1, ev2, ev3);
    }




    public static List<Estado> obtenerEstadosMock() {
        return FXCollections.observableArrayList(
                new Estado("AutoDetectado", "Evento"),
                new Estado("PendienteDeRevisión", "Evento"),
                new Estado("SinRevisión", "Evento"),
                new Estado("EnRevisión", "Evento"),
                new Estado("Rechazado", "Evento"),
                new Estado("Derivado", "Evento"),
                new Estado("Confirmado", "Evento"),
                new Estado("PendienteDeCierre", "Evento"),
                new Estado("Cerrado", "Cerrado"),
                new Estado("AutoConfirmado", "Evento")
        );
    }

}

