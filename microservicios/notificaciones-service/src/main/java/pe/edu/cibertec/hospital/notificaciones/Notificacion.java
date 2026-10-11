package pe.edu.cibertec.hospital.notificaciones;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("notificaciones")
public record Notificacion(@Id UUID id,@Column("evento_id") String eventoId,
    @Column("recurso_id") UUID recursoId,@Column("tipo_evento") String tipoEvento,
    @Column("mensaje") String mensaje,@Column("fecha_evento") LocalDateTime fechaEvento) { }
