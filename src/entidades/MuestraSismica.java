package entidades;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class MuestraSismica {
    private LocalDateTime fechaHoraMuestra;
    private List<DetalleMuestraSismica> detallesMuestraSismica;

    // Constructor
    public MuestraSismica(LocalDateTime fechaHoraMuestra) {
        this.fechaHoraMuestra = fechaHoraMuestra;
        this.detallesMuestraSismica = new ArrayList<>();
    }
    // Getters y Setters
    public LocalDateTime getFechaHoraMuestra() {
        return fechaHoraMuestra;
    }
    public void setFechaHoraMuestra(LocalDateTime fechaHoraMuestra) {
        this.fechaHoraMuestra = fechaHoraMuestra;
    }

    public List<DetalleMuestraSismica> getDetallesMuestra() {
        return detallesMuestraSismica;
    }
    public void setDetallesMuestra(List<DetalleMuestraSismica> detallesMuestra) {
        this.detallesMuestraSismica = detallesMuestra;
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
