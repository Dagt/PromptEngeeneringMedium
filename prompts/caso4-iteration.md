# Iteración de Prompt: Transfer Endpoint

## v1.0 - Prompt Inicial
Prompt original del enunciado:
```
Genera un endpoint REST para transferencias bancarias en Spring Boot.
Debe validar fondos y retornar la transacción creada.
```

### Output v1.0
`[pendiente: pega el código generado en la versión 1.0]`

### Métricas v1.0
Completa la tabla de métricas con los puntajes iniciales (según el enunciado, todos 0%):
```
OpenAPI documentation (@Operation): 0%
Input validation (Jakarta Validation): 0%
Security (@PreAuthorize, JWT): 0%
Exception handling (@ControllerAdvice): 0%
Proper DTOs (no Map<String, Object>): 0%
Business logic (validar fondos): 0%
HTTP status codes correctos (201, 400, 409): 0%
Unit tests (JUnit + Mockito): 0%
Integration tests (REST Assured): 0%
Logging/Observability (SLF4J, metrics): 0%
TOTAL: 0%
```

### Análisis de Gap
`[pendiente: explica qué falta y por qué falló]`

---
## v2.0 - Primera Iteración
Define un prompt mejorado que incluya técnicas avanzadas. Ejemplo de estructura:
- Rol: "Eres un experto en Spring Boot 3.2, seguridad bancaria y OpenAPI 3."
- Few-Shot: incluye un endpoint completo que cumpla con OpenAPI, seguridad y validaciones como muestra.
- JSON Schema: define la estructura esperada de controller + DTOs + service + tests.
- Especifica seguridad JWT, roles, validación de fondos en el servicio, y códigos HTTP correctos.

Escribe aquí tu prompt v2.0:
`[pendiente: prompt v2.0]`

### Cambios aplicados
- Técnica 1 agregada: `[Few-Shot / JSON Schema / System Instructions / otra]`
- Técnica 2 agregada: `[detalla]`
- Especificación agregada: `[ej. OpenAPI annotations, security, DTOs]`

### Output v2.0
`[pendiente: pega el código generado en la versión 2.0]`

### Métricas v2.0
`[pendiente: tabla de métricas actualizada]`

---
## v3.0 - Segunda Iteración (agrega más si es necesario)
Sigue iterando hasta superar 95% en todas las métricas. Refuerza:
- Coherencia de anotaciones OpenAPI
- Validaciones estrictas (Bean Validation)
- Seguridad y manejo de errores
- Tests unitarios e integración

### Prompt v3.0
`[pendiente: prompt v3.0]`

### Cambios aplicados
`[pendiente]`

### Output v3.0
`[pendiente]`

### Métricas v3.0
`[pendiente]`

---
## Prompt Final (vX.Y)
`[pendiente: prompt que logra >95% en todas las métricas]`

### Output Final
`[pendiente: código final]`

### Métricas Finales
`[pendiente: tabla con scores >95%]`

### Validación con Tests
Comandos a ejecutar (apunta los resultados cuando los corras):
- `./mvnw test`
- `./mvnw jacoco:report`

Cobertura esperada (actualiza con los datos reales):
- Line Coverage: `XX% (objetivo: >90%)`
- Branch Coverage: `XX% (objetivo: >85%)`
- Method Coverage: `XX% (objetivo: >90%)`

## Resumen de Iteraciones
Completa la tabla cuando tengas los resultados:
| Versión | Técnicas Aplicadas | Score Total | Iteraciones Necesarias |
|---------|-------------------|-------------|----------------------|
| v1.0 | Ninguna | 0% | - |
| v2.0 | [técnicas] | XX% | - |
| v3.0 | [técnicas] | XX% | - |
| vX.Y | [técnicas] | >95% ✅ | X iteraciones |
