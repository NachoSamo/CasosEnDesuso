package entidades.estadosConcretos;

import entidades.CambioEstadoES;
import entidades.Empleado;
import entidades.EventoSismico;
import entidades.estadoPadreAbstracto.EstadoES;

import java.time.LocalDateTime;

public class AutoDetectado extends EstadoES {

    public AutoDetectado() {
        super("AutoDetectado");
    }

    @Override
    public void revisar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        // 1. Finalizar el estado actual
        CambioEstadoES actual = es.getCambioEstadoActual();
        if (actual != null) {
            actual.setFechaHoraFin(fh);
        }

        // 2. Crear el nuevo estado y el nuevo cambio de estado
        EstadoES nuevoEstado = new EnRevision();
        CambioEstadoES nuevoCambio = new CambioEstadoES(fh, null, resp, nuevoEstado);

        // 3. Actualizar el evento sísmico (contexto)
        es.agregarCE(nuevoCambio);
        es.setEstado(nuevoEstado);
    }
}