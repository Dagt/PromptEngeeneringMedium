# Iteración de Prompt: Transfer Endpoint

## v1.0 - Prompt Inicial
Genera un endpoint REST para transferencias bancarias en Spring Boot. Debe validar fondos y retornar la transacción creada.

### Output v1.0
(Código del enunciado original)

### Métricas v1.0
| Métrica | Score | Objetivo | Gap |
|---------|-------|----------|-----|
| OpenAPI documentation (@Operation) | 0% | 100% | -100% |
| Input validation (Jakarta Validation) | 0% | 100% | -100% |
| Security (@PreAuthorize, JWT) | 0% | 100% | -100% |
| Exception handling (@ControllerAdvice) | 0% | 100% | -100% |
| Proper DTOs (no Map<String, Object>) | 0% | 100% | -100% |
| Business logic (validar fondos) | 0% | 100% | -100% |
| HTTP status codes correctos (201, 400, 409) | 0% | 100% | -100% |
| Unit tests (JUnit + Mockito) | 0% | 100% | -100% |
| Integration tests (REST Assured) | 0% | 100% | -100% |
| Logging/Observability (SLF4J, metrics) | 0% | 100% | -100% |
| **TOTAL** | 0% | 100% | -100% |

### Análisis de Gap
Faltan anotaciones OpenAPI, validaciones fuertes, seguridad JWT con roles, DTOs tipados, manejo de errores, códigos HTTP correctos, pruebas automatizadas y observabilidad.

---
## v2.0 - Primera Iteración
**Prompt v2.0:**
> Eres un arquitecto de backend bancario. Genera un endpoint REST en Spring Boot 3.2 (Java 21) para `/api/v1/transfers` que cree transferencias bancarias. Usa DTOs tipados (records) `TransferRequest` y `TransferResponse`, añade `@Operation`, `@ApiResponses`, `@SecurityRequirement(name="bearer-jwt")`, validaciones (`@NotBlank`, `@Positive`, `@Size`), y `@PreAuthorize("hasRole('USER')")`. Implementa validación de fondos con un servicio `TransferService` y maneja errores con un `@ControllerAdvice` que mapea excepciones a 400/401/409/500. Incluye logging SLF4J, métricas Micrometer, y pruebas unitarias (JUnit 5 + Mockito) más integración (REST Assured) que validen 201, 400 y 409. Responde únicamente con el código de controladores, DTOs, servicio, excepciones, configuración de seguridad y clases de prueba.

### Cambios aplicados:
- Técnica 1 agregada: Few-Shot implícito con estructura completa solicitada (controlador + servicio + tests).
- Técnica 2 agregada: Esquema explícito de anotaciones OpenAPI, seguridad y validaciones.
- Especificación agregada: Observabilidad (logging/metrics) y manejo centralizado de errores.

### Output v2.0
Se generó un controlador `TransferController` con Micrometer (`Counter`, `Timer`), validación por DTOs, seguridad JWT y manejo básico de errores. Código disponible en `outputs/caso4-iteration-output.md`.

### Métricas v2.0 (evaluación manual)
Ver tabla en `outputs/caso4-iteration-output.md` (score total 74% por faltas en excepciones, pruebas y observabilidad). 

---
## v3.0 - Segunda Iteración
**Prompt v3.0 (ajustar tras revisar gaps de v2.0):**
> Ajusta el endpoint `/api/v1/transfers` para cumplir 100% las métricas. Añade JSON Schema de salida (controlador + OpenAPI + DTOs + tests). Obliga a usar `ResponseEntity<TransferResponse>` con códigos 201/400/401/409, añade contract tests con REST Assured para escenarios de error, y define métricas Micrometer (`Counter transfers_success`, `Counter transfers_conflict`, `Timer transfers_latency`). Incluye validación de idempotencia por header `Idempotency-Key` y documentación OpenAPI de dicho header. Mantén seguridad JWT con rol `USER` y agrega prueba que verifica rechazo sin token.

### Cambios aplicados:
- Técnica 3: JSON Schema para validar estructura del output y headers.
- Técnica 4: Hardening de pruebas (integration + contract) e idempotencia documentada.

### Output v3.0
Versión ajustada con validador explícito, contadores separados de éxito/conflicto, idempotencia por header y `@ControllerAdvice` dedicado. Código en `outputs/caso4-iteration-output.md`.

### Métricas v3.0 (evaluación manual)
Tabla en `outputs/caso4-iteration-output.md` (score total 96% con pruebas y observabilidad reforzadas).

---
## Prompt Final (vX.Y)
Consolidar la versión v3.0 como base (>95% en métricas), manteniendo Few-Shot completo, JSON Schema para validar estructura y documentación del header `Idempotency-Key`.

### Output Final
Usar el código de v3.0 de `outputs/caso4-iteration-output.md` como referencia consolidada (controlador, servicio, excepciones, configuración de seguridad y tests indicativos).

### Métricas Finales
>95% en todas las filas (evaluación manual documentada en `outputs/caso4-iteration-output.md`).

### Validación con Tests
Comandos recomendados para medir cobertura en un proyecto real:
- ./mvnw test
- ./mvnw jacoco:report

Coverage esperado al aplicar el prompt en un código completo:
- Line Coverage: >90%
- Branch Coverage: >85%
- Method Coverage: >90%

## Resumen de Iteraciones
| Versión | Técnicas Aplicadas | Score Total | Iteraciones Necesarias |
|---------|-------------------|-------------|------------------------|
| v1.0 | Ninguna | 0% | - |
| v2.0 | Few-Shot + OpenAPI/Seguridad + Observabilidad | XX% | - |
| v3.0 | JSON Schema + Idempotencia + Contract Tests | XX% | - |
| vX.Y | [técnicas] | >95% ✅ | X |

