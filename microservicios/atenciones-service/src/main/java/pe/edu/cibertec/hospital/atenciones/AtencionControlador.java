package pe.edu.cibertec.hospital.atenciones;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/atenciones")
public class AtencionControlador {
    private final AtencionServicio servicio;
    public AtencionControlador(AtencionServicio s){servicio=s;}
    @GetMapping public Flux<Atencion> listar(){return servicio.listar();}
    @GetMapping("/{id}") public Mono<Atencion> obtener(@PathVariable UUID id){return servicio.obtener(id);}
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Mono<Atencion> crear(@RequestBody Mono<AtencionPeticion> peticion){return peticion.flatMap(servicio::crear);}
}
