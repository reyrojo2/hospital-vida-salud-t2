CREATE TABLE IF NOT EXISTS notificaciones (
 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 evento_id VARCHAR(50) NOT NULL UNIQUE,
 recurso_id UUID NOT NULL,
 tipo_evento VARCHAR(60) NOT NULL,
 mensaje VARCHAR(250) NOT NULL,
 fecha_evento TIMESTAMP NOT NULL
);
