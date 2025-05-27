package entidades;

public class DetalleMuestraSismica {
    private int valor;
    private TipoDato tipoDato;

    // Constructor
    public DetalleMuestraSismica(int valor, TipoDato tipoDato) {
        this.valor = valor;
        this.tipoDato = tipoDato;
    }
    // Getters y Setters
    public int getValor() {
        return valor;
    }
    public void setValor(int valor) {
        this.valor = valor;
    }
    public TipoDato getTipoDato() {
        return tipoDato;
    }
    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }
    public String getDatos() {
        return "DetalleMuestraSismica{" +
                "valor=" + valor +
                ", tipoDato=" + tipoDato.getDatos() +
                '}';
    }
}
