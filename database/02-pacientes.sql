CREATE TABLE IF NOT EXISTS pacientes (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tipo_documento VARCHAR(20) NOT NULL,
  numero_documento VARCHAR(25) NOT NULL,
  nombres VARCHAR(120) NOT NULL,
  apellidos VARCHAR(120) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  telefono VARCHAR(30) NOT NULL,
  correo VARCHAR(160) NOT NULL,
  direccion VARCHAR(250),
  fecha_registro TIMESTAMP NOT NULL,
  estado VARCHAR(10) NOT NULL CHECK (estado IN ('ACTIVO','INACTIVO')),
  CONSTRAINT uk_documento_paciente UNIQUE (tipo_documento, numero_documento)
);
CREATE INDEX IF NOT EXISTS idx_paciente_apellidos ON pacientes (apellidos);
