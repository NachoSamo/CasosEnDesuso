package entidades;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class EventoSismico {
    private LocalDateTime fechaHoraFin;
    private LocalDateTime fechaHoraOcurrencia;
    private String latitudEpicentro;
    private String longitudEpicentro;
    private String latitudHipocentro;
    private String longitudHipocentro;
    private double valorMagnitud;
    private LocalDateTime fechaHoraRevision;
    private Empleado responsableRevision;
    private ArrayList<CambioEstado> cambiosEstado;
    private Estado estado;
    private ClasificacionSismo clasificacionSismo;
    private AlcanceSismo alcanceSismo;
    private OrigenDeGeneracion origenGeneracion;
    private List<SerieTemporal> seriesTemporales;

    // Constructor
    public EventoSismico(LocalDateTime fechaHoraOcurrencia, String latitudEpicentro, String longitudEpicentro,
                         String latitudHipocentro, String longitudHipocentro, double valorMagnitud,
                         ClasificacionSismo clasificacionSismo, AlcanceSismo alcanceSismo,
                         OrigenDeGeneracion origenGeneracion) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
        this.latitudEpicentro = latitudEpicentro;
        this.longitudEpicentro = longitudEpicentro;
        this.latitudHipocentro = latitudHipocentro;
        this.longitudHipocentro = longitudHipocentro;
        this.valorMagnitud = valorMagnitud;
        this.clasificacionSismo = clasificacionSismo;
        this.alcanceSismo = alcanceSismo;
        this.origenGeneracion = origenGeneracion;
        this.cambiosEstado = new ArrayList<>();
        this.seriesTemporales = new ArrayList<>();
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

    public LocalDateTime getFechaHoraRevision() {
        return fechaHoraRevision;
    }

    public void setFechaHoraRevision(LocalDateTime fechaHoraRevision) {
        this.fechaHoraRevision = fechaHoraRevision;
    }

    public Empleado getResponsableRevision() {
        return responsableRevision;
    }

    public void setResponsableRevision(Empleado responsableRevision) {
        this.responsableRevision = responsableRevision;
    }

    public Boolean esPendienteDeRevision() {
        return this.estado.soyPendienteDeRevision;
    }

    /*CAMBIAR!!!!!!!
    public Map<String, Object> getDatos() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("fechaHoraOcurrencia", getFechaHoraOcurrencia());
        datos.put("coordEpicentro", getCoordEpicentro());
        datos.put("coordHipocentro", getCoordHipocentro());
        datos.put("magnitud", getValorMagnitud());
        return datos;
    } */
   public getDatos() {
        return getFechaHoraOcurrencia(), getLatitudEpicentro(), getLongitudEpicentro(), getLatitudHipocentro(),
            "longitudHipocentro", getLongitudHipocentro(),
            "magnitud", getValorMagnitud()
        );
   }

    /**
     * Cambia el estado del evento sísmico.
     * @param nuevoEstado El nuevo estado a asignar.
     * @param fechaCambio La fecha y hora del cambio de estado.
     */
    public void revisar(Estado nuevoEstado, LocalDateTime fechaCambio) {
        // Cierra el cambio de estado actual si existe
        CambioEstado ultimoCambio = buscarCEActual();
        if (ultimoCambio != null) {
            ultimoCambio.setFechaHoraFin(fechaCambio); // Cierra el último cambio de estado
        }
        // Crea un nuevo cambio de estado
       crearNuevoCE(fechaCambio, null, nuevoEstado);
        // Actualiza el estado actual
        setEstado(nuevoEstado);
    }

    public CambioEstado buscarCEActual() {
        if (cambiosEstado.isEmpty()) {
            return null; // No hay cambios de estado
        }
        CambioEstado ultimoCambio = cambiosEstado.get(cambiosEstado.size() - 1);
        return ultimoCambio.sosActual() ? ultimoCambio : null; // Retorna el último cambio si es actual
    }

    public void crearNuevoCE(LocalDateTime fechaCambio, null, Estado estado) {
        CambioEstado nuevoCambio = new CambioEstado(fechaCambio, null, estado);
        cambiosEstado.add(nuevoCambio);
    }

    public String getACO() { //sabra dios
        return this.alcanceSismo.getAlcanceSismo().getNombre(), this.clasificacionSismo.getNombre(), this.origenGeneracion.getNombre();
    }


}