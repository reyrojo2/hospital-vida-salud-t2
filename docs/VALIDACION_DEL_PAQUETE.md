# Estado de validación antes de subir a GitHub

**Comprobaciones realizadas sobre este ZIP:**

- Analizados sintácticamente los archivos Java mediante el parser del compilador JDK 21: sin errores sintácticos detectados.
- Validados los 9 archivos `pom.xml` como XML bien formado.
- Validados los archivos `.yml` con un parser YAML, incluida la configuración del Gateway y la definición GitHub Actions.
- Existen scripts SQL independientes para cada una de las seis bases de datos.
- Existen pruebas con JUnit 5, Mockito, Reactor Test y WebTestClient para los principales requisitos.
- Revisado que no se incluya `.git`, archivos `.env` con datos secretos, tokens ni capturas ficticias.

**Todavía no demostrado:**

- Ejecución real de `mvn verify` contra dependencias descargadas de Maven Central.
- Inicio de los ocho módulos con PostgreSQL, Eureka y RabbitMQ reales.
- Workflow de GitHub Actions verde y demostración roja/verde.
- Evidencias y capturas tomadas en la PC del estudiante.

Estas verificaciones necesitan Internet para resolver dependencias y/o servicios instalados. Este entorno de creación no dispone de Maven ni acceso directo al repositorio de paquetes. El proyecto se entrega para prueba y ajuste final en la PC del estudiante; no debe declararse «compilado y aprobado» antes de completar los pasos del README.
