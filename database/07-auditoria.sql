CREATE TABLE IF NOT EXISTS auditoria_eventos (
 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 evento_id VARCHAR(50) NOT NULL UNIQUE,
 recurso_id UUID NOT NULL,
 tipo_evento VARCHAR(60) NOT NULL,
 estado_anterior VARCHAR(20) NOT NULL,
 estado_nuevo VARCHAR(20) NOT NULL,
 origen VARCHAR(80) NOT NULL,
 fecha_evento TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_auditoria_recurso ON auditoria_eventos(recurso_id);
