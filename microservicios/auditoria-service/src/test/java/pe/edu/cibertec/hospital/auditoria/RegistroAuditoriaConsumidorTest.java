package pe.edu.cibertec.hospital.auditoria;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
class RegistroAuditoriaConsumidorTest {
    @Test void auditaTransicionDeEstado(){
        RegistroAuditoriaRepositorio repositorio=mock(RegistroAuditoriaRepositorio.class);
        R2dbcEntityTemplate template=mock(R2dbcEntityTemplate.class);
        when(repositorio.existsByEventoId("ev-02")).thenReturn(Mono.just(false));
        when(template.insert(any(RegistroAuditoria.class))).thenAnswer(inv->Mono.just(inv.getArgument(0)));
        Map<String,String> evento=Map.of("eventoId","ev-02","tipo","CitaConfirmada",
            "recursoId","11111111-1111-1111-1111-111111111111","pacienteId","22222222-2222-2222-2222-222222222222",
            "estadoAnterior","PENDIENTE","estadoNuevo","CONFIRMADA","origen","citas-service","fecha","2026-10-10T12:00:00");
        StepVerifier.create(new RegistroAuditoriaConsumidor(repositorio,template).recibir(evento)).verifyComplete();
        verify(template).insert(any(RegistroAuditoria.class));
    }
}
