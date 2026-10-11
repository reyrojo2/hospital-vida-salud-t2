package pe.edu.cibertec.hospital.citas;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
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
public class CitaServicio {
    private static final Map<String, Set<String>> TRANSICIONES=Map.of(
        "PENDIENTE",Set.of("CONFIRMADA","CANCELADA"),
        "CONFIRMADA",Set.of("ATENDIDA","AUSENTE","CANCELADA"),
        "ATENDIDA",Set.of(), "CANCELADA",Set.of(), "AUSENTE",Set.of());
    private final CitaRepositorio repositorio;
    private final PacienteCliente pacientes;
    private final CitaEventos eventos;
    private final R2dbcEntityTemplate template;
    public CitaServicio(CitaRepositorio repositorio, PacienteCliente pacientes, CitaEventos eventos, R2dbcEntityTemplate template) {
       this.repositorio=repositorio; this.pacientes=pacientes; this.eventos=eventos; this.template=template;
    }
    public Flux<Cita> listar() { return repositorio.findAll(); }
    public Mono<Cita> obtener(UUID id) { return repositorio.findById(id).switchIfEmpty(Mono.error(
        new ResponseStatusException(HttpStatus.NOT_FOUND,"Cita inexistente"))); }
    public Mono<Cita> crear(CitaPeticion p) {
        validar(p);
        return pacientes.verificarActivo(p.pacienteId())
            .then(repositorio.ocupado(p.profesionalCodigo().strip(),p.fechaHora()))
            .flatMap(ocupado -> ocupado ? Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,"Agenda ocupada")) :
                template.insert(new Cita(UUID.randomUUID(),p.pacienteId(),p.especialidad().strip(),p.fechaHora(),
                    p.profesionalCodigo().strip(),p.motivoAdministrativo().strip(),"PENDIENTE",
                    LocalDateTime.now(ZoneOffset.UTC),LocalDateTime.now(ZoneOffset.UTC))))
            .onErrorMap(DataIntegrityViolationException.class,ex->new ResponseStatusException(HttpStatus.CONFLICT,"Agenda ocupada",ex));
    }
    public Mono<Cita> cambiarEstado(UUID id, String nuevo) {
        if (nuevo==null || !TRANSICIONES.containsKey(nuevo))
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Estado invalido"));
        return obtener(id).flatMap(actual -> {
            if (!TRANSICIONES.get(actual.estado()).contains(nuevo))
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,"Transicion de estado no permitida"));
            if ("CONFIRMADA".equals(nuevo) && !actual.fechaHora().isAfter(LocalDateTime.now(ZoneOffset.UTC)))
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,"No se confirma una cita pasada"));
            Cita editada=new Cita(actual.id(),actual.pacienteId(),actual.especialidad(),actual.fechaHora(),
                actual.profesionalCodigo(),actual.motivoAdministrativo(),nuevo,actual.fechaCreacion(),LocalDateTime.now(ZoneOffset.UTC));
            return repositorio.save(editada).flatMap(guardada -> eventos.publicar(guardada,actual.estado()).thenReturn(guardada));
        });
    }
    private static void validar(CitaPeticion p) {
        if (p==null || p.pacienteId()==null || p.fechaHora()==null || vacio(p.especialidad()) ||
            vacio(p.profesionalCodigo()) || vacio(p.motivoAdministrativo()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Datos obligatorios de cita incompletos");
        if (!p.fechaHora().isAfter(LocalDateTime.now(ZoneOffset.UTC)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La cita debe programarse para una fecha futura (UTC)");
    }
    private static boolean vacio(String valor) { return valor==null||valor.isBlank(); }
}
