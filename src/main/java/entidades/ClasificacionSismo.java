package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ClasificacionSismo {
    private String nombre;
    private double kmProfundidadDesde;
    private double kmProfundidadHasta;
}
