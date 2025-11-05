package entidades.estadosConcretos;

import entidades.CambioEstado;
import entidades.Empleado;
import entidades.Estado;
import entidades.EventoSismico;

import java.time.LocalDateTime;

public class EnRevision extends Estado {

    public void rechazar(LocalDateTime fechahora, Empleado empleado
            , EventoSismico eventoSismico, CambioEstado[] cambiosEstado) {
    }

    public void confirmar(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }

    public void derivar() {
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }
}
