package pe.edu.cibertec.hospital.pacientes;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/pacientes")
public class PacienteControlador {
    private final PacienteServicio servicio;
    public PacienteControlador(PacienteServicio servicio) { this.servicio = servicio; }
    @GetMapping public Flux<Paciente> listar() { return servicio.listar(); }
    @GetMapping("/{id}") public Mono<Paciente> obtener(@PathVariable UUID id) { return servicio.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Mono<Paciente> crear(@RequestBody Mono<PacientePeticion> peticion) { return peticion.flatMap(servicio::crear); }
    @PutMapping("/{id}") public Mono<Paciente> actualizar(@PathVariable UUID id, @RequestBody Mono<PacientePeticion> peticion) {
        return peticion.flatMap(p -> servicio.actualizar(id,p));
    }
    @PatchMapping("/{id}/estado") public Mono<Paciente> estado(@PathVariable UUID id, @RequestBody Mono<EstadoPeticion> peticion) {
        return peticion.flatMap(p -> servicio.estado(id,p.estado()));
    }
    public record EstadoPeticion(String estado) { }
}
