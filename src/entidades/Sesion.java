package entidades;
import java.time.LocalDateTime;
import java.util.List;

public class Sesion {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Usuario usuario;

    // Constructor
    public Sesion(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Usuario usuario) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.usuario = usuario;
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
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public static Sesion obtenerSesionActiva(List<Sesion> sesiones) {
        for (Sesion sesion : sesiones) {
            if (sesion.getFechaHoraFin() == null) {
                return sesion;
            }
        }
        return null;
    }
}
