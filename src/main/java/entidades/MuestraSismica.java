package entidades;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MuestraSismica {
    private LocalDateTime fechaHoraMuestra;
    private List<DetalleMuestraSismica> detallesMuestraSismica;

    // Constructor
    public MuestraSismica(LocalDateTime fechaHoraMuestra) {
        this.fechaHoraMuestra = fechaHoraMuestra;
        this.detallesMuestraSismica = new ArrayList<>();
    }

    // Métodos para agregar y eliminar detalles de la muestra sísmica
    public void agregarDetalleMuestra(DetalleMuestraSismica detalle) {
        this.detallesMuestraSismica.add(detalle);
    }
    public void eliminarDetalleMuestra(DetalleMuestraSismica detalle) {
        this.detallesMuestraSismica.remove(detalle);
    }
    // Método para obtener los datos de la muestra sísmica
    public String getDatos() {
        StringBuilder sb = new StringBuilder();
        sb.append("MuestraSismica{")
          .append("fechaHoraMuestra=").append(fechaHoraMuestra)
          .append(", detallesMuestraSismica=[");
        for (DetalleMuestraSismica detalle : detallesMuestraSismica) {
            sb.append(detalle.getDatos()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }
}
