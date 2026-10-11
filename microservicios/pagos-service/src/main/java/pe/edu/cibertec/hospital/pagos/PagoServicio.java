package pe.edu.cibertec.hospital.pagos;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class PagoServicio {
    private static final Set<String> MEDIOS=Set.of("EFECTIVO","TARJETA","TRANSFERENCIA");
    private final PagoRepositorio repositorio;
    private final PagoPacienteCliente pacientes;
    private final R2dbcEntityTemplate template;
    public PagoServicio(PagoRepositorio r,PagoPacienteCliente p,R2dbcEntityTemplate template){repositorio=r;pacientes=p;this.template=template;}
    public Flux<Pago> listar(){return repositorio.findAll();}
    public Mono<Pago> obtener(UUID id){return repositorio.findById(id).switchIfEmpty(
        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Pago inexistente")));}
    public Mono<Pago> crear(PagoPeticion p){
        if(p==null||p.pacienteId()==null||p.monto()==null||p.monto().compareTo(BigDecimal.ZERO)<=0||
           p.concepto()==null||p.concepto().isBlank()||p.numeroOperacion()==null||p.numeroOperacion().isBlank()||
           p.medioPago()==null||!MEDIOS.contains(p.medioPago()))
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pago invalido: monto positivo, concepto, medio y operacion obligatorios"));
        String operacion=p.numeroOperacion().strip();
        return pacientes.comprobar(p.pacienteId()).then(repositorio.existsByNumeroOperacion(operacion))
            .flatMap(existe->existe?Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,"Numero de operacion duplicado")):
                template.insert(new Pago(UUID.randomUUID(),p.pacienteId(),p.concepto().strip(),p.monto(),
                     LocalDateTime.now(ZoneOffset.UTC),p.medioPago(),operacion,"REGISTRADO")))
            .onErrorMap(DataIntegrityViolationException.class,ex->new ResponseStatusException(HttpStatus.CONFLICT,"Numero de operacion duplicado",ex));
    }
}
