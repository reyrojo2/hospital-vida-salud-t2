package pe.edu.cibertec.hospital.citas;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;
public interface CitaRepositorio extends ReactiveCrudRepository<Cita,UUID> {
    @Query("SELECT EXISTS (SELECT 1 FROM citas WHERE profesional_codigo = :codigo AND fecha_hora = :fecha AND estado NOT IN ('CANCELADA', 'AUSENTE'))")
    Mono<Boolean> ocupado(@Param("codigo") String codigo, @Param("fecha") LocalDateTime fecha);
}
