package pe.edu.cibertec.hospital.notificaciones;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionControlador {
    private final NotificacionRepositorio repositorio;
    public NotificacionControlador(NotificacionRepositorio r){repositorio=r;}
    @GetMapping public Flux<Notificacion> listar(){return repositorio.findAll();}
}
