package pe.edu.cibertec.hospital.citas;
import java.util.UUID;
import java.time.LocalDateTime;
public record CitaPeticion(UUID pacienteId, String especialidad, LocalDateTime fechaHora,
    String profesionalCodigo, String motivoAdministrativo) { }
