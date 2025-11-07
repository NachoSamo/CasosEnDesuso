package persistence.repositorios;

import entidades.EventoSismico;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EventoSismicoRepositoryH2 implements EventoSismicoRepository {

    private final Map<Integer, EventoSismico> store = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public void save(EventoSismico evento) {
        if (evento == null) {
            System.out.println("Evento nulo, no se guarda.");
            return;
        }
        int id = idGenerator.getAndIncrement();
        store.put(id, evento);
        System.out.println("Estado del evento sismico: " + evento + " ha sido cambiado a *ejemplo* (id=" + id + ")");
    }

    @Override
    public Optional<EventoSismico> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<EventoSismico> findAllAutoDetectados() {
        // Ejemplo simple: devuelve todos los guardados como "auto-detectados" de ejemplo.
        return new ArrayList<>(store.values());
    }

    @Override
    public void saveAll(List<EventoSismico> eventos) {
        if (eventos == null) return;
        for (EventoSismico e : eventos) {
            save(e);
        }
    }
}