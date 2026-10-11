package pe.edu.cibertec.hospital.citas;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
class CitaServicioTest {
    private final CitaRepositorio repositorio=mock(CitaRepositorio.class);
    private final PacienteCliente pacientes=mock(PacienteCliente.class);
    private final CitaEventos eventos=mock(CitaEventos.class);
    private final R2dbcEntityTemplate template=mock(R2dbcEntityTemplate.class);
    private final CitaServicio servicio=new CitaServicio(repositorio,pacientes,eventos,template);
    private Cita cita(String estado){return new Cita(UUID.randomUUID(),UUID.randomUUID(),"Consulta",LocalDateTime.now().plusDays(3),"AGENDA-001","Control administrativo",estado,LocalDateTime.now(),LocalDateTime.now());}
    @Test void creaCitaParaPacienteValido(){
        UUID id=UUID.randomUUID();
        CitaPeticion p=new CitaPeticion(id,"General",LocalDateTime.now().plusDays(2),"P-001","Consulta de prueba");
        when(pacientes.verificarActivo(id)).thenReturn(Mono.empty());
        when(repositorio.ocupado(eq("P-001"),any(LocalDateTime.class))).thenReturn(Mono.just(false));
        when(template.insert(any(Cita.class))).thenAnswer(inv->Mono.just(inv.getArgument(0)));
        StepVerifier.create(servicio.crear(p)).assertNext(c->assertEquals("PENDIENTE",c.estado())).verifyComplete();
    }
    @Test void rechazaAgendaDuplicada(){
        UUID id=UUID.randomUUID();
        CitaPeticion p=new CitaPeticion(id,"General",LocalDateTime.now().plusDays(2),"P-001","Prueba");
        when(pacientes.verificarActivo(id)).thenReturn(Mono.empty());
        when(repositorio.ocupado(eq("P-001"),any(LocalDateTime.class))).thenReturn(Mono.just(true));
        StepVerifier.create(servicio.crear(p)).expectError(ResponseStatusException.class).verify();
        verify(template,never()).insert(any(Cita.class));
    }
    @Test void noConfirmaCancelada(){
        Cita cancelada=cita("CANCELADA");
        when(repositorio.findById(cancelada.id())).thenReturn(Mono.just(cancelada));
        StepVerifier.create(servicio.cambiarEstado(cancelada.id(),"CONFIRMADA"))
          .expectErrorSatisfies(ex->{assertInstanceOf(ResponseStatusException.class,ex);
            assertEquals(HttpStatus.CONFLICT,((ResponseStatusException)ex).getStatusCode());}).verify();
        verify(eventos,never()).publicar(any(),anyString());
    }
    @Test void confirmaYPidePublicarEvento(){
        Cita pendiente=cita("PENDIENTE");
        when(repositorio.findById(pendiente.id())).thenReturn(Mono.just(pendiente));
        when(repositorio.save(any(Cita.class))).thenAnswer(inv->Mono.just(inv.getArgument(0)));
        when(eventos.publicar(any(Cita.class),eq("PENDIENTE"))).thenReturn(Mono.empty());
        StepVerifier.create(servicio.cambiarEstado(pendiente.id(),"CONFIRMADA"))
          .assertNext(c->assertEquals("CONFIRMADA",c.estado())).verifyComplete();
        verify(eventos).publicar(any(Cita.class),eq("PENDIENTE"));
    }
}
