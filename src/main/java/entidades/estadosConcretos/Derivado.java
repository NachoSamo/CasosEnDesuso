package entidades.estadosConcretos;

import entidades.CambioEstadoES;
import entidades.Empleado;
import entidades.EventoSismico;
import entidades.estadoPadreAbstracto.EstadoES;

import java.time.LocalDateTime;

public class Derivado extends EstadoES {
    public Derivado() {
        super("Derivado");
    }

    @Override
    public void confirmar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        es.validarExistencias();

        CambioEstadoES actual = es.getCambioEstadoActual();
        actual.setFechaHoraFin(fh);

        EstadoES nuevoEstado = new Confirmado();
        CambioEstadoES nuevoCambio = new CambioEstadoES(fh, resp, nuevoEstado);

        es.agregarCE(nuevoCambio);
        es.setEstado(nuevoEstado);
    }

    @Override
    public void rechazar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        es.validarExistencias();

        es.setFechaHoraRevision(fh);
        es.setResponsableRevision(resp);

        CambioEstadoES actual = es.getCambioEstadoActual();
        actual.setFechaHoraFin(fh);

        EstadoES nuevoEstado = new Rechazado();
        CambioEstadoES nuevoCambio = new CambioEstadoES(fh, resp, nuevoEstado);

        es.agregarCE(nuevoCambio);
        es.setEstado(nuevoEstado);
    }
}