package pe.edu.cibertec.hospital.pagos;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
class PagoServicioTest {
    private final PagoRepositorio repositorio=mock(PagoRepositorio.class);
    private final PagoPacienteCliente cliente=mock(PagoPacienteCliente.class);
    private final R2dbcEntityTemplate template=mock(R2dbcEntityTemplate.class);
    private final PagoServicio servicio=new PagoServicio(repositorio,cliente,template);
    @Test void rechazaMontoCeroONegativo(){
        for(BigDecimal monto:new BigDecimal[]{BigDecimal.ZERO,new BigDecimal("-10")}){
            StepVerifier.create(servicio.crear(new PagoPeticion(UUID.randomUUID(),"Prueba",monto,"EFECTIVO","TEST-OP")))
                .expectErrorSatisfies(ex->{assertInstanceOf(ResponseStatusException.class,ex);
                assertEquals(HttpStatus.BAD_REQUEST,((ResponseStatusException)ex).getStatusCode());}).verify();
        }
        verifyNoInteractions(cliente);
    }
    @Test void rechazaOperacionDuplicada(){
        UUID id=UUID.randomUUID();
        when(cliente.comprobar(id)).thenReturn(Mono.empty());
        when(repositorio.existsByNumeroOperacion("DEMO-01")).thenReturn(Mono.just(true));
        StepVerifier.create(servicio.crear(new PagoPeticion(id,"Tramite",new BigDecimal("25"),"TARJETA","DEMO-01")))
          .expectError(ResponseStatusException.class).verify();
        verify(template,never()).insert(any(Pago.class));
    }
}
