package entidades;

public class Estado {
    //Atributos
    private String nombreEstado;
    private String ambito;

    //Constructor new()
    public Estado(String nombreEstado, String ambito) {
        this.nombreEstado = nombreEstado;
        this.ambito = ambito;
    }
    //Getters y Setters
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
    public boolean soyPendienteDeRevision() {
        return this.nombreEstado == "PendienteDeRevision";
    }
    public boolean esEnRevision() {
        return this.nombreEstado == "EnRevision";
    }
    public boolean esRechazado() {
        return this.nombreEstado == "Rechazado";
    }
    public boolean esAmbitoEvento() {
        return this.ambito == "Evento";
    }

}
