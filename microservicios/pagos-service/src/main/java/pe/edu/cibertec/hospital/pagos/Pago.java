package pe.edu.cibertec.hospital.pagos;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("pagos")
public record Pago(@Id UUID id,@Column("paciente_id") UUID pacienteId,String concepto,
  BigDecimal monto,@Column("fecha_pago") LocalDateTime fechaPago,
  @Column("medio_pago") String medioPago,@Column("numero_operacion") String numeroOperacion,
  String estado) { }
