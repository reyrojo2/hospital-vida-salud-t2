package pe.edu.cibertec.hospital.pagos;
import java.time.Duration;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
@Component
public class PagoPacienteCliente {
    private final WebClient web;
    public PagoPacienteCliente(WebClient.Builder balanceadorPagos){web=balanceadorPagos.baseUrl("http://pacientes-service").build();}
    public Mono<Void> comprobar(UUID id){
        return web.get().uri("/api/pacientes/{id}",id).exchangeToMono(res->{
            if(res.statusCode().value()==404)return Mono.error(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Paciente inexistente"));
            if(res.statusCode().isError())return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Error en pacientes"));
            return res.bodyToMono(PacienteReferencia.class).flatMap(p->"ACTIVO".equals(p.estado())?Mono.<Void>empty():
                Mono.error(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Paciente inactivo")));
        }).onErrorMap(WebClientRequestException.class,ex->new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Pacientes no disponible",ex))
          .timeout(Duration.ofSeconds(3),Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Tiempo de espera agotado")));
    }
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)
    public record PacienteReferencia(String estado) { }
}
