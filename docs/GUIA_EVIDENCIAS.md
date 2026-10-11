# Guía de evidencias para entrega T2

Guardar las capturas reales dentro de `evidencias/` con nombres representativos. **No incluir capturas inventadas**: la carpeta está vacía hasta ejecutar los sistemas y workflows.

1. `01-eureka.png`: panel en `http://localhost:8761`, con los microservicios registrados.
2. `02-gateway.png`: solicitud real por `http://localhost:8080/api/pacientes` con respuesta HTTP 201.
3. `03-paciente-duplicado.png`: petición duplicada y respuesta HTTP 409.
4. `04-cita-confirmada.png`: cita asociada al paciente, con transición a `CONFIRMADA`.
5. `05-atencion-pago.png`: operaciones administrativas ficticias.
6. `06-sql-r2dbc.png`: datos en tablas PostgreSQL propias de cada servicio y configuración R2DBC.
7. `07-rabbitmq.png`: exchange `hospital.eventos`, routing keys y colas de consumidores, junto a evidencia de registros en Notificaciones y Auditoría.
8. `08-maven-test.png`: `mvn verify` exitoso en terminal.
9. `09-github-ci-rojo.png`: workflow fallido por una prueba modificada intencionalmente en una rama independiente.
10. `10-github-ci-verde.png`: segundo workflow con la prueba corregida y estado exitoso.
11. `11-pull-request.png`: PR dirigido a `main` con verificaciones finalizadas.
12. `12-diagrama.png`: visualización legible de la arquitectura/flujo Mermaid.

## Demostrar fallo controlado sin destruir el proyecto

1. Publica el ZIP en `feature/fase-1-arquitectura`, comprueba que el workflow pasa.
2. Crea una rama desde ella, por ejemplo `test/fallo-controlado-ci`.
3. En `PacienteServicioTest.java`, cambia temporalmente la expectativa `assertEquals("ACTIVO",p.estado())` a `assertEquals("INACTIVO",p.estado())`.
4. Commit y push de esa rama. Comprueba que el paso `mvn verify` queda rojo y captura el log con el fallo de aserción.
5. Restituye la expectativa correcta en otro commit y push; guarda captura verde.
6. Crea el Pull Request hacia `main` desde tu rama principal de trabajo, **no mezcles el fallo controlado** con los cambios finales.

El workflow del repositorio reacciona a todos los push; también se ejecuta en PR hacia `main`.

## Comandos Git para trabajar desde PC

```powershell
git clone -b feature/fase-1-arquitectura https://github.com/reyrojo2/hospital-vida-salud-t2.git
cd hospital-vida-salud-t2
# Copiar aqui el contenido del ZIP sin borrar .git.
git status
git add .
git commit -m "feat: implementar microservicios hospitalarios reactivos y CI"
git push -u origin feature/fase-1-arquitectura
```

Una vez terminado, crea el PR en GitHub con base `main` y compare `feature/fase-1-arquitectura`. Entrega un repositorio accesible para revisión.
