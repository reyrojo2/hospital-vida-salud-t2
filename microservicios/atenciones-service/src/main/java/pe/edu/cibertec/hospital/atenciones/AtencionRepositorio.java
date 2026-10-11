package pe.edu.cibertec.hospital.atenciones;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
public interface AtencionRepositorio extends ReactiveCrudRepository<Atencion,UUID> {
   Mono<Boolean> existsByCitaId(UUID citaId);
}
