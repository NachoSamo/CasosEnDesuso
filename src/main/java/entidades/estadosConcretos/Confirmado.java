package entidades.estadosConcretos;

import entidades.Estado;

public class Confirmado extends Estado {

    public void adquirirDatos(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }

    public void registrarPendienteCierre(){
        System.out.println("Metodo que no corresponde al flujo normal del CU");
    }
}
