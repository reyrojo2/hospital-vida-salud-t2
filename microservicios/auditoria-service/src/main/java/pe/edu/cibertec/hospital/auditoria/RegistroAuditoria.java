package pe.edu.cibertec.hospital.auditoria;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("auditoria_eventos")
public record RegistroAuditoria(@Id UUID id,@Column("evento_id") String eventoId,
    @Column("recurso_id") UUID recursoId,@Column("tipo_evento") String tipoEvento,
    @Column("estado_anterior") String estadoAnterior,@Column("estado_nuevo") String estadoNuevo,String origen,@Column("fecha_evento") LocalDateTime fechaEvento) { }
