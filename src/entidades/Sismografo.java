package entidades;
import java.util.List;
import java.time.LocalDate;

public class Sismografo {
    private LocalDate fechaAdquisicion;
    private int identificadorSismografo;
    private int nroSerie;
    private EstacionSismologica estacionSismologica;
    private List<SerieTemporal> seriesTemporales;

    // Constructor
    public Sismografo(LocalDate fechaAdquisicion, int identificadorSismografo, int nroSerie, EstacionSismologica estacionSismologica, List<SerieTemporal> seriesTemporales) {
        this.fechaAdquisicion = fechaAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
        this.nroSerie = nroSerie;
        this.estacionSismologica = estacionSismologica;
        this.seriesTemporales = seriesTemporales;
    }   
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

    public Map<SerieTemporal, EstacionSismologica> getSeriesPorEstacion() {
    Map<SerieTemporal, EstacionSismologica> map = new HashMap<>();
    for (SerieTemporal st : this.getSeriesTemporales()) {
        map.put(st, this.getEstacionSismologica());
    }
    return map;
    }
}
