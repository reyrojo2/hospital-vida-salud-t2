package pe.edu.cibertec.hospital.auditoria;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
@Component
public class RegistroAuditoriaConsumidor {
    private final RegistroAuditoriaRepositorio repositorio;
    private final R2dbcEntityTemplate template;
    public RegistroAuditoriaConsumidor(RegistroAuditoriaRepositorio r,R2dbcEntityTemplate template){repositorio=r;this.template=template;}
    @RabbitListener(queues="auditoria.citas")
    public Mono<Void> recibir(Map<String,String> evento) {
        String identificador=evento.get("eventoId");
        String tipo=evento.get("tipo");
        UUID recurso=UUID.fromString(evento.get("recursoId"));
        LocalDateTime fecha=LocalDateTime.parse(evento.get("fecha"));
        return repositorio.existsByEventoId(identificador)
             .flatMap(existe->existe?Mono.empty():template.insert(new RegistroAuditoria(UUID.randomUUID(),identificador,recurso,tipo,evento.get("estadoAnterior"),evento.get("estadoNuevo"),evento.get("origen"),fecha)).then())
             .onErrorResume(DataIntegrityViolationException.class,ex->Mono.empty())
             .then();
    }
}
