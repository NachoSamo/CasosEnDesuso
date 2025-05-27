package entidades;

public class Empleado {
    private String nombre;
    private String apellido;
    private String mail;
    private String telefono;
    private Usuario usuario;

    // Constructor
    public Empleado(String nombre, String apellido, String mail, String telefono, Usuario usuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.telefono = telefono;
        this.usuario = usuario;
    }  
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }   
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Boolean esTuUsuario(Usuario usuario) {
        return this.usuario.getUsuario().equals(usuario);// el .username es para acceder al atributo username de Usuario y .getUsername() es para acceder al valor del atributo username
    }
}
