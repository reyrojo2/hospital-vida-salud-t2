package pe.edu.cibertec.hospital.atenciones;
import java.time.Duration;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
@Component
public class CitaCliente {
    private final WebClient web;
    public CitaCliente(WebClient.Builder balanceadorAtenciones) {this.web=balanceadorAtenciones.baseUrl("http://citas-service").build();}
    public Mono<Void> validar(UUID citaId,UUID pacienteId) {
        return web.get().uri("/api/citas/{id}",citaId).exchangeToMono(res -> {
            if (res.statusCode().value()==404) return Mono.error(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Cita no encontrada"));
            if (res.statusCode().isError()) return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Error en servicio de citas"));
            return res.bodyToMono(CitaReferencia.class).flatMap(c -> {
                if (!pacienteId.equals(c.pacienteId())) return Mono.error(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Paciente y cita no corresponden"));
                if (!"CONFIRMADA".equals(c.estado())) return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,"Cita debe estar confirmada"));
                return Mono.<Void>empty();
            });
        }).onErrorMap(WebClientRequestException.class,ex->new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Servicio citas no disponible",ex))
          .timeout(Duration.ofSeconds(3),Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Tiempo de espera agotado")));
    }
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)
    public record CitaReferencia(UUID pacienteId,String estado) { }
}
