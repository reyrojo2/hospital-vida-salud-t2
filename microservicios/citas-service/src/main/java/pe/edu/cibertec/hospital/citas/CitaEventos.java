package pe.edu.cibertec.hospital.citas;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
@Component
public class CitaEventos {
    private final RabbitTemplate rabbit;
    public CitaEventos(RabbitTemplate rabbit) { this.rabbit=rabbit; }
    public Mono<Void> publicar(Cita cita, String anterior) {
        return Mono.fromRunnable(() -> {
            Map<String,String> mensaje=Map.of(
                "eventoId",UUID.randomUUID().toString(), "tipo", "CONFIRMADA".equals(cita.estado())?"CitaConfirmada":"CitaEstadoActualizado",
                "recursoId",cita.id().toString(), "pacienteId",cita.pacienteId().toString(),
                "estadoAnterior",anterior, "estadoNuevo",cita.estado(),
                "origen","citas-service", "fecha",LocalDateTime.now(ZoneOffset.UTC).toString());
            rabbit.convertAndSend("hospital.eventos", "CONFIRMADA".equals(cita.estado())?"cita.confirmada":"cita.actualizada", new java.util.HashMap<>(mensaje));
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
