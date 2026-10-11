package pe.edu.cibertec.hospital.atenciones;
import java.util.UUID;
public record AtencionPeticion(UUID pacienteId, UUID citaId, String area) { }
