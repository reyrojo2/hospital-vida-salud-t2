package pe.edu.cibertec.hospital.pacientes;
import java.time.LocalDate;
public record PacientePeticion(String tipoDocumento, String numeroDocumento, String nombres,
   String apellidos, LocalDate fechaNacimiento, String telefono, String correo, String direccion) { }
