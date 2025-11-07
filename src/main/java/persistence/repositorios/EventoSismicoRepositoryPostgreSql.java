package persistence.repositorios;

import entidades.EventoSismico;
import entidades.estadosConcretos.AutoDetectado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import persistence.PersistenceManager;

import java.util.List;
import java.util.Optional;

public class EventoSismicoRepositoryPostgreSql implements EventoSismicoRepository {

    @Override
    public void save(EventoSismico evento) {
        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(evento);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Optional<EventoSismico> findById(int id) {
        EntityManager em = PersistenceManager.getEntityManager();
        try {
            EventoSismico evento = em.find(EventoSismico.class, id);
            return Optional.ofNullable(evento);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<EventoSismico> findAllAutoDetectados() {
        EntityManager em = PersistenceManager.getEntityManager();
        try {
            // Obtenemos el nombre de la clase que representa el estado que buscamos.
            String estadoClassName = AutoDetectado.class.getName();

            // Usamos una consulta JPQL (Java Persistence Query Language) para buscar por el nombre de la clase.
            String jpql = "SELECT e FROM EventoSismico e WHERE e.estadoActualClassName = :className ORDER BY e.fechaHoraOcurrencia ASC";

            TypedQuery<EventoSismico> query = em.createQuery(jpql, EventoSismico.class);
            query.setParameter("className", estadoClassName);

            // Hibernate ejecutará automáticamente el método @PostLoad en cada entidad para rehidratar el objeto 'estado'.
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void saveAll(List<EventoSismico> eventos) {
        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            for (EventoSismico evento : eventos) {
                em.merge(evento);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}