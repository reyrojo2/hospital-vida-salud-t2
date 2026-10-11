package pe.edu.cibertec.hospital.pacientes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("pacientes")
public record Paciente(
    @Id UUID id,
    @Column("tipo_documento") String tipoDocumento,
    @Column("numero_documento") String numeroDocumento,
    String nombres,
    String apellidos,
    @Column("fecha_nacimiento") LocalDate fechaNacimiento,
    String telefono,
    String correo,
    String direccion,
    @Column("fecha_registro") LocalDateTime fechaRegistro,
    String estado) { }
