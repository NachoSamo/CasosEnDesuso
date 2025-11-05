package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioEstado {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Estado estado;

    public Boolean sosActual() {
        return fechaHoraFin == null;
    }
    public void setFechaHoraFin() {
        this.fechaHoraFin = LocalDateTime.now();
    }

    public void setResponsable(Empleado empleadoResponsable) {
    }
}
