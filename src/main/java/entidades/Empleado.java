package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    private String nombre;
    private String apellido;
    private String mail;
    private String telefono;
    private Usuario usuario;

    public boolean esTuUsuario(Usuario usuario) {
        // Compara el usuario recibido con el objeto del atributo usuario
        return this.usuario.equals(usuario);
    }
}