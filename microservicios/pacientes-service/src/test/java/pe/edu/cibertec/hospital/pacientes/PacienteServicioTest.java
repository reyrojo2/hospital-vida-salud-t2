package pe.edu.cibertec.hospital.pacientes;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
class PacienteServicioTest {
    private final PacienteRepositorio repositorio=mock(PacienteRepositorio.class);
    private final R2dbcEntityTemplate template=mock(R2dbcEntityTemplate.class);
    private final PacienteServicio servicio=new PacienteServicio(repositorio,template);
    private final PacientePeticion valido=new PacientePeticion("DNI","00000001","Ana","Ejemplo",LocalDate.of(1999,1,1),"900000001","ana@example.test",null);
    @Test void registraPacienteValido(){
        when(repositorio.existsByTipoDocumentoAndNumeroDocumento("DNI","00000001")).thenReturn(Mono.just(false));
        when(template.insert(any(Paciente.class))).thenAnswer(inv->Mono.just(inv.getArgument(0)));
        StepVerifier.create(servicio.crear(valido)).assertNext(p->{
            assertEquals("ACTIVO",p.estado());assertEquals("00000001",p.numeroDocumento());
        }).verifyComplete();
        verify(template).insert(any(Paciente.class));
    }
    @Test void rechazaDocumentoDuplicado(){
        when(repositorio.existsByTipoDocumentoAndNumeroDocumento("DNI","00000001")).thenReturn(Mono.just(true));
        StepVerifier.create(servicio.crear(valido)).expectErrorSatisfies(ex->{
            assertInstanceOf(ResponseStatusException.class,ex);
            assertEquals(HttpStatus.CONFLICT,((ResponseStatusException)ex).getStatusCode());
        }).verify();
        verify(template,never()).insert(any(Paciente.class));
    }
    @Test void recursoInexistente(){
        when(repositorio.findById(any(UUID.class))).thenReturn(Mono.empty());
        StepVerifier.create(servicio.obtener(UUID.randomUUID())).expectErrorSatisfies(ex->{
            assertInstanceOf(ResponseStatusException.class,ex);
            assertEquals(HttpStatus.NOT_FOUND,((ResponseStatusException)ex).getStatusCode());
        }).verify();
    }
}
