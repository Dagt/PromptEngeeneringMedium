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
_Pegar aquí el código generado._

### Métricas v2.0
_Completar tras ejecutar el prompt._

---
## v3.0 - Segunda Iteración
**Prompt v3.0 (ajustar tras revisar gaps de v2.0):**
> Ajusta el endpoint `/api/v1/transfers` para cumplir 100% las métricas. Añade JSON Schema de salida (controlador + OpenAPI + DTOs + tests). Obliga a usar `ResponseEntity<TransferResponse>` con códigos 201/400/401/409, añade contract tests con REST Assured para escenarios de error, y define métricas Micrometer (`Counter transfers_success`, `Counter transfers_conflict`, `Timer transfers_latency`). Incluye validación de idempotencia por header `Idempotency-Key` y documentación OpenAPI de dicho header. Mantén seguridad JWT con rol `USER` y agrega prueba que verifica rechazo sin token.

### Cambios aplicados:
- Técnica 3: JSON Schema para validar estructura del output y headers.
- Técnica 4: Hardening de pruebas (integration + contract) e idempotencia documentada.

### Output v3.0
_Pegar aquí el código generado._

### Métricas v3.0
_Completar tras ejecutar el prompt._

---
## Prompt Final (vX.Y)
_Usar la iteración necesaria hasta superar 95% en todas las métricas. Recomendación: combinar el mejor prompt anterior, reforzar ejemplos Few-Shot de endpoints completos y fijar formato exacto de paquetes._

### Output Final
_Pegar código final del endpoint, DTOs, servicio, excepciones, configuración de seguridad y tests._

### Métricas Finales
_Tablas de métricas con scores >95% en todas las filas._

### Validación con Tests
Comandos a ejecutar (llenar con resultados reales):
- ./mvnw test
- ./mvnw jacoco:report

Coverage Report esperado:
- Line Coverage: XX% (objetivo: >90%)
- Branch Coverage: XX% (objetivo: >85%)
- Method Coverage: XX% (objetivo: >90%)

## Resumen de Iteraciones
| Versión | Técnicas Aplicadas | Score Total | Iteraciones Necesarias |
|---------|-------------------|-------------|------------------------|
| v1.0 | Ninguna | 0% | - |
| v2.0 | Few-Shot + OpenAPI/Seguridad + Observabilidad | XX% | - |
| v3.0 | JSON Schema + Idempotencia + Contract Tests | XX% | - |
| vX.Y | [técnicas] | >95% ✅ | X |

