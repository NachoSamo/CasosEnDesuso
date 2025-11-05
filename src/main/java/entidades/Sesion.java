package entidades;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
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

    public static Sesion obtenerSesionActiva(List<Sesion> sesiones) {
        for (Sesion sesion : sesiones) {
            if (sesion.getFechaHoraFin() == null) {
                return sesion;
            }
        }
        return null;
    }
}
