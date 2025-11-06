package persistence.repository;

import persistence.EntityManagerProvider;
import persistence.entities.EventoBloqueoRevisionEntity;
import persistence.entities.EmpleadoEntity;
import persistence.entities.EventoSismicoEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;

public class EventoBloqueoRevisionRepository {

    public void lockEvent(String eventoId, String analistaUsuario, LocalDateTime tomadoEn) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            EventoBloqueoRevisionEntity b = em.find(EventoBloqueoRevisionEntity.class, eventoId);
            if (b == null) b = new EventoBloqueoRevisionEntity();
            b.setEventoId(eventoId);
            // find analista
            if (analistaUsuario != null) {
                TypedQuery<EmpleadoEntity> q = em.createQuery("SELECT e FROM EmpleadoEntity e WHERE e.usuarioId = :u", EmpleadoEntity.class);
                q.setParameter("u", analistaUsuario);
                java.util.List<EmpleadoEntity> r = q.getResultList();
                if (!r.isEmpty()) b.setAnalista(r.get(0));
            }
            b.setTomadoEn(tomadoEn != null ? tomadoEn : LocalDateTime.now());
            em.merge(b);
            em.getTransaction().commit();
        } finally {
            EntityManager e = em;
            if (e.getTransaction().isActive()) e.getTransaction().rollback();
            em.close();
        }
    }

    public void unlockEvent(String eventoId, LocalDateTime liberadoEn) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            EventoBloqueoRevisionEntity b = em.find(EventoBloqueoRevisionEntity.class, eventoId);
            if (b != null) {
                b.setLiberadoEn(liberadoEn != null ? liberadoEn : LocalDateTime.now());
                em.merge(b);
            }
            em.getTransaction().commit();
        } finally {
            EntityManager e = em;
            if (e.getTransaction().isActive()) e.getTransaction().rollback();
            em.close();
        }
    }
}
