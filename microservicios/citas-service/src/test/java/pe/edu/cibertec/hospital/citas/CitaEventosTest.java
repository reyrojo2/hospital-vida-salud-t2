package pe.edu.cibertec.hospital.citas;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.test.StepVerifier;
class CitaEventosTest {
    @Test void publicaEnExchangeYRoutingKeyCorrectos(){
        RabbitTemplate rabbit=mock(RabbitTemplate.class);
        Cita cita=new Cita(UUID.randomUUID(),UUID.randomUUID(),"General",LocalDateTime.now().plusDays(2),"DOC-1","Prueba","CONFIRMADA",LocalDateTime.now(),LocalDateTime.now());
        StepVerifier.create(new CitaEventos(rabbit).publicar(cita,"PENDIENTE")).verifyComplete();
        verify(rabbit).convertAndSend(eq("hospital.eventos"),eq("cita.confirmada"),any(Map.class));
    }
}
