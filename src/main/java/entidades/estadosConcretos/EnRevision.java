package entidades.estadosConcretos;

import entidades.CambioEstadoES;
import entidades.Empleado;
import entidades.EventoSismico;
import entidades.estadoPadreAbstracto.EstadoES;

import java.time.LocalDateTime;

public class EnRevision extends EstadoES {

    public EnRevision() {
        super("EnRevision");
    }

    @Override
    public void confirmar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        es.validarExistencias(); // Lógica de negocio del CU

        // Lógica de transición
        CambioEstadoES actual = es.getCambioEstadoActual();
        actual.setFechaHoraFin(fh);

        EstadoES nuevoEstado = new Confirmado();
        CambioEstadoES nuevoCambio = new CambioEstadoES(fh, resp, nuevoEstado);

        es.agregarCE(nuevoCambio);
        es.setEstado(nuevoEstado);
    }

    @Override
    public void rechazar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        es.validarExistencias(); // Lógica de negocio del CU

        // Lógica específica de 'rechazar' del CU
        es.setFechaHoraRevision(fh);
        es.setResponsableRevision(resp);

        // Lógica de transición
        CambioEstadoES actual = es.getCambioEstadoActual();
        actual.setFechaHoraFin(fh);

        EstadoES nuevoEstado = new Rechazado();
        CambioEstadoES nuevoCambio = new CambioEstadoES(fh, resp, nuevoEstado);

        es.agregarCE(nuevoCambio);
        es.setEstado(nuevoEstado);
    }

    @Override
    public void derivar(EventoSismico es, LocalDateTime fh, Empleado resp) {
        es.validarExistencias(); // Lógica de negocio del CU

        // Lógica de transición
        CambioEstadoES actual = es.getCambioEstadoActual();
        actual.setFechaHoraFin(fh);

        EstadoES nuevoEstado = new Derivado();
        CambioEstadoES nuevoCambio = new CambioEstadoES(fh, resp, nuevoEstado);

        es.agregarCE(nuevoCambio);
        es.setEstado(nuevoEstado);
    }
}