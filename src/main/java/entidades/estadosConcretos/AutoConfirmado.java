package entidades.estadosConcretos;

import entidades.estadoPadreAbstracto.EstadoES;

public class AutoConfirmado extends EstadoES {

    public void aquirirDatos(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }

    public void registrarPendienteCierreAutomatico(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }
}
