package persistence;

import entidades.estadoPadreAbstracto.EstadoES;
import entidades.estadosConcretos.*;

public class EstadoFactory {
    public static EstadoES fromCodigo(String codigo) {
        if (codigo == null) return new SinRevision();
        switch (codigo) {
            case "auto_detectado": return new AutoDetectado();
            case "bloqueado_en_revision": return new EnRevision();
            case "pendiente_revision": return new PendienteDeRevision();
            case "derivado": return new Derivado();
            case "confirmado": return new Confirmado();
            case "rechazado": return new Rechazado();
            case "pendiente_cierre": return new PendienteDeCierre();
            case "cerrado": return new Cerrado();
            case "sin_revision": return new SinRevision();
            default: return new SinRevision();
        }
    }
}
