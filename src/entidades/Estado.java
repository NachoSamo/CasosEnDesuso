package entidades;

public class Estado {
    // Atributos
    private String nombreEstado;
    private String ambito;

    // Constructor
    public Estado(String nombreEstado, String ambito) {
        this.nombreEstado = nombreEstado;
        this.ambito = ambito;
    }

    // Getters y Setters
    public String getNombre() {
        return nombreEstado;
    }

    public void setNombre(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    // Métodos de comparación de estado
    public boolean soyPendienteDeRevision() {
        return "PendienteDeRevision".equals(this.nombreEstado);
    }

    public boolean soyAutoDetectado() {
        return "AutoDetectado".equals(this.nombreEstado);
    }

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
