package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Embeddable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrigenDeGeneracion {
    private String nombre;
    private String descripcion;
}
