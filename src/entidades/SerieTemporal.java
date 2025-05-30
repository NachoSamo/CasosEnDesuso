package entidades;
import java.time.LocalDateTime;
import java.util.List;

public class SerieTemporal {
    private String condicionAlarma;
    private LocalDateTime fechaHoraInicioRegistroMuestras;
    private LocalDateTime fechaHoraRegistro;
    private double frecuenciaMuestreo;
    private List<MuestraSismica> muestrasSismicas;

    // Constructor
    public SerieTemporal(String condicionAlarma, LocalDateTime fechaHoraInicioRegistroMuestras, LocalDateTime fechaHoraRegistro, double frecuenciaMuestreo) {
        this.condicionAlarma = condicionAlarma;
        this.fechaHoraInicioRegistroMuestras = fechaHoraInicioRegistroMuestras;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.frecuenciaMuestreo = frecuenciaMuestreo;
    }
    // Getters y Setters
    public String getCondicionAlarma() {
        return condicionAlarma;
    }
    public void setCondicionAlarma(String condicionAlarma) {
        this.condicionAlarma = condicionAlarma;
    }
    public LocalDateTime getFechaHoraInicioRegistroMuestras() {
        return fechaHoraInicioRegistroMuestras;
    }
    public void setFechaHoraInicioRegistroMuestras(LocalDateTime fechaHoraInicioRegistroMuestras) {
        this.fechaHoraInicioRegistroMuestras = fechaHoraInicioRegistroMuestras;
    }
    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }
    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }
    public double getFrecuenciaMuestreo() {
        return frecuenciaMuestreo;
    }
    public void setFrecuenciaMuestreo(double frecuenciaMuestreo) {
        this.frecuenciaMuestreo = frecuenciaMuestreo;
    }

    //Nos interesa este metodo para la secuencia
    public String getMuestrasSismicas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Datos de las muestras de la serie:{");
        for (MuestraSismica muestra : muestrasSismicas) {
            sb.append(muestra.getDatos()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }
    public void setMuestrasSismicas(List<MuestraSismica> muestrasSismicas) {
        this.muestrasSismicas = muestrasSismicas;
    }
    
    // Método para agregar una muestra sísmica a la serie temporal
    public void agregarMuestraSismica(MuestraSismica muestra) {
        this.muestrasSismicas.add(muestra);
    }
    // Método para eliminar una muestra sísmica de la serie temporal
    public void eliminarMuestraSismica(MuestraSismica muestra) {
        this.muestrasSismicas.remove(muestra);
    }
    // Método para obtener los datos de la serie temporal
    public String getDatos() {
        StringBuilder sb = new StringBuilder();
        sb.append("SerieTemporal{")
          .append("condicionAlarma='").append(condicionAlarma).append('\'')
          .append(", fechaHoraInicioRegistroMuestras=").append(fechaHoraInicioRegistroMuestras)
          .append(", fechaHoraRegistro=").append(fechaHoraRegistro)
          .append(", frecuenciaMuestreo=").append(frecuenciaMuestreo)
          .append(", muestrasSismicas=[");
        for (MuestraSismica muestra : muestrasSismicas) {
            sb.append(muestra.getDatos()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }

    
}
