package pe.edu.cibertec.hospital.citas;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
@RestControllerAdvice
public class ErrorHttp {
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<Map<String,Object>>> estado(ResponseStatusException ex) {
        return Mono.just(ResponseEntity.status(ex.getStatusCode())
            .body(Map.of("codigo",ex.getStatusCode().value(),"error",ex.getReason()==null?"Error de solicitud":ex.getReason())));
    }
}
