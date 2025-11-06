package persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos_derivaciones")
public class EventoDerivacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private EventoSismicoEntity evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "de_analista_id", nullable = false)
    private EmpleadoEntity deAnalista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_supervisor_id", nullable = false)
    private EmpleadoEntity aSupervisor;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    private String motivo;

    private String estado;

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public EventoSismicoEntity getEvento() { return evento; }
    public void setEvento(EventoSismicoEntity evento) { this.evento = evento; }
    public EmpleadoEntity getDeAnalista() { return deAnalista; }
    public void setDeAnalista(EmpleadoEntity deAnalista) { this.deAnalista = deAnalista; }
    public EmpleadoEntity getASupervisor() { return aSupervisor; }
    public void setASupervisor(EmpleadoEntity aSupervisor) { this.aSupervisor = aSupervisor; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
