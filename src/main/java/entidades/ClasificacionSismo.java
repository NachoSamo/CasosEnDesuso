package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionSismo {
    private String nombre;
    private double kmProfundidadDesde;
    private double kmProfundidadHasta;
}
