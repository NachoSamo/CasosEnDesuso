package entidades;

import entidades.estadoPadreAbstracto.EstadoES;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cambios_estado")
public class CambioEstadoES {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Empleado responsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_sismico_id")
    private EventoSismico eventoSismico;

    @Transient
    private EstadoES estado;

    @Column(name = "estado_class_name")
    private String estadoClassName;

    public CambioEstadoES(LocalDateTime fechaHoraInicio, Empleado responsable, EstadoES estado) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = null;
        this.responsable = responsable;
        this.setEstado(estado);
    }

    public boolean esActual() {
        return this.fechaHoraFin == null;
    }


    public void setEstado(EstadoES estado) {
        this.estado = estado;
        if (estado != null) {
            this.estadoClassName = estado.getClass().getName();
        } else {
            this.estadoClassName = null;
        }
    }


    @PostLoad
    private void rehidratarEstado() {
        if (this.estadoClassName != null) {
            try {
                Class<?> clazz = Class.forName(this.estadoClassName);
                this.estado = (EstadoES) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                // Manejar la excepción (e.g., loggear un error)
                System.err.println("Error al rehidratar el estado: " + e.getMessage());
                this.estado = null;
            }
        }
    }
}