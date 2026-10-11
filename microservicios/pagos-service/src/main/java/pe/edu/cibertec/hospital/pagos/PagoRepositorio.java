package pe.edu.cibertec.hospital.pagos;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
public interface PagoRepositorio extends ReactiveCrudRepository<Pago,UUID> {
    Mono<Boolean> existsByNumeroOperacion(String numeroOperacion);
}
