package pe.edu.cibertec.hospital.citas;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
class CitaControladorTest {
    @Test void consultaCitaPorId(){
        UUID id=UUID.randomUUID();
        CitaServicio servicio=mock(CitaServicio.class);
        Cita cita=new Cita(id,UUID.randomUUID(),"Demo",LocalDateTime.now().plusDays(2),
            "P-001","Motivo de prueba","PENDIENTE",LocalDateTime.now(),LocalDateTime.now());
        when(servicio.obtener(id)).thenReturn(Mono.just(cita));
        WebTestClient.bindToController(new CitaControlador(servicio)).build()
            .get().uri("/api/citas/{id}",id).exchange().expectStatus().isOk()
            .expectBody().jsonPath("$.estado").isEqualTo("PENDIENTE");
    }
}
