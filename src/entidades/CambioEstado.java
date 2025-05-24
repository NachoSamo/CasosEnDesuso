package entidades;

import java.time.LocalDateTime;

public class CambioEstado {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Estado estado;

    // Constructor
    public CambioEstado(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Estado estado) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = estado;
    }
    // Getters y Setters
    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }
    public Estado getEstado() {
        return estado;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    public Boolean sosActual() {
        return fechaHoraFin == null;
    }
}
