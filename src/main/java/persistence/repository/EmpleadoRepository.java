package persistence.repository;

import entidades.Empleado;
import entidades.Usuario;
import persistence.EntityManagerProvider;
import persistence.entities.EmpleadoEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository {

    public List<Empleado> findAll() {
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            TypedQuery<EmpleadoEntity> q = em.createQuery("SELECT e FROM EmpleadoEntity e", EmpleadoEntity.class);
            List<EmpleadoEntity> ents = q.getResultList();
            List<Empleado> result = new ArrayList<>();
            for (EmpleadoEntity ent : ents) {
                Empleado emp = new Empleado();
                // We only have usuarioId in the entity; map it to Usuario.username
                if (ent.getUsuarioId() != null) {
                    emp.setUsuario(new Usuario(ent.getUsuarioId(), null));
                }
                emp.setNombre(null);
                emp.setApellido(null);
                emp.setMail(null);
                emp.setTelefono(null);
                result.add(emp);
            }
            return result;
        } finally {
            em.close();
        }
    }

    public Empleado findByUsuarioUsername(String username) {
        if (username == null) return null;
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            TypedQuery<EmpleadoEntity> q = em.createQuery("SELECT e FROM EmpleadoEntity e WHERE e.usuarioId = :u", EmpleadoEntity.class);
            q.setParameter("u", username);
            List<EmpleadoEntity> res = q.getResultList();
            if (res.isEmpty()) return null;
            EmpleadoEntity ent = res.get(0);
            Empleado emp = new Empleado();
            emp.setUsuario(new Usuario(ent.getUsuarioId(), null));
            return emp;
        } finally {
            em.close();
        }
    }
}
