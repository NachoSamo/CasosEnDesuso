package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstacionSismologica {
    private String codigoEstacion;
    private Boolean documentoCertificacionAdq;
    private LocalDate fechaSolicitudCertificacion;
    private String latitud;
    private String longitud;
    private String nombre;
    private int nroCertificacionAdquisicion;

    public EstacionSismologica(String estacionOeste) {
    }
}
