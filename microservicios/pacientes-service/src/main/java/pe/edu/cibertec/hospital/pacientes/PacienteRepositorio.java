package pe.edu.cibertec.hospital.pacientes;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
public interface PacienteRepositorio extends ReactiveCrudRepository<Paciente, UUID> {
    Mono<Boolean> existsByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
    Mono<Paciente> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
}
