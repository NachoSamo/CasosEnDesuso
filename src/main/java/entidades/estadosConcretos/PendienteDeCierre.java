package entidades.estadosConcretos;

import entidades.Estado;

public class PendienteDeCierre extends Estado {
    public void cerrar(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }
}
