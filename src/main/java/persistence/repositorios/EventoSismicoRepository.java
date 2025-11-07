package persistence.repositorios;

import entidades.EventoSismico;

import java.util.List;
import java.util.Optional;


public interface EventoSismicoRepository {

    void save(EventoSismico evento);

    Optional<EventoSismico> findById(int id);

    List<EventoSismico> findAllAutoDetectados();

    void saveAll(List<EventoSismico> eventos);
}