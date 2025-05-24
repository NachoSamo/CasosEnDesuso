package entidades;

import java.time.LocalDateTime;
import java.util.List;

public class EventoSismico {
    //atributos
    private LocalDateTime fechaHoraOcurrencia;
    private int latitudEpicentro;
    private int longitudEpicentro;
    private double profundidadEpicentro;
    private double valorMagnitud;
    private String responsableRevision;
    private boolean esPendienteRevision;
    private int id;
    private Estado estado; 
    private Origen origen; 
    private Magnitud magnitud;
    private ClasificacionSismo clasificacionSismo;
    private Alcance alcance;
    private List<CambioEstado> cambiosEstado;
    
    // Constructor
    public EventoSismico(LocalDateTime fechaHoraOcurrencia, int latitudEpicentro, int longitudEpicentro, 
                         double profundidadEpicentro, double valorMagnitud, String responsableRevision, 
                         boolean esPendienteRevision, int id, Estado estado, Origen origen, 
                         Magnitud magnitud, ClasificacionSismo clasificacionSismo, Alcance alcance) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
        this.latitudEpicentro = latitudEpicentro;
        this.longitudEpicentro = longitudEpicentro;
        this.profundidadEpicentro = profundidadEpicentro;
        this.valorMagnitud = valorMagnitud;
        this.responsableRevision = responsableRevision;
        this.esPendienteRevision = esPendienteRevision;
        this.id = id;
        this.estado = estado;
        this.origen = origen;
        this.magnitud = magnitud;
        this.clasificacionSismo = clasificacionSismo;
        this.alcance = alcance;
        this.cambiosEstado = null; // Inicializar como null o una lista vacía según sea necesario

    }
    // Getters y Setters
    public LocalDateTime getFechaHoraOcurrencia() {
        return fechaHoraOcurrencia;
    }
    public void setFechaHoraOcurrencia(LocalDateTime fechaHoraOcurrencia) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
    }
    public int getLatitudEpicentro() {
        return latitudEpicentro;
    }
    public void setLatitudEpicentro(int latitudEpicentro) {
        this.latitudEpicentro = latitudEpicentro;
    }

    public int getLongitudEpicentro() {
        return longitudEpicentro;
    }

    public void setLongitudEpicentro(int longitudEpicentro) {
        this.longitudEpicentro = longitudEpicentro;
    }

    public double getProfundidadEpicentro() {
        return profundidadEpicentro;
    }

    public void setProfundidadEpicentro(double profundidadEpicentro) {
        this.profundidadEpicentro = profundidadEpicentro;
    }

    public double getValorMagnitud() {
        return valorMagnitud;
    }

    public void setValorMagnitud(double valorMagnitud) {
        this.valorMagnitud = valorMagnitud;
    }

    public String getResponsableRevision() {
        return responsableRevision;
    }

    public void setResponsableRevision(String responsableRevision) {
        this.responsableRevision = responsableRevision;
    }

    public boolean isEsPendienteRevision() {
        return esPendienteRevision;
    }

    public void setEsPendienteRevision(boolean esPendienteRevision) {
        this.esPendienteRevision = esPendienteRevision;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Origen getOrigen() {
        return origen;
    }

    public void setOrigen(Origen origen) {
        this.origen = origen;
    }

    public Magnitud getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(Magnitud magnitud) {
        this.magnitud = magnitud;
    }

    public ClasificacionSismo getClasificacionSismo() {
        return clasificacionSismo;
    }

    public void setClasificacionSismo(ClasificacionSismo clasificacionSismo) {
        this.clasificacionSismo = clasificacionSismo;
    }

    public Alcance getAlcance() {
        return alcance;
    }

    public void setAlcance(Alcance alcance) {
        this.alcance = alcance;
    }

    public List<CambioEstado> getCambiosEstado() {
        return cambiosEstado;
    }

    public void setCambiosEstado(List<CambioEstado> cambiosEstado) {
        this.cambiosEstado = cambiosEstado;
    }
}
