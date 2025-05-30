package entidades;

public class ClasificacionSismo {
    private String nombre;
    private double kmProfundidadDesde;
    private double kmProfundidadHasta;

    // Constructor new()
    public ClasificacionSismo(String nombre, double kmProfundidadDesde, double kmProfundidadHasta) {
        this.nombre = nombre;
        this.kmProfundidadDesde = kmProfundidadDesde;
        this.kmProfundidadHasta = kmProfundidadHasta;
    }
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public double getKmProfundidadDesde() {
        return kmProfundidadDesde;
    }
    public void setKmProfundidadDesde(double kmProfundidadDesde) {
        this.kmProfundidadDesde = kmProfundidadDesde;
    }
    public double getKmProfundidadHasta() {
        return kmProfundidadHasta;
    }
    public void setKmProfundidadHasta(double kmProfundidadHasta) {
        this.kmProfundidadHasta = kmProfundidadHasta;
    }
}
