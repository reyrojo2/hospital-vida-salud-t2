package pe.edu.cibertec.hospital.atenciones;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("atenciones")
public record Atencion(@Id UUID id, @Column("paciente_id") UUID pacienteId,
  @Column("cita_id") UUID citaId, @Column("fecha_atencion") LocalDateTime fechaAtencion,
  String area, String estado) { }
