package pe.edu.cibertec.hospital.atenciones;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class AtencionServicio {
    private final AtencionRepositorio repositorio;
    private final CitaCliente citas;
    private final R2dbcEntityTemplate template;
    public AtencionServicio(AtencionRepositorio r,CitaCliente c,R2dbcEntityTemplate template){repositorio=r;citas=c;this.template=template;}
    public Flux<Atencion> listar(){return repositorio.findAll();}
    public Mono<Atencion> obtener(UUID id){return repositorio.findById(id).switchIfEmpty(
        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Atencion inexistente")));}
    public Mono<Atencion> crear(AtencionPeticion p){
        if(p==null||p.citaId()==null||p.pacienteId()==null||p.area()==null||p.area().isBlank())
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Datos obligatorios de atencion incompletos"));
        return citas.validar(p.citaId(),p.pacienteId()).then(repositorio.existsByCitaId(p.citaId()))
            .flatMap(existe->existe?Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,"Atencion duplicada para esta cita")):
                template.insert(new Atencion(UUID.randomUUID(),p.pacienteId(),p.citaId(),LocalDateTime.now(ZoneOffset.UTC),p.area().strip(),"REGISTRADA")))
            .onErrorMap(DataIntegrityViolationException.class,ex->new ResponseStatusException(HttpStatus.CONFLICT,"Atencion duplicada",ex));
    }
}
