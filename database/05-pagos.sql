CREATE TABLE IF NOT EXISTS pagos (
 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 paciente_id UUID NOT NULL,
 concepto VARCHAR(180) NOT NULL,
 monto NUMERIC(12,2) NOT NULL CHECK (monto > 0),
 fecha_pago TIMESTAMP NOT NULL,
 medio_pago VARCHAR(20) NOT NULL CHECK (medio_pago IN ('EFECTIVO','TARJETA','TRANSFERENCIA')),
 numero_operacion VARCHAR(70) NOT NULL UNIQUE,
 estado VARCHAR(15) NOT NULL CHECK (estado IN ('REGISTRADO'))
);
CREATE INDEX IF NOT EXISTS idx_pagos_paciente ON pagos (paciente_id);
