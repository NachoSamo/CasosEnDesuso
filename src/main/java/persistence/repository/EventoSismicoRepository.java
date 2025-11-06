package persistence.repository;

import entidades.*;
import persistence.EntityManagerProvider;
import persistence.EstadoFactory;
import persistence.entities.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

public class EventoSismicoRepository {

    public List<EventoSismico> findPendientes() {
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            TypedQuery<EventoSismicoEntity> q = em.createQuery(
                    "SELECT e FROM EventoSismicoEntity e WHERE e.estadoActual = :estado ORDER BY e.fechaOcurrencia DESC",
                    EventoSismicoEntity.class);
            q.setParameter("estado", "pendiente_revision");
            List<EventoSismicoEntity> entities = q.getResultList();
            return entities.stream().map(this::toDomain).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    public Optional<EventoSismico> findById(String id) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            EventoSismicoEntity en = em.find(EventoSismicoEntity.class, id);
            return Optional.ofNullable(en).map(this::toDomain);
        } finally {
            em.close();
        }
    }

    public void save(EventoSismico evento) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            EventoSismicoEntity en = toEntity(evento, em);
            em.merge(en);
            em.getTransaction().commit();
        } finally {
            EntityManager e = em;
            if (e.getTransaction().isActive()) e.getTransaction().rollback();
            em.close();
        }
    }

    private EventoSismico toDomain(EventoSismicoEntity en) {
        // Map basic fields
        ClasificacionSismo clas = null;
        if (en.getClasificacion() != null) {
            clas = new ClasificacionSismo(en.getClasificacion().getNombre(), 0, 0);
        }
        AlcanceSismo alc = null;
        if (en.getAlcance() != null) {
            alc = new AlcanceSismo(en.getAlcance().getNombre(), null);
        }
        OrigenDeGeneracion ori = null;
        if (en.getOrigen() != null) {
            ori = new OrigenDeGeneracion(en.getOrigen().getNombre(), null);
        }

        // Domain expects lat/lon as strings; convert
        String latEpic = en.getEpicentroLat() != null ? String.valueOf(en.getEpicentroLat()) : null;
        String lngEpic = en.getEpicentroLng() != null ? String.valueOf(en.getEpicentroLng()) : null;
    // Hipocentro coordinates are not yet stored in the entity; keep null for now
    String latHip = null;
    String lngHip = null;

        EventoSismico dominio = new EventoSismico(en.getFechaOcurrencia(), latEpic, lngEpic, latHip, lngHip,
                en.getMagnitud() != null ? en.getMagnitud() : 0.0, clas, alc, ori);
        // set id
        dominio.setId(en.getId());

        // Map historial -> cambiosEstado (chronological)
        if (en.getHistorial() != null && !en.getHistorial().isEmpty()) {
            List<EventoHistorialEstadoEntity> hist = new ArrayList<>(en.getHistorial());
            hist.sort(Comparator.comparing(EventoHistorialEstadoEntity::getFechaHora));
            List<CambioEstadoES> cambios = new ArrayList<>();
            EventoHistorialEstadoEntity prev = null;
            for (EventoHistorialEstadoEntity h : hist) {
                String codigoEstado = h.getEstado() != null ? h.getEstado().getCodigo() : null;
                entidades.estadoPadreAbstracto.EstadoES estado = persistence.EstadoFactory.fromCodigo(codigoEstado);

                // map responsable (Empleado)
                Empleado responsable = null;
                if (h.getUsuario() != null) {
                    entidades.Empleado emp = new Empleado();
                    emp.setUsuario(new Usuario(h.getUsuario().getUsuarioId(), null));
                    responsable = emp;
                }

                CambioEstadoES cambio = new CambioEstadoES(h.getFechaHora(), null, responsable, estado);
                cambios.add(cambio);

                // set end time of previous cambio
                if (prev != null) {
                    int idx = cambios.size() - 2;
                    if (idx >= 0) cambios.get(idx).setFechaHoraFin(h.getFechaHora());
                }
                prev = h;
            }
            dominio.setCambiosEstado(cambios);
            // set current estado as the last one
            if (!cambios.isEmpty()) dominio.setEstado(cambios.get(cambios.size() - 1).getEstado());
        } else {
            // Map current estado from entity (fallback)
            dominio.setEstado(EstadoFactory.fromCodigo(en.getEstadoActual()));
        }
        return dominio;
    }

    private EventoSismicoEntity toEntity(EventoSismico evento, EntityManager em) {
        // ensure domain object has an id
        if (evento.getId() == null) {
            evento.setId(java.util.UUID.randomUUID().toString());
        }
        EventoSismicoEntity en = em.find(EventoSismicoEntity.class, evento.getId());
        if (en == null) en = new EventoSismicoEntity();

        en.setId(evento.getId());
        en.setFechaOcurrencia(evento.getFechaHoraOcurrencia());
        en.setMagnitud(evento.getValorMagnitud());
        try {
            en.setEpicentroLat(Double.parseDouble(evento.getLatitudEpicentro()));
            en.setEpicentroLng(Double.parseDouble(evento.getLongitudEpicentro()));
        } catch (Exception ex) {
            // ignore parse issues
        }
    // hipocentro not available in domain as km; leave null unless provided elsewhere
    en.setHipocentroKm(null);
    en.setEstadoActual(estadoCodigoFromEstado(evento.getEstado()));
        // map clasif/alcance/origen by name lookups
        if (evento.getClasificacionSismo() != null) {
            TypedQuery<ClasificacionSismoEntity> q = em.createQuery("SELECT c FROM ClasificacionSismoEntity c WHERE c.nombre = :n", ClasificacionSismoEntity.class);
            q.setParameter("n", evento.getClasificacionSismo().getNombre());
            List<ClasificacionSismoEntity> res = q.getResultList();
            if (!res.isEmpty()) en.setClasificacion(res.get(0));
        }
        if (evento.getAlcanceSismo() != null) {
            TypedQuery<AlcanceSismoEntity> q = em.createQuery("SELECT a FROM AlcanceSismoEntity a WHERE a.nombre = :n", AlcanceSismoEntity.class);
            q.setParameter("n", evento.getAlcanceSismo().getNombre());
            List<AlcanceSismoEntity> res = q.getResultList();
            if (!res.isEmpty()) en.setAlcance(res.get(0));
        }
        if (evento.getOrigenGeneracion() != null) {
            TypedQuery<OrigenDeGeneracionEntity> q = em.createQuery("SELECT o FROM OrigenDeGeneracionEntity o WHERE o.nombre = :n", OrigenDeGeneracionEntity.class);
            q.setParameter("n", evento.getOrigenGeneracion().getNombre());
            List<OrigenDeGeneracionEntity> res = q.getResultList();
            if (!res.isEmpty()) en.setOrigen(res.get(0));
        }

        return en;
    }

    private String estadoCodigoFromEstado(entidades.estadoPadreAbstracto.EstadoES estado) {
        if (estado == null) return null;
        String nombre = estado.getNombre();
        if (nombre == null) return null;
        switch (nombre.toLowerCase()) {
            case "autodetectado": return "auto_detectado";
            case "enrevision": return "bloqueado_en_revision";
            case "pendientederevision": return "pendiente_revision";
            case "derivado": return "derivado";
            case "confirmado": return "confirmado";
            case "rechazado": return "rechazado";
            case "pendientedecierre": return "pendiente_cierre";
            case "cerrado": return "cerrado";
            default: return nombre.toLowerCase();
        }
    }
}
