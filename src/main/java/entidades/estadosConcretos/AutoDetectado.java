package entidades.estadosConcretos;

import entidades.CambioEstadoES;
import entidades.Empleado;
import entidades.EventoSismico;
import entidades.estadoPadreAbstracto.EstadoES;

import java.time.LocalDateTime;
import java.util.List;

public class AutoDetectado extends EstadoES {

    public AutoDetectado() {
        super("AutoDetectado");
    }


    @Override
    public void revisar(EventoSismico es, LocalDateTime fh, Empleado resp, List<CambioEstadoES> cambiosEstado) {
        // 1. Buscar el cambio de estado actual dentro de la lista recibida
        CambioEstadoES actual = null;
        if (cambiosEstado != null) {
            for (CambioEstadoES ce : cambiosEstado) {
                if (ce.getFechaHoraFin() == null) {
                    actual = ce;
                    break;
                }
            }
        }

        // 2. Finaliza el cambio de estado actual (si existe)
        if (actual != null) {
            actual.setFechaHoraFin(fh);
        }

        // 3. Crear el nuevo estado y el nuevo cambio de estado
        EstadoES nuevoEstado = new EnRevision();
        CambioEstadoES nuevoCambio = new CambioEstadoES(fh, resp, nuevoEstado);

        // 4. Actualizar el evento sísmico (contexto)
        es.agregarCE(nuevoCambio);
        es.setEstado(nuevoEstado);
    }


    public void rechazar(EventoSismico es, LocalDateTime fh, Empleado resp, List<CambioEstadoES> cambiosEstado) {
        // 1. Buscar el cambio de estado actual dentro de la lista recibida
        CambioEstadoES actual = null;
        if (cambiosEstado != null) {
            for (CambioEstadoES ce : cambiosEstado) {
                if (ce.getFechaHoraFin() == null) {
                    actual = ce;
                    break;
                }
            }
        }

        // 2. Finaliza el cambio de estado actual (si existe)
        if (actual != null) {
            actual.setFechaHoraFin(fh);
        }

        // 3. Crear el nuevo estado y el nuevo cambio de estado
        EstadoES nuevoEstado = new EnRevision();
        CambioEstadoES nuevoCambio = new CambioEstadoES(fh, resp, nuevoEstado);

        // 4. Actualizar el evento sísmico (contexto)
        es.agregarCE(nuevoCambio);
        es.setEstado(nuevoEstado);
    }
}
