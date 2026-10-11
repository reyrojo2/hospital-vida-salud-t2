package pe.edu.cibertec.hospital.citas;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("citas")
public record Cita(@Id UUID id, @Column("paciente_id") UUID pacienteId,
    String especialidad, @Column("fecha_hora") LocalDateTime fechaHora,
    @Column("profesional_codigo") String profesionalCodigo,
    @Column("motivo_administrativo") String motivoAdministrativo, String estado,
    @Column("fecha_creacion") LocalDateTime fechaCreacion,
    @Column("fecha_actualizacion") LocalDateTime fechaActualizacion) { }
