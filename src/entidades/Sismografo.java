package entidades;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sismografo {
    private LocalDate fechaAdquisicion;
    private int identificadorSismografo;
    private int nroSerie;
    private EstacionSismologica estacionSismologica;
    private List<SerieTemporal> seriesTemporales;

    // Constructor
    public Sismografo(LocalDate fechaAdquisicion, int identificadorSismografo, int nroSerie,
                      EstacionSismologica estacionSismologica, List<SerieTemporal> seriesTemporales) {
        this.fechaAdquisicion = fechaAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
        this.nroSerie = nroSerie;
        this.estacionSismologica = estacionSismologica;
        this.seriesTemporales = seriesTemporales;
    }

    // Getters y Setters
    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public int getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public void setIdentificadorSismografo(int identificadorSismografo) {
        this.identificadorSismografo = identificadorSismografo;
    }

    public int getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(int nroSerie) {
        this.nroSerie = nroSerie;
    }

    public EstacionSismologica getEstacionSismologica() {
        return estacionSismologica;
    }

    public void setEstacionSismologica(EstacionSismologica estacionSismologica) {
        this.estacionSismologica = estacionSismologica;
    }

    public List<SerieTemporal> getSeriesTemporales() {
        return seriesTemporales;
    }

    public void setSeriesTemporales(List<SerieTemporal> seriesTemporales) {
        this.seriesTemporales = seriesTemporales;
    }

    /**
     * Devuelve un mapa donde cada SerieTemporal se asocia con su Estación Sismológica.
     * Es llamado desde el Controlador al ordenar por estación.
     */
    public Map<SerieTemporal, EstacionSismologica> getSeriesPorEstacion() {
        Map<SerieTemporal, EstacionSismologica> map = new HashMap<>();
        for (SerieTemporal serie : this.seriesTemporales) {
            map.put(serie, this.estacionSismologica);
        }
        return map;
    }


}
