package pe.edu.cibertec.hospital.pacientes;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
class PacienteControladorTest {
    @Test void validaEstadoHttpCreado(){
        PacienteServicio servicio=mock(PacienteServicio.class);
        PacientePeticion peticion=new PacientePeticion("DNI","00000002","Luis","Demo",LocalDate.of(2000,1,1),"900000002","luis@example.test",null);
        when(servicio.crear(any(PacientePeticion.class))).thenReturn(Mono.just(new Paciente(null,"DNI","00000002","Luis","Demo",peticion.fechaNacimiento(),"900000002","luis@example.test",null,null,"ACTIVO")));
        WebTestClient.bindToController(new PacienteControlador(servicio)).build().post().uri("/api/pacientes")
            .bodyValue(peticion).exchange().expectStatus().isCreated()
            .expectBody().jsonPath("$.numeroDocumento").isEqualTo("00000002");
    }
}
