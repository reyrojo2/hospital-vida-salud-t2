CREATE TABLE IF NOT EXISTS citas (
 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 paciente_id UUID NOT NULL,
 especialidad VARCHAR(120) NOT NULL,
 fecha_hora TIMESTAMP NOT NULL,
 profesional_codigo VARCHAR(40) NOT NULL,
 motivo_administrativo VARCHAR(250) NOT NULL,
 estado VARCHAR(12) NOT NULL CHECK (estado IN ('PENDIENTE','CONFIRMADA','ATENDIDA','CANCELADA','AUSENTE')),
 fecha_creacion TIMESTAMP NOT NULL,
 fecha_actualizacion TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_cita_agenda_vigente
 ON citas (profesional_codigo, fecha_hora)
 WHERE estado NOT IN ('CANCELADA','AUSENTE');
CREATE INDEX IF NOT EXISTS idx_cita_paciente ON citas (paciente_id);
