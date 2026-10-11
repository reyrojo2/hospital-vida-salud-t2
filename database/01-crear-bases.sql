-- Ejecutar desde psql conectado a la base postgres con permisos CREATEDB.
-- Ningun microservicio accede a las tablas privadas de otro servicio.
CREATE DATABASE hospital_pacientes;
CREATE DATABASE hospital_citas;
CREATE DATABASE hospital_atenciones;
CREATE DATABASE hospital_pagos;
CREATE DATABASE hospital_notificaciones;
CREATE DATABASE hospital_auditoria;
