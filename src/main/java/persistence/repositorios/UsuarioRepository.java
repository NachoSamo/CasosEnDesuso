package persistence.repositorios;

import entidades.Usuario;
import java.util.Optional;

public interface UsuarioRepository {

    void save(Usuario usuario);

    Optional<Usuario> findByUsername(String username);
}