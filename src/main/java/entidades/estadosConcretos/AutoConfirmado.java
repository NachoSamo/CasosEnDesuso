package entidades.estadosConcretos;

import entidades.Estado;

public class AutoConfirmado extends Estado {

    public void aquirirDatos(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }

    public void registrarPendienteCierreAutomatico(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }
}
