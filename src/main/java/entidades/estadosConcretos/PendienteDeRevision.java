package entidades.estadosConcretos;

import entidades.Empleado;
import entidades.Estado;

import java.time.LocalDateTime;

public class PendienteDeRevision extends Estado {
    public void revisar(LocalDateTime fechahora, Empleado empleado) {

    }

    public void controlarTiempo() {
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }


}
