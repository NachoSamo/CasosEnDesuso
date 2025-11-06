package entidades;

import entidades.estadoPadreAbstracto.EstadoES;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CambioEstadoES {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Empleado responsable;
    private EstadoES estado;

    public CambioEstadoES(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Empleado responsable, EstadoES estado) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.responsable = responsable;
        this.estado = estado;
    }

    public boolean esActual() {
        return this.fechaHoraFin == null;
    }
}