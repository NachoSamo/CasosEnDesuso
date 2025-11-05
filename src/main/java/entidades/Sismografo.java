package entidades;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Sismografo {
    private LocalDate fechaAdquisicion;
    private int identificadorSismografo;
    private int nroSerie;
    private EstacionSismologica estacionSismologica;
    private List<SerieTemporal> seriesTemporales;
    private Object estacion;

    // Constructor
    public Sismografo(LocalDate fechaAdquisicion, int identificadorSismografo, int nroSerie,
                      EstacionSismologica estacionSismologica, List<SerieTemporal> seriesTemporales) {
        this.fechaAdquisicion = fechaAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
        this.nroSerie = nroSerie;
        this.estacionSismologica = estacionSismologica;
        this.seriesTemporales = seriesTemporales;
    }

    public Sismografo(String s1, EstacionSismologica est1) {
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


    public void agregarSerieTemporal(SerieTemporal serie) {
        if (serie != null) {
            this.seriesTemporales.add(serie);
        }
    }
}
