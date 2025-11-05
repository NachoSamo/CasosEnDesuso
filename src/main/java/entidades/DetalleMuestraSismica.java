package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleMuestraSismica {
    private int valor;
    private TipoDato tipoDato;

    public String getDatos() {
        return "DetalleMuestraSismica{" +
                "valor=" + valor +
                ", tipoDato=" + tipoDato.getDatos() +
                '}';
    }
}
