package persistence.repositorios;

import entidades.Empleado;
import java.util.Optional;

public interface EmpleadoRepository {

    void save(Empleado empleado);

    Optional<Empleado> findByUsername(String username);
}