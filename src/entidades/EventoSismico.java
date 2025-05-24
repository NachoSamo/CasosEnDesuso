package entidades;
import java.time.LocalDateTime;
import java.util.List;

public class EventoSismico {
    private LocalDateTime fechaHoraFin;
    private LocalDateTime fechaHoraOcurrencia;
    private String latitudEpicentro;
    private String longitudEpicentro;
    private String latitudHipocentro;
    private String longitudHipocentro;
    private double valorMagnitud;
    private List<CambioEstado> cambiosEstado;
    private Estado estado;
    private MagnitudRichter magnitudRichter;
    private ClasificacionSismo clasificacionSismo;
    private AlcanceSismo alcanceSismo;
    private OrigenDeGeneracion origenGeneracion;
    private List<SerieTemporal> seriesTemporales;

    // Constructor
    public EventoSismico(LocalDateTime fechaHoraOcurrencia, String latitudEpicentro, String longitudEpicentro, String latitudHipocentro, String longitudHipocentro, double valorMagnitud, List<CambioEstado> cambiosEstado, Estado estado, MagnitudRichter magnitudRichter, ClasificacionSismo clasificacionSismo, AlcanceSismo alcanceSismo, OrigenDeGeneracion origenGeneracion, List<SerieTemporal> seriesTemporales) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
        this.latitudEpicentro = latitudEpicentro;
        this.longitudEpicentro = longitudEpicentro;
        this.latitudHipocentro = latitudHipocentro;
        this.longitudHipocentro = longitudHipocentro;
        this.valorMagnitud = valorMagnitud;
        this.cambiosEstado = cambiosEstado;
        this.estado = estado;
        this.magnitudRichter = magnitudRichter;
        this.clasificacionSismo = clasificacionSismo;
        this.alcanceSismo = alcanceSismo;
        this.origenGeneracion = origenGeneracion;
        this.seriesTemporales = seriesTemporales;
    }
    // Getters y Setters
    //CAMBIAR LOS METODOS QUE REFERENCIAN
    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public LocalDateTime getFechaHoraOcurrencia() {
        return fechaHoraOcurrencia;
    }

    public void setFechaHoraOcurrencia(LocalDateTime fechaHoraOcurrencia) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
    }

    public String getLatitudEpicentro() {
        return latitudEpicentro;
    }

    public void setLatitudEpicentro(String latitudEpicentro) {
        this.latitudEpicentro = latitudEpicentro;
    }

    public String getLongitudEpicentro() {
        return longitudEpicentro;
    }

    public void setLongitudEpicentro(String longitudEpicentro) {
        this.longitudEpicentro = longitudEpicentro;
    }

    public String getLatitudHipocentro() {
        return latitudHipocentro;
    }

    public void setLatitudHipocentro(String latitudHipocentro) {
        this.latitudHipocentro = latitudHipocentro;
    }

    public String getLongitudHipocentro() {
        return longitudHipocentro;
    }

    public void setLongitudHipocentro(String longitudHipocentro) {
        this.longitudHipocentro = longitudHipocentro;
    }

    public double getValorMagnitud() {
        return valorMagnitud;
    }

    public void setValorMagnitud(double valorMagnitud) {
        this.valorMagnitud = valorMagnitud;
    }

    public List<CambioEstado> getCambiosEstado() {
        return cambiosEstado;
    }

    public void setCambiosEstado(List<CambioEstado> cambiosEstado) {
        this.cambiosEstado = cambiosEstado;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public MagnitudRichter getMagnitudRichter() {
        return magnitudRichter;
    }

    public void setMagnitudRichter(MagnitudRichter magnitudRichter) {
        this.magnitudRichter = magnitudRichter;
    }

    public ClasificacionSismo getClasificacionSismo() {
        return clasificacionSismo;
    }

    public void setClasificacionSismo(ClasificacionSismo clasificacionSismo) {
        this.clasificacionSismo = clasificacionSismo;
    }

    public AlcanceSismo getAlcanceSismo() {
        return alcanceSismo;
    }

    public void setAlcanceSismo(AlcanceSismo alcanceSismo) {
        this.alcanceSismo = alcanceSismo;
    }

    public OrigenDeGeneracion getOrigenGeneracion() {
        return origenGeneracion;
    }

    public void setOrigenGeneracion(OrigenDeGeneracion origenGeneracion) {
        this.origenGeneracion = origenGeneracion;
    }

    public List<SerieTemporal> getSeriesTemporales() {
        return seriesTemporales;
    }

    public void setSeriesTemporales(List<SerieTemporal> seriesTemporales) {
        this.seriesTemporales = seriesTemporales;
    }
}
