package entidades;

import java.time.LocalDateTime;
import java.util.*;

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

    // --- Getters y Setters ---
    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }

    public LocalDateTime getFechaHoraOcurrencia() { return fechaHoraOcurrencia; }
    public void setFechaHoraOcurrencia(LocalDateTime fechaHoraOcurrencia) { this.fechaHoraOcurrencia = fechaHoraOcurrencia; }

    public String getLatitudEpicentro() { return latitudEpicentro; }
    public void setLatitudEpicentro(String latitudEpicentro) { this.latitudEpicentro = latitudEpicentro; }

    public String getLongitudEpicentro() { return longitudEpicentro; }
    public void setLongitudEpicentro(String longitudEpicentro) { this.longitudEpicentro = longitudEpicentro; }

    public String getLatitudHipocentro() { return latitudHipocentro; }
    public void setLatitudHipocentro(String latitudHipocentro) { this.latitudHipocentro = latitudHipocentro; }

    public String getLongitudHipocentro() { return longitudHipocentro; }
    public void setLongitudHipocentro(String longitudHipocentro) { this.longitudHipocentro = longitudHipocentro; }

    public double getValorMagnitud() { return valorMagnitud; }
    public void setValorMagnitud(double valorMagnitud) { this.valorMagnitud = valorMagnitud; }

    public List<CambioEstado> getCambiosEstado() { return cambiosEstado; }
    public void setCambiosEstado(ArrayList<CambioEstado> cambiosEstado) { this.cambiosEstado = cambiosEstado; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public ClasificacionSismo getClasificacionSismo() { return clasificacionSismo; }
    public void setClasificacionSismo(ClasificacionSismo clasificacionSismo) { this.clasificacionSismo = clasificacionSismo; }

    public AlcanceSismo getAlcanceSismo() { return alcanceSismo; }
    public void setAlcanceSismo(AlcanceSismo alcanceSismo) { this.alcanceSismo = alcanceSismo; }

    public OrigenDeGeneracion getOrigenGeneracion() { return origenGeneracion; }
    public void setOrigenGeneracion(OrigenDeGeneracion origenGeneracion) { this.origenGeneracion = origenGeneracion; }

    public List<SerieTemporal> getSeriesTemporales() { return seriesTemporales; }
    public void setSeriesTemporales(List<SerieTemporal> seriesTemporales) { this.seriesTemporales = seriesTemporales; }

    public LocalDateTime getFechaHoraRevision() { return fechaHoraRevision; }
    public void setFechaHoraRevision(LocalDateTime fechaHoraRevision) { this.fechaHoraRevision = fechaHoraRevision; }

    public Empleado getResponsableRevision() { return responsableRevision; }
    public void setResponsableRevision(Empleado responsableRevision) { this.responsableRevision = responsableRevision; }

    public Boolean soySinRevisar() {
        return this.estado != null && this.estado.soySinRevisar();
    }
    public String getFechaHoraOcurrenciaTexto() {
        return this.fechaHoraOcurrencia != null ? this.fechaHoraOcurrencia.toString() : "";
    }

    public Map<String, Object> getDatos() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("fechaHora: ", getFechaHoraOcurrencia());
        datos.put("coordEpicentro: ", getCoordEpicentro());
        datos.put("coordHipocentro: ", getCoordHipocentro());
        datos.put("magnitud: ", getValorMagnitud());
        return datos;
    }

    public String getCoordEpicentro() {
        return getLatitudEpicentro() + getLongitudEpicentro();
    }

    public String getCoordHipocentro() {
        return getLatitudHipocentro() + getLongitudHipocentro();
    }



    public Map<SerieTemporal, List<String>> getDatosMuestrasSismicas() {
        System.out.println("📡 Obteniendo series temporales para el evento: " + this);
        Map<SerieTemporal, List<String>> datosSeries = new HashMap<>();

        for (SerieTemporal serie : seriesTemporales) {
            List<String> muestrasInfo = Collections.singletonList(serie.getDatosMuestrasSismicas());
            datosSeries.put(serie, muestrasInfo);
        }
        System.out.println("✅ Datos extraídos de las series: " + datosSeries);
        return datosSeries;
    }



    public void revisar(Estado nuevoEstado, LocalDateTime fechaCambio) {
        CambioEstado ultimoCambio = buscarCEActual();
        if (ultimoCambio != null) {
            ultimoCambio.setFechaHoraFin(fechaCambio);
        }
        crearNuevoCE(fechaCambio, null, nuevoEstado);
        setEstado(nuevoEstado);
    }


    public CambioEstado buscarCEActual() {
        if (cambiosEstado.isEmpty()) return null;
        CambioEstado ultimoCambio = cambiosEstado.get(cambiosEstado.size() - 1);
        return ultimoCambio.sosActual() ? ultimoCambio : null;
    }

    public void crearNuevoCE(LocalDateTime fechaCambio, LocalDateTime fechaFin, Estado estado) {
         CambioEstado nuevoCambio = new CambioEstado(fechaCambio, fechaFin, estado);
        cambiosEstado.add(nuevoCambio);
    }

    public Map<String, String> getACO() {
        Map<String, String> datos = new HashMap<>();
        datos.put("alcance", this.alcanceSismo.getNombre());
        datos.put("clasificacion", this.clasificacionSismo.getNombre());
        datos.put("origen", this.origenGeneracion.getNombre());
        return datos;
    }



    public Map<SerieTemporal, EstacionSismologica> getSeriesPorEstacion() {
        Map<SerieTemporal, EstacionSismologica> resultado = new HashMap<>();
        for (SerieTemporal serie : seriesTemporales) {
            Sismografo sismografo = (Sismografo) serie.getMuestrasSismicas();  // ✅ ← este método debe estar en SerieTemporal
            if (sismografo != null) {
                resultado.put(serie, sismografo.getEstacionSismologica());
            }
        }
        return resultado;
    }


    public void confirmar(Estado estadoConfirmado, LocalDateTime fechaCambio) {
        System.out.println("🔎 Buscando cambio de estado actual...");
        CambioEstado ultimoCambio = buscarCEActual();
        if (ultimoCambio != null) {
            ultimoCambio.setFechaHoraFin(fechaCambio);
            System.out.println("⏱ Cambio de estado actual cerrado");
        }

        crearNuevoCE(fechaCambio, null, estadoConfirmado);
        System.out.println("🆕 Nuevo cambio de estado creado");

        setEstado(estadoConfirmado);
        System.out.println("🆕 Estado seteado");

        System.out.println("✔ Evento confirmado a las " + fechaCambio);
    }

    public void derivar(Estado estadoDerivar, LocalDateTime fechaCambio) {
        System.out.println("🔎 Buscando cambio de estado actual...");
        CambioEstado ultimoCambio = buscarCEActual();
        if (ultimoCambio != null) {
            ultimoCambio.setFechaHoraFin(fechaCambio);
            System.out.println("⏱ Cambio de estado actual cerrado");
        }

        crearNuevoCE(fechaCambio, null, estadoDerivar);
        System.out.println("🆕 Nuevo cambio de estado creado");

        setEstado(estadoDerivar);
        System.out.println("🆕 Estado seteado");

        System.out.println("➡ Evento derivado a experto a las " + fechaCambio);
    }

    public void rechazar(Estado estadoRechazado, LocalDateTime fechaHoraActual, Empleado responsable) {
        System.out.println("🔎 Buscando cambio de estado actual...");
        CambioEstado actual = buscarCEActual();
        if (actual != null) {
            actual.setFechaHoraFin(fechaHoraActual);
            System.out.println("⏱ Cambio de estado actual cerrado");
        }

        crearNuevoCE(fechaHoraActual, null, estadoRechazado);
        System.out.println("🆕 Nuevo cambio de estado creado");

        this.fechaHoraRevision = fechaHoraActual;
        this.responsableRevision = responsable;
        System.out.println("🕒 FechaHora y Responsable de revision seteados");

        setEstado(estadoRechazado);
        System.out.println("🆕 Estado seteado");

        System.out.println("❌ Evento rechazado a las " + fechaHoraActual);
    }













    public void validarExistencias(EventoSismico evento) {
        if (evento.getValorMagnitud() == 0
                || evento.getAlcanceSismo() == null
                || evento.getOrigenGeneracion() == null) {
            System.out.println("⚠ Faltan datos esenciales.");
            return;
        }
    }
}
