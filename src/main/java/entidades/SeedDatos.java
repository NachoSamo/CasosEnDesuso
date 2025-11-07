package entidades;

import persistence.repositorios.*;
import persistence.repositorios.EmpleadoRepositoryPostgreSql;
import persistence.repositorios.EventoSismicoRepositoryPostgreSql;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeedDatos {

    public static void inicializarDatosSiNecesario() {
        EmpleadoRepository empleadoRepo = new EmpleadoRepositoryPostgreSql();
        EventoSismicoRepository eventoRepo = new EventoSismicoRepositoryPostgreSql();

        // Verificamos si la base de datos ya tiene datos usando un empleado conocido.
        Optional<Empleado> empleadoExistente = empleadoRepo.findByUsername("jperez");

        if (empleadoExistente.isPresent()) {
            System.out.println("La base de datos ya contiene datos. Omitiendo inicializacion.");
            return;
        }

        System.out.println("Base de datos vacia. Inicializando datos...");

        // --- 1. Crear y guardar Empleados y Usuarios ---
        Empleado emp1 = new Empleado();
        emp1.setNombre("Juan");
        emp1.setApellido("Perez");
        emp1.setMail("jperez@email.com");
        emp1.setTelefono("123456789");
        emp1.setUsuario(new Usuario("jperez", "admin"));

        Empleado emp2 = new Empleado();
        emp2.setNombre("Lucia");
        emp2.setApellido("Gomez");
        emp2.setMail("lgomez@email.com");
        emp2.setTelefono("987654321");
        emp2.setUsuario(new Usuario("lgomez", "admin"));

        // Guardamos los empleados PRIMERO para que obtengan un ID de la base de datos.
        empleadoRepo.save(emp1);
        empleadoRepo.save(emp2);

        System.out.println("Empleados iniciales guardados.");

        // --- 2. Crear Eventos Sismicos usando los empleados ya guardados ---
        List<EventoSismico> eventos = new ArrayList<>();

        EventoSismico ev1 = new EventoSismico(
                LocalDateTime.of(2025, 5, 27, 14, 56), "32°54'S", "68°54'O", "12°54'S", "68°14'O", 3.5,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Tectonico", "Por movimiento de placas")
        );
        eventos.add(ev1);

        EventoSismico ev2 = new EventoSismico(
                LocalDateTime.of(2025, 3, 27, 17, 16), "32°54'S", "68°54'O", "42°14'S", "68°52'O", 3.9,
                new ClasificacionSismo("Moderado", 71, 300), new AlcanceSismo("Mediano", "Region amplia"),
                new OrigenDeGeneracion("Volcanico", "Actividad magmatica interna")
        );
        eventos.add(ev2);

        EventoSismico ev3 = new EventoSismico(
                LocalDateTime.of(2024, 8, 7, 23, 56), "32°54'S", "68°54'O", "22°34'S", "68°41'O", 1.4,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Inducido", "Intervencion humana")
        );
        ev3.revisar(LocalDateTime.now().minusDays(1), emp1); // Usamos el empleado persistido
        eventos.add(ev3);

        EventoSismico ev4 = new EventoSismico(
                LocalDateTime.of(2023, 1, 10, 10, 0), "35°10'S", "70°00'O", "25°10'S", "70°00'O", 5.1,
                new ClasificacionSismo("Fuerte", 301, 700), new AlcanceSismo("Regional", "Afecta multiples regiones"),
                new OrigenDeGeneracion("Tectonico", "Por movimiento de placas")
        );
        ev4.revisar(LocalDateTime.now().minusHours(5), emp2); // Usamos el otro empleado
        ev4.confirmar(LocalDateTime.now().minusHours(4), emp2);
        eventos.add(ev4);

        eventoRepo.saveAll(eventos);
        System.out.println("Eventos sismicos iniciales guardados.");
        System.out.println("Inicializacion de datos completada.");
    }
}