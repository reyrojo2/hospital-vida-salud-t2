package pe.edu.cibertec.hospital.notificaciones;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
public interface NotificacionRepositorio extends ReactiveCrudRepository<Notificacion,UUID> {
    Mono<Boolean> existsByEventoId(String eventoId);
}
