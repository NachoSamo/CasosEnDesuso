package entidades;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

public class MockDatos {

    public static List<Empleado> obtenerEmpleadosMock() {
        return FXCollections.observableArrayList(
                new Empleado("Juan", "Pérez", "jperez@email.com", "123456789", new Usuario("jperez", "admin")),
                new Empleado("Lucía", "Gómez", "lgomez@email.com", "987654321", new Usuario("lgomez", "admin"))
        );
    }

    public static List<EventoSismico> obtenerEventosMock() {
        return FXCollections.observableArrayList(
                new EventoSismico(LocalDateTime.of(2025, 5, 27, 14, 56), "32°54'S", "68°54'O", "12°54'S", "68°14'O", 3.5,
                        new ClasificacionSismo("Leve", 0, 70),
                        new AlcanceSismo("Local", "Zona limitada"),
                        new OrigenDeGeneracion("Tectónico", "Por movimiento de placas")),

                new EventoSismico(LocalDateTime.of(2025, 3, 27, 17, 16), "32°54'S", "68°54'O", "42°14'S", "68°52'O", 3.9,
                        new ClasificacionSismo("Moderado", 71, 300),
                        new AlcanceSismo("Mediano", "Región amplia"),
                        new OrigenDeGeneracion("Volcánico", "Actividad magmática interna")),

                new EventoSismico(LocalDateTime.of(2024, 8, 7, 23, 56), "32°54'S", "68°54'O", "22°34'S", "68°41'O", 1.4,
                        new ClasificacionSismo("Leve", 0, 70),
                        new AlcanceSismo("Local", "Zona limitada"),
                        new OrigenDeGeneracion("Inducido", "Intervención humana (fracking, presas)"))
        );
    }
}
