package persistence.repositorios;

import entidades.Empleado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import persistence.PersistenceManager;
import java.util.Optional;

public class EmpleadoRepositoryPostgreSql implements EmpleadoRepository {

    @Override
    public void save(Empleado empleado) {
        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(empleado);
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
    public Optional<Empleado> findByUsername(String username) {
        EntityManager em = PersistenceManager.getEntityManager();
        try {
            String jpql = "SELECT e FROM Empleado e WHERE e.usuario.username = :username";
            TypedQuery<Empleado> query = em.createQuery(jpql, Empleado.class);
            query.setParameter("username", username);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty(); // Es normal no encontrar resultados, no es un error.
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}