package pe.edu.cibertec.hospital.pacientes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class PacienteServicio {
    private final PacienteRepositorio repositorio;
    private final R2dbcEntityTemplate template;
    public PacienteServicio(PacienteRepositorio repositorio,R2dbcEntityTemplate template) { this.repositorio = repositorio; this.template=template; }
    public Flux<Paciente> listar() { return repositorio.findAll(); }
    public Mono<Paciente> obtener(UUID id) { return repositorio.findById(id).switchIfEmpty(
        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"))); }
    public Mono<Paciente> crear(PacientePeticion p) {
        validar(p);
        String tipo=p.tipoDocumento().strip().toUpperCase(Locale.ROOT);
        String numero=p.numeroDocumento().strip();
        return repositorio.existsByTipoDocumentoAndNumeroDocumento(tipo, numero)
            .flatMap(existe -> existe ? Mono.error(duplicado()) : template.insert(new Paciente(
                UUID.randomUUID(), tipo, numero, p.nombres().strip(), p.apellidos().strip(),
                p.fechaNacimiento(), p.telefono().strip(), p.correo().strip(), p.direccion(),
                LocalDateTime.now(ZoneOffset.UTC), "ACTIVO")))
            .onErrorMap(DataIntegrityViolationException.class, error -> duplicado());
    }
    public Mono<Paciente> actualizar(UUID id, PacientePeticion p) {
        validar(p);
        return obtener(id).flatMap(anterior -> repositorio.save(new Paciente(id,
            p.tipoDocumento().strip().toUpperCase(Locale.ROOT), p.numeroDocumento().strip(),
            p.nombres().strip(), p.apellidos().strip(), p.fechaNacimiento(), p.telefono().strip(),
            p.correo().strip(), p.direccion(), anterior.fechaRegistro(), anterior.estado())))
            .onErrorMap(DataIntegrityViolationException.class, error -> duplicado());
    }
    public Mono<Paciente> estado(UUID id, String nuevo) {
        if (nuevo == null || !(nuevo.equals("ACTIVO") || nuevo.equals("INACTIVO")))
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de paciente invalido"));
        return obtener(id).flatMap(p -> repositorio.save(new Paciente(p.id(), p.tipoDocumento(),
            p.numeroDocumento(), p.nombres(), p.apellidos(), p.fechaNacimiento(), p.telefono(),
            p.correo(), p.direccion(), p.fechaRegistro(), nuevo)));
    }
    private static ResponseStatusException duplicado() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Documento de paciente duplicado");
    }
    private static void validar(PacientePeticion p) {
        if (p == null || vacio(p.tipoDocumento()) || vacio(p.numeroDocumento()) ||
            vacio(p.nombres()) || vacio(p.apellidos()) || p.fechaNacimiento()==null ||
            vacio(p.telefono()) || vacio(p.correo()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos obligatorios de paciente incompletos");
        if (p.fechaNacimiento().isAfter(LocalDate.now(ZoneOffset.UTC)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha de nacimiento futura no permitida");
        if (!p.correo().contains("@"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo invalido");
    }
    private static boolean vacio(String valor) { return valor == null || valor.isBlank(); }
}
