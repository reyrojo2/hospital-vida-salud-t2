package pe.edu.cibertec.hospital.auditoria;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
public interface RegistroAuditoriaRepositorio extends ReactiveCrudRepository<RegistroAuditoria,UUID> {
    Mono<Boolean> existsByEventoId(String eventoId);
}
