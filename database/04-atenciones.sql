CREATE TABLE IF NOT EXISTS atenciones (
 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 paciente_id UUID NOT NULL,
 cita_id UUID NOT NULL UNIQUE,
 fecha_atencion TIMESTAMP NOT NULL,
 area VARCHAR(120) NOT NULL,
 estado VARCHAR(15) NOT NULL CHECK (estado IN ('REGISTRADA'))
);
CREATE INDEX IF NOT EXISTS idx_atenciones_paciente ON atenciones(paciente_id);
