package entidades;

public class TipoDato {
    private String denominacion;
    private String nombreUnidadMedida;
    private double valorUmbral;

    // Constructor
    public TipoDato(String denominacion, String nombreUnidadMedida, double valorUmbral) {
        this.denominacion = denominacion;
        this.nombreUnidadMedida = nombreUnidadMedida;
        this.valorUmbral = valorUmbral;
    }
    // Getters y Setters
    public String getDenominacion() {
        return denominacion;
    }
    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }
    public String getNombreUnidadMedida() {
        return nombreUnidadMedida;
    }
    public void setNombreUnidadMedida(String nombreUnidadMedida) {
        this.nombreUnidadMedida = nombreUnidadMedida;
    }
    public double getValorUmbral() {
        return valorUmbral;
    }
    public void setValorUmbral(double valorUmbral) {
        this.valorUmbral = valorUmbral;
    }
    public String getDatos() {
        return "TipoDato{" +
                "denominacion='" + denominacion + '\'' +
                ", nombreUnidadMedida='" + nombreUnidadMedida + '\'' +
                ", valorUmbral=" + valorUmbral +
                '}';
    }
}
