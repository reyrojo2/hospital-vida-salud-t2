package pe.edu.cibertec.hospital.pagos;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/pagos")
public class PagoControlador {
    private final PagoServicio servicio;
    public PagoControlador(PagoServicio s){servicio=s;}
    @GetMapping public Flux<Pago> listar(){return servicio.listar();}
    @GetMapping("/{id}") public Mono<Pago> obtener(@PathVariable UUID id){return servicio.obtener(id);}
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pago> crear(@RequestBody Mono<PagoPeticion> peticion){return peticion.flatMap(servicio::crear);}
}
