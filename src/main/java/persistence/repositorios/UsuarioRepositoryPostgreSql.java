package persistence.repositorios;

import entidades.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import persistence.PersistenceManager;
import java.util.Optional;

public class UsuarioRepositoryPostgreSql implements UsuarioRepository {

    @Override
    public void save(Usuario usuario) {
        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(usuario);
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
    public Optional<Usuario> findByUsername(String username) {
        EntityManager em = PersistenceManager.getEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, username);
            return Optional.ofNullable(usuario);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}