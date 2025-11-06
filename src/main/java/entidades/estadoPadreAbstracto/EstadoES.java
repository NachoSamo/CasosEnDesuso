package entidades.estadoPadreAbstracto;

import entidades.Empleado;
import entidades.EventoSismico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase abstracta que representa el estado de un EventoSismico, aplicando el patrón de diseño State.
 * Define todas las posibles acciones que pueden modificar el estado de un evento.
 * La implementación por defecto de cada acción es lanzar una excepción, forzando
 * a las clases de estado concretas a sobreescribir únicamente los métodos que son
 * válidos para dicho estado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class EstadoES {

    protected String nombre;

    public void registrarSismo(EventoSismico es) {
        throw new UnsupportedOperationException("La operación 'registrarSismo' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void revisar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        throw new UnsupportedOperationException("La operación 'revisar' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void confirmarRevision(EventoSismico es) {
        throw new UnsupportedOperationException("La operación 'confirmarRevision' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void derivar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        throw new UnsupportedOperationException("La operación 'derivar' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void confirmar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        throw new UnsupportedOperationException("La operación 'confirmar' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void controlarTiempo(EventoSismico es) {
        throw new UnsupportedOperationException("La operación 'controlarTiempo' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void adquirirDatos(EventoSismico es) {
        throw new UnsupportedOperationException("La operación 'adquirirDatos' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void anular(EventoSismico es) {
        throw new UnsupportedOperationException("La operación 'anular' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void registrarPendienteCierre(EventoSismico es) {
        throw new UnsupportedOperationException("La operación 'registrarPendienteCierre' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void registrarPendienteCierreAutomatico(EventoSismico es) {
        throw new UnsupportedOperationException("La operación 'registrarPendienteCierreAutomatico' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void cerrar(EventoSismico es) {
        throw new UnsupportedOperationException("La operación 'cerrar' no es válida en el estado '" + this.getNombre() + "'");
    }

    public void rechazar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        throw new UnsupportedOperationException("La operación 'rechazar' no es válida en el estado '" + this.getNombre() + "'");
    }
}