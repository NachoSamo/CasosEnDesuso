package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDato {
    private String denominacion;
    private String nombreUnidadMedida;
    private double valorUmbral;

    public String getDatos() {
        return "TipoDato{" +
                "denominacion='" + denominacion + '\'' +
                ", nombreUnidadMedida='" + nombreUnidadMedida + '\'' +
                ", valorUmbral=" + valorUmbral +
                '}';
    }
}
