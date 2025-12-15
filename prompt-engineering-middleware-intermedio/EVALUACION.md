# Evaluación del repositorio contra el assignment

## Hallazgos generales
- La estructura de carpetas solicitada está presente (`prompts/`, `openapi-specs/`, `outputs/`, `endpoints-generados/`, `analisis-seguridad/`, `arquitectura-microservicios/`, `screenshots/`).
- No existe el PDF final ni el archivo ZIP con el nombre requerido, y la carpeta `screenshots/` solo contiene un marcador `.gitkeep`, por lo que faltan evidencias de ejecución solicitadas.

## Parte 1
- Escenario A y B están documentados en `README.md` con técnicas, justificaciones y riesgos. El formato solicitado de viñetas y riesgos se cumple parcialmente (no hay separación explícita de 2-3 líneas para riesgos por escenario, pero el contenido está presente).【F:README.md†L7-L37】【F:README.md†L39-L62】

## Parte 2
- **Caso 1**: El prompt incluye los ejemplos Few-Shot, JSON Schema y validaciones. El output generado contiene controladores, DTOs, servicios, `@ControllerAdvice` y pruebas unitarias/integración, con validaciones y seguridad JWT.【F:prompts/caso1-endpoints.md†L1-L88】【F:endpoints-generados/caso1-endpoints-output.md†L1-L176】
  - La validación OpenAPI se declaró como revisión manual; no hay evidencia automática ni métricas de cobertura de pruebas (marca ⚠️ en el propio archivo).【F:prompts/caso1-endpoints.md†L90-L109】
- **Caso 2**: El prompt exige Long Context + CoT y especifica tabla de resultados. El output entrega razonamiento paso a paso, tabla, vulnerabilidades y métricas. Sin embargo, el prompt no incluye los YAML completos como indica el requerimiento de “copiar y pegar íntegro”, lo que deja un gap de cumplimiento de entrada obligatoria.【F:prompts/caso2-api-security.md†L7-L38】【F:analisis-seguridad/caso2-api-security-output.md†L1-L43】
- **Caso 3**: Se documenta el prompt chaining con cuatro prompts y el output incluye bounded contexts, diseño de microservicios, comunicación, migración y métricas. Cumple el formato solicitado.【F:prompts/caso3-microservices.md†L1-L52】【F:arquitectura-microservicios/caso3-microservices-output.md†L1-L124】

## Parte 3
- **Iteración con métricas**: Se registran v1.0 (tabla de gaps), v2.0, v3.0 y un prompt final. El output v3.0 alcanza 96% según la tabla, pero no hay evidencia de ejecución de pruebas (`mvn test`/`jacoco`) ni cobertura real; las métricas son “evaluación manual”.【F:prompts/caso4-iteration.md†L1-L90】【F:outputs/caso4-iteration-output.md†L1-L83】
- **Justificación de técnicas (3.2)**: El documento `prompts/parte3-justificacion.md` cumple la longitud y explica el impacto de cada técnica.【F:prompts/parte3-justificacion.md†L1-L19】

## Brechas frente a los requisitos
1. Falta el PDF final y el ZIP con nomenclatura solicitada.
2. No hay capturas de pantalla `caso*-execution.png` en `screenshots/`.
3. Caso 2: el prompt no incluye los YAML completos como entrada explícita.
4. Parte 2 Caso 1 y Parte 3 Caso 4: métricas de validación y cobertura se basan en “revisión manual”; no hay comandos ni reportes de pruebas.
5. No hay métricas ejecutables (p. ej., `Swagger UI` o validaciones automáticas de OpenAPI) que respalden los checkmarks.
