package entidades.estadosConcretos;

import entidades.estadoPadreAbstracto.EstadoES;

public class PendienteDeCierre extends EstadoES {
    public void cerrar(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }
}
