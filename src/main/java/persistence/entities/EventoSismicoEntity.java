package persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos_sismicos")
public class EventoSismicoEntity {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "fecha_ocurrencia", nullable = false)
    private LocalDateTime fechaOcurrencia;

    private Double magnitud;

    @Column(name = "epicentro_lat")
    private Double epicentroLat;

    @Column(name = "epicentro_lng")
    private Double epicentroLng;

    @Column(name = "hipocentro_km")
    private Double hipocentroKm;

    @Column(name = "estado_actual", nullable = false)
    private String estadoActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clasificacion_id")
    private ClasificacionSismoEntity clasificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcance_id")
    private AlcanceSismoEntity alcance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_id")
    private OrigenDeGeneracionEntity origen;

    @Column(name = "auto_confirmado")
    private Boolean autoConfirmado;

    @Column(name = "cerrado_en")
    private LocalDateTime cerradoEn;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoHistorialEstadoEntity> historial = new ArrayList<>();

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoDerivacionEntity> derivaciones = new ArrayList<>();

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getFechaOcurrencia() { return fechaOcurrencia; }
    public void setFechaOcurrencia(LocalDateTime fechaOcurrencia) { this.fechaOcurrencia = fechaOcurrencia; }
    public Double getMagnitud() { return magnitud; }
    public void setMagnitud(Double magnitud) { this.magnitud = magnitud; }
    public Double getEpicentroLat() { return epicentroLat; }
    public void setEpicentroLat(Double epicentroLat) { this.epicentroLat = epicentroLat; }
    public Double getEpicentroLng() { return epicentroLng; }
    public void setEpicentroLng(Double epicentroLng) { this.epicentroLng = epicentroLng; }
    public Double getHipocentroKm() { return hipocentroKm; }
    public void setHipocentroKm(Double hipocentroKm) { this.hipocentroKm = hipocentroKm; }
    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }
    public ClasificacionSismoEntity getClasificacion() { return clasificacion; }
    public void setClasificacion(ClasificacionSismoEntity clasificacion) { this.clasificacion = clasificacion; }
    public AlcanceSismoEntity getAlcance() { return alcance; }
    public void setAlcance(AlcanceSismoEntity alcance) { this.alcance = alcance; }
    public OrigenDeGeneracionEntity getOrigen() { return origen; }
    public void setOrigen(OrigenDeGeneracionEntity origen) { this.origen = origen; }
    public Boolean getAutoConfirmado() { return autoConfirmado; }
    public void setAutoConfirmado(Boolean autoConfirmado) { this.autoConfirmado = autoConfirmado; }
    public LocalDateTime getCerradoEn() { return cerradoEn; }
    public void setCerradoEn(LocalDateTime cerradoEn) { this.cerradoEn = cerradoEn; }
    public List<EventoHistorialEstadoEntity> getHistorial() { return historial; }
    public void setHistorial(List<EventoHistorialEstadoEntity> historial) { this.historial = historial; }
    public List<EventoDerivacionEntity> getDerivaciones() { return derivaciones; }
    public void setDerivaciones(List<EventoDerivacionEntity> derivaciones) { this.derivaciones = derivaciones; }
}
