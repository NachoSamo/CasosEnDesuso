package entidades.estadoPadreAbstracto;

import entidades.CambioEstado;
import entidades.Empleado;
import entidades.EventoSismico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class EstadoES {

    protected String nombreEstado;
    protected String ambito;


    public void revisar(LocalDateTime fechahora, Empleado empleado) {

    }

    public void rechazar(LocalDateTime fechahora, Empleado empleado
            , EventoSismico eventoSismico, CambioEstado[] cambiosEstado) {
    }

    public void registrarSismo() {
    }

    public void confirmarRevision() {
    }

    public void derivar() {
    }

    public void controlarTiempo() {
    }

    public void adquiriDatos() {
    }

    public void anular() {
    }

    public void registrarPedienteCierre() {
    }

    public void registrarCierreAutomatico() {
    }

    public void cerrar() {
    }

}