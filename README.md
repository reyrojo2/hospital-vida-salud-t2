# Hospital Vida Salud — Segunda Evaluación T2

**Cibertec | Desarrollo de Aplicaciones Web II | 2026**

Aplicación administrativa ficticia basada en microservicios, diseñada para demostrar Spring Boot, Spring WebFlux, `Mono`, `Flux`, PostgreSQL/R2DBC, Spring Cloud Gateway, Eureka, RabbitMQ, Maven, Git y GitHub Actions.

**No es una plataforma clínica.** Todos los ejemplos son inventados; no almacenes datos reales ni secretos en GitHub.

## 1. Alcance

Implementado: **Pacientes, Citas, Atenciones administrativas, Pagos simulados, Notificaciones y Auditoría**, más Gateway y Eureka. Se excluyen Farmacia, Hospitalización y Laboratorio por ser procesos referenciales/opcionales para el flujo escogido. El documento técnico justifica la delimitación.

## 2. Requisitos locales

- JDK **21** y Maven **3.9 o compatible** (puede ser una instalación portable, `mvn -version`).
- PostgreSQL **13 o superior** (`gen_random_uuid()` nativo), preferentemente PostgreSQL 18.
- RabbitMQ Server con plugin de administración opcional (puerto AMQP 5672, interfaz administrativa habitual 15672).
- Una terminal PowerShell para cada aplicación Java.
- Git para publicar cambios en la rama de trabajo.

> Docker **no es obligatorio**. Si no lo tienes o tu PC requiere permisos de administrador, usa PostgreSQL y RabbitMQ instalados localmente.

## 3. Preparar bases de datos

Con la herramienta `psql` y un usuario autorizado a crear bases:

```powershell
psql -U postgres -d postgres -f database/01-crear-bases.sql
psql -U postgres -d hospital_pacientes -f database/02-pacientes.sql
psql -U postgres -d hospital_citas -f database/03-citas.sql
psql -U postgres -d hospital_atenciones -f database/04-atenciones.sql
psql -U postgres -d hospital_pagos -f database/05-pagos.sql
psql -U postgres -d hospital_notificaciones -f database/06-notificaciones.sql
psql -U postgres -d hospital_auditoria -f database/07-auditoria.sql
```

Las bases se crean **una sola vez**; si ya existen, no vuelvas a ejecutar `01-crear-bases.sql`. Cada servicio solo conoce su base, sin claves foráneas SQL entre microservicios. El usuario SQL que configures necesita permisos sobre las tablas.

## 4. Configurar variables de entorno (PowerShell)

**Estas variables solo se asignan en tu computadora: no las subas a GitHub.** Sustituye los valores de ejemplo por los que configuraste localmente.

```powershell
$env:DB_USER = "postgres"
$env:DB_PASSWORD = Read-Host "Clave local de PostgreSQL"
$env:RABBIT_HOST = "localhost"
$env:RABBIT_PORT = "5672"
$env:RABBIT_USER = Read-Host "Usuario RabbitMQ"
$env:RABBIT_PASSWORD = Read-Host "Clave RabbitMQ"
$env:EUREKA_URL = "http://localhost:8761/eureka/"
```

`Read-Host` permite introducir la clave sin escribirla en un archivo. Por seguridad no compartas la terminal ni su historial. Configura estas variables **en cada ventana** PowerShell donde levantes un servicio; también puedes usar variables de entorno de usuario del sistema para evitar repetirlas. Si PostgreSQL local usa `trust`, aun así asigna un valor apropiado para `DB_PASSWORD` según tu configuración.

Si ejecutas los servicios desde un IDE, configura estas variables en cada *Run Configuration*. Cada proyecto incluye `application.yml` con un `R2DBC_URL` predeterminado exclusivo de su base; puede sobreescribirse por entorno si la instalación de PostgreSQL es diferente.

## 5. Ejecutar y probar

Desde la **raíz** del repositorio:

```powershell
mvn --batch-mode --no-transfer-progress clean verify
```

Las pruebas de CI usan repositorios y componentes simulados, así que **no necesitan** PostgreSQL, Eureka ni RabbitMQ instalados para correr. Para ejecutar la demostración manual, sí se necesitan los servicios reales.

Con PostgreSQL y RabbitMQ encendidos, en ventanas diferentes ejecuta, **en este orden**:

```powershell
mvn -pl infraestructura/eureka-server spring-boot:run
mvn -pl microservicios/pacientes-service spring-boot:run
mvn -pl microservicios/citas-service spring-boot:run
mvn -pl microservicios/atenciones-service spring-boot:run
mvn -pl microservicios/pagos-service spring-boot:run
mvn -pl microservicios/notificaciones-service spring-boot:run
mvn -pl microservicios/auditoria-service spring-boot:run
mvn -pl infraestructura/gateway spring-boot:run
```

Abre **http://localhost:8761** para observar el panel Eureka y los servicios registrados. Todas las solicitudes externas pasan por **http://localhost:8080** (Gateway); los otros puertos se usan para pruebas del registro/infraestructura.

## 6. Flujo demostrativo completo

Ejecuta el script `scripts/demo.ps1` **una vez que todos los servicios estén disponibles**:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/demo.ps1
```

El script registra un paciente ficticio, consulta su ID, comprueba rechazo por duplicidad, crea una cita futura, la confirma, registra atención administrativa y un pago de demostración; también muestra las tablas de notificaciones y auditoría después de un breve intervalo. Verifica los mensajes de RabbitMQ, Eureka y los datos SQL para obtener las capturas requeridas.

### Endpoints principales (a través de Gateway)

| Método | Ruta | Propósito |
|---|---|---|
| POST | `/api/pacientes` | Registrar paciente |
| GET | `/api/pacientes` | Listar pacientes (Flux) |
| GET | `/api/pacientes/{id}` | Consultar (Mono, 404 si no existe) |
| PUT | `/api/pacientes/{id}` | Actualizar |
| PATCH | `/api/pacientes/{id}/estado` | Activar/inactivar |
| POST | `/api/citas` | Programar cita con validación síncrona de paciente |
| GET | `/api/citas`, `/api/citas/{id}` | Consultar cita(s) |
| PATCH | `/api/citas/{id}/estado` | Controlar transiciones |
| POST | `/api/atenciones` | Registrar atención administrativa simulada |
| GET | `/api/atenciones`, `/api/atenciones/{id}` | Consultar atenciones |
| POST | `/api/pagos` | Registrar pago ficticio, monto > 0 |
| GET | `/api/pagos`, `/api/pagos/{id}` | Consultar pagos |
| GET | `/api/notificaciones` | Consultar avisos registrados |
| GET | `/api/auditoria` | Consultar historial de eventos |

Datos de ejemplo y códigos HTTP en `docs/DOCUMENTO_TECNICO.md`.

## 7. GitHub y CI

El archivo `.github/workflows/ci.yml` activa la compilación y las pruebas con `mvn verify` para cada `push` y `pull_request` dirigido a `main`. El proyecto es multi-módulo: desde la raíz Maven compila y prueba las **8 aplicaciones**.

Las evidencias deben mostrar un workflow exitoso y un fallo **controlado** en una rama separada, corregido mediante otro commit (ver `docs/GUIA_EVIDENCIAS.md`). El despliegue a producción no forma parte de la evaluación y no se configura.

## 8. Documentación

- `docs/DOCUMENTO_TECNICO.md`: análisis, límites de dominio, arquitectura, datos, flujos, contratos, seguridad y CI.
- `docs/GUIA_EVIDENCIAS.md`: lista de capturas y ciclo CI rojo/verde.
- `docs/arquitectura.mmd` y `docs/flujo.mmd`: diagramas Mermaid listos para visualizar en GitHub o el editor.
- `database/`: seis esquemas SQL independientes y script de creación de bases.

## Limitaciones del alcance académico

- Los avisos son registros, no correos ni SMS reales.
- No se implementa autenticación/autorización ni datos clínicos; **no exponer el Gateway a Internet**.
- Los cambios de cita se guardan antes de publicar el evento; ante caída de RabbitMQ podría quedar un cambio de estado sin evento. En producción se usaría el patrón *transactional outbox*.
- Fechas/hora de las citas y de los eventos se manejan en **UTC** para simplificar el ejercicio.
- El proyecto requiere ejecución y capturas en un equipo con Java, Maven, PostgreSQL y RabbitMQ. No presenta evidencia real de ejecución CI hasta que se publique el código y corra GitHub Actions.
