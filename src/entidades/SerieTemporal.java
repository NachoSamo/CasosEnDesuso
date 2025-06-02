package entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SerieTemporal {
    private String condicionAlarma;
    private LocalDateTime fechaHoraInicioRegistroMuestras;
    private LocalDateTime fechaHoraRegistro;
    private double frecuenciaMuestreo;
    private List<MuestraSismica> muestrasSismicas;

    // Constructor
    public SerieTemporal(String condicionAlarma, LocalDateTime fechaHoraInicioRegistroMuestras,
                         LocalDateTime fechaHoraRegistro, double frecuenciaMuestreo) {
        this.condicionAlarma = condicionAlarma;
        this.fechaHoraInicioRegistroMuestras = fechaHoraInicioRegistroMuestras;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.frecuenciaMuestreo = frecuenciaMuestreo;
        this.muestrasSismicas = new ArrayList<>(); // ✅ aseguramos que no sea null
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

    public void setMuestrasSismicas(List<MuestraSismica> muestrasSismicas) {
        this.muestrasSismicas = muestrasSismicas;
    }

    // ✅ Método necesario para el diagrama de secuencia
    public List<MuestraSismica> getMuestrasSismicas() {
        return muestrasSismicas;
    }

    // ✅ Método para representar en texto los datos de las muestras
    public String getDatosMuestrasSismicas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Datos de las muestras de la serie:{");
        for (MuestraSismica muestra : muestrasSismicas) {
            sb.append(muestra.getDatos()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }

    public void agregarMuestraSismica(MuestraSismica muestra) {
        this.muestrasSismicas.add(muestra);
    }

    public void eliminarMuestraSismica(MuestraSismica muestra) {
        this.muestrasSismicas.remove(muestra);
    }

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