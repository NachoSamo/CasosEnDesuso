package persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos_bloqueos_revision")
public class EventoBloqueoRevisionEntity {
    @Id
    @Column(name = "evento_id", length = 36)
    private String eventoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analista_id", nullable = false)
    private EmpleadoEntity analista;

    @Column(name = "tomado_en", nullable = false)
    private LocalDateTime tomadoEn;

    @Column(name = "liberado_en")
    private LocalDateTime liberadoEn;

    // getters/setters
    public String getEventoId() { return eventoId; }
    public void setEventoId(String eventoId) { this.eventoId = eventoId; }
    public EmpleadoEntity getAnalista() { return analista; }
    public void setAnalista(EmpleadoEntity analista) { this.analista = analista; }
    public LocalDateTime getTomadoEn() { return tomadoEn; }
    public void setTomadoEn(LocalDateTime tomadoEn) { this.tomadoEn = tomadoEn; }
    public LocalDateTime getLiberadoEn() { return liberadoEn; }
    public void setLiberadoEn(LocalDateTime liberadoEn) { this.liberadoEn = liberadoEn; }
}
