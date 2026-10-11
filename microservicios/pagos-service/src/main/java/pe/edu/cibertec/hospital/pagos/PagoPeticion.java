package pe.edu.cibertec.hospital.pagos;
import java.math.BigDecimal;
import java.util.UUID;
public record PagoPeticion(UUID pacienteId,String concepto,BigDecimal monto,String medioPago,String numeroOperacion) { }
