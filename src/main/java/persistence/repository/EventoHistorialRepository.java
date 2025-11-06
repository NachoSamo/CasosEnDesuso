package persistence.repository;

import entidades.CambioEstadoES;
import persistence.EntityManagerProvider;
import persistence.entities.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;

public class EventoHistorialRepository {

    public void saveCambio(String eventoId, CambioEstadoES cambio) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            EventoHistorialEstadoEntity hist = new EventoHistorialEstadoEntity();
            // set evento reference
            EventoSismicoEntity eventoRef = em.getReference(EventoSismicoEntity.class, eventoId);
            hist.setEvento(eventoRef);
            // find estado by codigo
            String codigo = cambio.getEstado() != null ? cambio.getEstado().getNombre() : null;
            EstadoEventoEntity estadoEnt = null;
            if (codigo != null) {
                TypedQuery<EstadoEventoEntity> q = em.createQuery("SELECT e FROM EstadoEventoEntity e WHERE e.codigo = :c", EstadoEventoEntity.class);
                q.setParameter("c", codigo);
                java.util.List<EstadoEventoEntity> res = q.getResultList();
                if (!res.isEmpty()) estadoEnt = res.get(0);
            }
            if (estadoEnt == null) {
                // fallback: try to find a default state (pendiente_revision)
                TypedQuery<EstadoEventoEntity> q2 = em.createQuery("SELECT e FROM EstadoEventoEntity e WHERE e.codigo = :c", EstadoEventoEntity.class);
                q2.setParameter("c", "pendiente_revision");
                java.util.List<EstadoEventoEntity> r2 = q2.getResultList();
                if (!r2.isEmpty()) estadoEnt = r2.get(0);
            }
            hist.setEstado(estadoEnt);

            // usuario/responsable
            if (cambio.getResponsable() != null && cambio.getResponsable().getUsuario() != null) {
                String usuarioId = cambio.getResponsable().getUsuario().getUsername();
                TypedQuery<EmpleadoEntity> q3 = em.createQuery("SELECT e FROM EmpleadoEntity e WHERE e.usuarioId = :u", EmpleadoEntity.class);
                q3.setParameter("u", usuarioId);
                java.util.List<EmpleadoEntity> r3 = q3.getResultList();
                if (!r3.isEmpty()) hist.setUsuario(r3.get(0));
            }

            LocalDateTime fecha = cambio.getFechaHoraInicio() != null ? cambio.getFechaHoraInicio() : LocalDateTime.now();
            hist.setFechaHora(fecha);
            hist.setMotivo(null);

            em.persist(hist);
            em.getTransaction().commit();
        } finally {
            EntityManager e = em;
            if (e.getTransaction().isActive()) e.getTransaction().rollback();
            em.close();
        }
    }
}
