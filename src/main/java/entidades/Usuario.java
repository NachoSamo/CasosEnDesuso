package entidades;

import java.util.Objects;

public class Usuario {
    private String username;
    private String password;

    // Constructor
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ✅ Comparación por username
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario otro = (Usuario) obj;
        return Objects.equals(username, otro.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
