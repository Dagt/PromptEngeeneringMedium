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
```
Sistema: Eres un experto en Spring Boot 3.2, banca y seguridad. Genera un endpoint REST para transferencias bancarias.
Instrucciones: Sigue exactamente el estilo de los endpoints de referencia (OpenAPI + validaciones + seguridad). Usa Java 21 con records.
- Controlador: `@RestController` y `@RequestMapping("/api/v1/transfers")`.
- Operación: `@PostMapping` con `@Operation(summary = "Create transfer", security = @SecurityRequirement(name = "bearer-jwt"))` y respuestas 201, 400, 401, 403, 404, 409.
- DTOs: `CreateTransferRequest` (fromAccount: UUID, toAccount: UUID, amount: BigDecimal @Positive, currency: String @NotBlank, reference: String @Size(max=80)); `TransferResponse` con id, status, createdAt.
- Seguridad: `@PreAuthorize("hasRole('USER')")` y validación de fondos en el servicio.
- Servicio: método `createTransfer` que valida fondos, registra transacción y retorna DTO.
- Excepciones: usa custom exceptions `InsufficientFundsException`, `AccountNotFoundException` ya manejadas por `@ControllerAdvice`.
- Tests: crea esqueletos de unit tests (Mockito) y de integración (REST Assured) con casos éxito/insuficientes fondos.
- Logging/metrics: agrega `@Slf4j` y métrica básica con Micrometer (`Counter transferCreated`).
Output solicitado: Controllers, DTOs, Service (interfaz + stub), Tests (unit + integration). No incluyas la lógica real, solo stubs con comentarios.
```

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
```
Sistema: Arquitecto senior de pagos bancarios. Mejora el endpoint de transferencia para superar 95% en métricas.
Refuerza: OpenAPI completa, validaciones estrictas, seguridad y observabilidad.

Requisitos:
- Añade `@Tag(name = "Transfers")`, ejemplos en `@ApiResponses` y esquemas de error con `ErrorResponse`.
- Bean Validation: valida moneda con `@Pattern(regexp = "^[A-Z]{3}$")`, monto máximo `@DecimalMax("1000000.00")`, y prohíbe transferirse a sí mismo (constraint custom `@DifferentAccount`).
- Idempotencia: encabezado `Idempotency-Key` obligatorio (`@RequestHeader @NotBlank`).
- Códigos HTTP: 201 éxito, 400 validación, 401/403 seguridad, 404 cuentas, 409 fondos insuficientes, 429 reintentos duplicados.
- Seguridad: `@PreAuthorize("hasAnyRole('USER','ADMIN')")` y anotación de auditoría `@Audited` (comentario si no existe).
- Servicio: incluir pseudocódigo de validación de fondos, registro en Kafka tópico `transfers.events`, y transacción ACID.
- Tests: casos para idempotencia, validación de moneda, fondos insuficientes y cabecera faltante.
- Observabilidad: métricas `Counter transferCreated`, `Counter transferConflict`, timer de latencia; logs estructurados con correlation-id.
Output: igual formato v2.0.
```

### Cambios aplicados
`[pendiente]`

### Output v3.0
`[pendiente]`

### Métricas v3.0
`[pendiente]`

---
## Prompt Final (vX.Y)
```
Sistema: Arquitecto bancario. Entrega el endpoint de transferencia cumpliendo >95% en métricas de OpenAPI, validaciones, seguridad, pruebas y observabilidad.

Lineamientos finales:
- Combina todo lo de v3.0 y añade: documentación OpenAPI con ejemplos completos (`@ExampleObject`), esquema de error reutilizable, y descripción de rate limiting (`X-RateLimit-*`).
- Incluye constraint custom `@DifferentAccount` (definición mínima) y uso en el DTO.
- En el servicio, muestra pasos comentados: verificar cuentas, verificar fondos, registrar transacción, publicar evento Kafka, persistir de forma transaccional, retornar DTO.
- Tests: unitarios (Mockito) cubriendo excepciones, integración (REST Assured) con escenarios 201, 400, 404, 409, 429.
- Observabilidad: métricas Micrometer + log de negocio con `log.info("transfer.created", ... )`.
- Entrega bloques: Controller, DTOs, Constraint `DifferentAccount`, Service interface + impl stub, Tests (unit + integration), Snippet de configuración de métricas.
```

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
