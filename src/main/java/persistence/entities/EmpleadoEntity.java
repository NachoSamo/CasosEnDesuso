package persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empleados")
public class EmpleadoEntity {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "usuario_id")
    private String usuarioId;

    @Column(name = "rol_empleado_id")
    private Integer rolEmpleadoId;

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public Integer getRolEmpleadoId() { return rolEmpleadoId; }
    public void setRolEmpleadoId(Integer rolEmpleadoId) { this.rolEmpleadoId = rolEmpleadoId; }
}
