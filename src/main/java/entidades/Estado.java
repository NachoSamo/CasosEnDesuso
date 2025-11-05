package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estado {
    // Atributos
    private String nombreEstado;
    private String ambito;

    // Métodos de comparación de estado
    public boolean soyPendienteDeRevision() {
        return "PendienteDeRevision".equals(this.nombreEstado);
    }

    public boolean soyAutoDetectado() {
        return "AutoDetectado".equals(this.nombreEstado);
    }

    // chequea segun maquina de estados
    public boolean soySinRevisar() {
        return this.soyPendienteDeRevision() || this.soyAutoDetectado();
    }

    public boolean esEnRevision() {
        return "EnRevision".equals(this.nombreEstado);
    }

    public boolean esRechazado() {
        return "Rechazado".equals(this.nombreEstado);
    }

    public boolean esAmbitoEvento() {
        return "Evento".equals(this.ambito);
    }
}
