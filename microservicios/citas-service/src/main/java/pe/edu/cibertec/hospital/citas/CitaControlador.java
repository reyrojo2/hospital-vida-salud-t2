package pe.edu.cibertec.hospital.citas;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/citas")
public class CitaControlador {
    private final CitaServicio servicio;
    public CitaControlador(CitaServicio servicio) {this.servicio=servicio;}
    @GetMapping public Flux<Cita> listar() {return servicio.listar();}
    @GetMapping("/{id}") public Mono<Cita> obtener(@PathVariable UUID id) {return servicio.obtener(id);}
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cita> crear(@RequestBody Mono<CitaPeticion> peticion) {return peticion.flatMap(servicio::crear);}
    @PatchMapping("/{id}/estado")
    public Mono<Cita> cambiar(@PathVariable UUID id, @RequestBody Mono<EstadoPeticion> peticion) {
       return peticion.flatMap(p->servicio.cambiarEstado(id,p.estado()));
    }
    public record EstadoPeticion(String estado) { }
}
