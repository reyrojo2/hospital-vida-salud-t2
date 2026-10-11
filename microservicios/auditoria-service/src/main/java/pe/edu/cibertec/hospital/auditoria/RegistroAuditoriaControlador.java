package pe.edu.cibertec.hospital.auditoria;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
@RestController
@RequestMapping("/api/auditoria")
public class RegistroAuditoriaControlador {
    private final RegistroAuditoriaRepositorio repositorio;
    public RegistroAuditoriaControlador(RegistroAuditoriaRepositorio r){repositorio=r;}
    @GetMapping public Flux<RegistroAuditoria> listar(){return repositorio.findAll();}
}
