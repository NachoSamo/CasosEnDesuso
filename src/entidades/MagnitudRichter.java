package entidades;

public class MagnitudRichter {
    private double numero;
    private String descripcionRichter;

    // Constructor
    public MagnitudRichter(double numero, String descripcionRichter) {
        this.numero = numero;
        this.descripcionRichter = descripcionRichter;
    }
    // Getters y Setters
    public double getNumero() {
        return numero;
    }
    public void setNumero(double numero) {
        this.numero = numero;
    }
    public String getDescripcionRichter() {
        return descripcionRichter;
    }
    public void setDescripcionRichter(String descripcionRichter) {
        this.descripcionRichter = descripcionRichter;
    }
}
