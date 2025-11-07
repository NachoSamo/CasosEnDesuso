package entidades;

import persistence.repositorios.EmpleadoRepository;
import persistence.repositorios.EmpleadoRepositoryPostgreSql;
import persistence.repositorios.EventoSismicoRepository;
import persistence.repositorios.EventoSismicoRepositoryPostgreSql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entidades.estadosConcretos.AutoDetectado;

public class SeedDatos {

    public static void inicializarDatosSiNecesario() {
        // Renombramos los repositorios para evitar confusiones
        EmpleadoRepository empleadoRepository = new EmpleadoRepositoryPostgreSql();
        EventoSismicoRepository eventoSismicoRepository = new EventoSismicoRepositoryPostgreSql();

        Optional<Empleado> empleadoExistente = empleadoRepository.findByUsername("jperez");

        if (empleadoExistente.isPresent()) {
            System.out.println("La base de datos ya contiene datos. Omitiendo inicializacion.");
            return;
        }

        System.out.println("Base de datos vacia. Inicializando datos...");

        // --- 1. Crear objetos Empleado en memoria ---
        Empleado emp1ParaGuardar = new Empleado();
        emp1ParaGuardar.setNombre("Juan");
        emp1ParaGuardar.setApellido("Perez");
        emp1ParaGuardar.setMail("jperez@email.com");
        emp1ParaGuardar.setTelefono("123456789");
        emp1ParaGuardar.setUsuario(new Usuario("jperez", "admin"));

        Empleado emp2ParaGuardar = new Empleado();
        emp2ParaGuardar.setNombre("Lucia");
        emp2ParaGuardar.setApellido("Gomez");
        emp2ParaGuardar.setMail("lgomez@email.com");
        emp2ParaGuardar.setTelefono("987654321");
        emp2ParaGuardar.setUsuario(new Usuario("lgomez", "admin"));

        // --- 2. Guardar los empleados ---
        empleadoRepository.save(emp1ParaGuardar);
        empleadoRepository.save(emp2ParaGuardar);

        System.out.println("Empleados iniciales guardados.");

        // --- 3. RECUPERAR los empleados de la base de datos ---
        // Esto nos asegura que tenemos las versiones "persistidas" con sus IDs.
        Empleado emp1Persistido = empleadoRepository.findByUsername("jperez").orElseThrow();
        Empleado emp2Persistido = empleadoRepository.findByUsername("lgomez").orElseThrow();

        // --- 4. Crear Eventos Sismicos usando las entidades persistidas ---
        List<EventoSismico> eventos = new ArrayList<>();

        EventoSismico ev1 = new EventoSismico(
                LocalDateTime.of(2025, 5, 27, 14, 56), "32°54'S", "68°54'O", "12°54'S", "68°14'O", 3.5,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Tectonico", "Por movimiento de placas")
        );
        ev1.setEstado(new AutoDetectado());
        eventos.add(ev1);

        EventoSismico ev2 = new EventoSismico(
                LocalDateTime.of(2025, 3, 27, 17, 16), "32°54'S", "68°54'O", "42°14'S", "68°52'O", 3.9,
                new ClasificacionSismo("Moderado", 71, 300), new AlcanceSismo("Mediano", "Region amplia"),
                new OrigenDeGeneracion("Volcanico", "Actividad magmatica interna")
        );
        ev2.setEstado(new AutoDetectado());
        eventos.add(ev2);

        EventoSismico ev3 = new EventoSismico(
                LocalDateTime.of(2024, 8, 7, 23, 56), "32°54'S", "68°54'O", "22°34'S", "68°41'O", 1.4,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Inducido", "Intervencion humana")
        );
        // Usamos la versión del empleado que SÍ tiene un ID.
        ev3.revisar(LocalDateTime.now().minusDays(1), emp1Persistido);
        ev3.setEstado(new AutoDetectado());
        eventos.add(ev3);

        EventoSismico ev4 = new EventoSismico(
                LocalDateTime.of(2023, 1, 10, 10, 0), "35°10'S", "70°00'O", "25°10'S", "70°00'O", 5.1,
                new ClasificacionSismo("Fuerte", 301, 700), new AlcanceSismo("Regional", "Afecta multiples regiones"),
                new OrigenDeGeneracion("Tectonico", "Por movimiento de placas")
        );
        // Usamos la versión del empleado que SÍ tiene un ID.
        ev4.revisar(LocalDateTime.now().minusHours(5), emp2Persistido);
        ev4.confirmar(LocalDateTime.now().minusHours(4), emp2Persistido);
        ev4.setEstado(new AutoDetectado());
        eventos.add(ev4);

        // --- 5. Añadir 10 eventos auto-detectados (no llamar revisar/confirmar para que queden en ese estado) ---
        EventoSismico ev5 = new EventoSismico(
                LocalDateTime.of(2025, 6, 1, 1, 0), "31°24'S", "64°11'O", "31°20'S", "64°10'O", 2.1,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Tectonico", "Movimiento local")
        );
        ev5.setEstado(new AutoDetectado());
        eventos.add(ev5);

        EventoSismico ev6 = new EventoSismico(
                LocalDateTime.of(2025, 6, 2, 2, 30), "31°30'S", "64°15'O", "31°26'S", "64°12'O", 2.4,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Tectonico", "Movimiento local")
        );
        ev6.setEstado(new AutoDetectado());
        eventos.add(ev6);

        EventoSismico ev7 = new EventoSismico(
                LocalDateTime.of(2025, 6, 3, 3, 45), "31°40'S", "64°20'O", "31°35'S", "64°18'O", 2.8,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Inducido", "Actividad humana")
        );
        ev7.setEstado(new AutoDetectado());
        eventos.add(ev7);

        EventoSismico ev8 = new EventoSismico(
                LocalDateTime.of(2025, 6, 4, 4, 10), "31°50'S", "64°25'O", "31°45'S", "64°22'O", 3.0,
                new ClasificacionSismo("Moderado", 71, 300), new AlcanceSismo("Mediano", "Region amplia"),
                new OrigenDeGeneracion("Volcanico", "Actividad magmatica")
        );
        ev8.setEstado(new AutoDetectado());
        eventos.add(ev8);

        EventoSismico ev9 = new EventoSismico(
                LocalDateTime.of(2025, 6, 5, 5, 5), "32°00'S", "64°30'O", "31°55'S", "64°28'O", 3.3,
                new ClasificacionSismo("Moderado", 71, 300), new AlcanceSismo("Mediano", "Region amplia"),
                new OrigenDeGeneracion("Tectonico", "Movimiento regional")
        );
        ev9.setEstado(new AutoDetectado());
        eventos.add(ev9);

        EventoSismico ev10 = new EventoSismico(
                LocalDateTime.of(2025, 6, 6, 6, 6), "32°10'S", "64°35'O", "32°05'S", "64°33'O", 3.6,
                new ClasificacionSismo("Moderado", 71, 300), new AlcanceSismo("Mediano", "Region amplia"),
                new OrigenDeGeneracion("Tectonico", "Movimiento regional")
        );
        ev10.setEstado(new AutoDetectado());
        eventos.add(ev10);

        EventoSismico ev11 = new EventoSismico(
                LocalDateTime.of(2025, 6, 7, 7, 7), "32°20'S", "64°40'O", "32°15'S", "64°38'O", 3.9,
                new ClasificacionSismo("Moderado", 71, 300), new AlcanceSismo("Mediano", "Region amplia"),
                new OrigenDeGeneracion("Volcanico", "Actividad magmatica")
        );
        ev11.setEstado(new AutoDetectado());
        eventos.add(ev11);

        EventoSismico ev12 = new EventoSismico(
                LocalDateTime.of(2025, 6, 8, 8, 8), "32°30'S", "64°45'O", "32°25'S", "64°43'O", 4.1,
                new ClasificacionSismo("Fuerte", 301, 700), new AlcanceSismo("Regional", "Afecta multiples regiones"),
                new OrigenDeGeneracion("Tectonico", "Falla mayor")
        );
        ev12.setEstado(new AutoDetectado());
        eventos.add(ev12);

        EventoSismico ev13 = new EventoSismico(
                LocalDateTime.of(2025, 6, 9, 9, 9), "32°40'S", "64°50'O", "32°35'S", "64°48'O", 2.2,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Inducido", "Actividad humana")
        );
        ev13.setEstado(new AutoDetectado());
        eventos.add(ev13);

        EventoSismico ev14 = new EventoSismico(
                LocalDateTime.of(2025, 6, 10, 10, 10), "32°50'S", "64°55'O", "32°45'S", "64°53'O", 2.5,
                new ClasificacionSismo("Leve", 0, 70), new AlcanceSismo("Local", "Zona limitada"),
                new OrigenDeGeneracion("Tectonico", "Movimiento local")
        );
        ev14.setEstado(new AutoDetectado());
        eventos.add(ev14);

        // Ahora, cuando guardemos los eventos, todos sus "responsables" ya existen en la BD.
        eventoSismicoRepository.saveAll(eventos);
        System.out.println("Eventos sismicos iniciales guardados.");
        System.out.println("Inicializacion de datos completada.");
    }
}