# Prompt Generación de Endpoints REST - v1.0

## Contexto
Genera 3 endpoints REST nuevos en Spring Boot 3.2 (Java 21, records) siguiendo exactamente los patrones de los endpoints de referencia. Respeta arquitectura en capas (controller → service → repository), seguridad con JWT y roles, validaciones con Bean Validation 3.0, manejo centralizado de excepciones y pruebas con JUnit 5 + Mockito + REST Assured.

### Endpoints de referencia (Few-Shot)
Usa estos ejemplos como guía exacta de estilo, anotaciones y estructura:
1. **POST /api/v1/accounts** – Crear cuenta
```java
@PostMapping("/api/v1/accounts")
@Operation(summary = "Create new account", security = @SecurityRequirement(name = "bearer-jwt"))
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Account created"),
    @ApiResponse(responseCode = "400", description = "Invalid input"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
})
public ResponseEntity<AccountResponse> createAccount(
    @Valid @RequestBody CreateAccountRequest request
) {
    // Implementación
}
```
2. **GET /api/v1/transactions/{id}** – Obtener transacción
```java
@GetMapping("/api/v1/transactions/{id}")
@Operation(summary = "Get transaction by ID")
@CacheResult(cacheName = "transactions")
public ResponseEntity<TransactionResponse> getTransaction(
    @PathVariable @Pattern(regexp = "^TXN-[0-9]{10}$") String id
) {
    // Implementación
}
```
3. **PUT /api/v1/customers/{customerId}/status** – Actualizar estado
```java
@PutMapping("/api/v1/customers/{customerId}/status")
@Operation(summary = "Update customer status")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<CustomerResponse> updateStatus(
    @PathVariable UUID customerId,
    @Valid @RequestBody UpdateStatusRequest request
) {
    // Implementación
}
```

### Endpoints a generar
- POST `/api/v1/transfers` – Crear transferencia entre cuentas (validar fondos)
- GET `/api/v1/accounts/{accountId}/balance` – Consultar saldo con cache
- PATCH `/api/v1/loans/{loanId}/payment` – Registrar pago de préstamo

## Prompt a ejecutar
1. Rol del sistema: "Eres un experto en Spring Boot 3.2, seguridad JWT, OpenAPI 3, y pruebas con JUnit 5 + Mockito + REST Assured."
2. Instrucciones al modelo:
   - Reproduce el estilo exacto de los ejemplos Few-Shot anteriores.
   - Usa Java 21 con records para DTOs.
   - Anota con OpenAPI 3.0, seguridad JWT, y roles en endpoints sensibles.
   - Aplica Bean Validation 3.0 en path params y bodies.
   - Incluye manejo de errores coherente con `@ControllerAdvice` (no implementar `@ControllerAdvice`, solo usar excepciones ya manejadas).
   - Genera pruebas unitarias y de integración minimalistas que sigan el patrón de los ejemplos (usa REST Assured para integración).
   - No uses `Map<String, Object>`; define Request/Response records.
   - Usa respuestas HTTP correctas (201 creación, 200 consulta, 400/401/403/404/409 según caso).
   - Para caching usa `@CacheResult` igual que en el ejemplo.
   - Incluye comentarios `// Implementación` donde corresponda sin desarrollar la lógica completa.
3. Solicita el output en bloques separados:
   - **Controllers** (3 endpoints nuevos)
   - **DTOs** (requests/responses)
   - **Services/Repositories (interfaces o métodos)**
   - **Security annotations** confirmadas
   - **Tests** (unit + integration esqueleto)

## JSON Schema de OpenAPI
Usa este JSON Schema para validar que cada endpoint incluye los elementos mínimos de OpenAPI 3.0 en las anotaciones:
```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "title": "OpenAPIAnnotationSchema",
  "type": "object",
  "properties": {
    "operation": {
      "type": "object",
      "required": ["summary"],
      "properties": {
        "summary": {"type": "string"},
        "security": {"type": "array"},
        "responses": {"type": "array"}
      }
    },
    "requestBody": {"type": "object"},
    "parameters": {"type": "array"},
    "responses": {
      "type": "object",
      "patternProperties": {
        "^[0-9]{3}$": {"type": "object"}
      }
    }
  },
  "required": ["operation", "responses"],
  "additionalProperties": true
}
```

## Output del LLM
Pega aquí el resultado generado (controllers, DTOs, services, tests):
- Controllers: `[pendiente]`
- DTOs: `[pendiente]`
- Services/Repositories: `[pendiente]`
- Tests: `[pendiente]`

## Validación OpenAPI
- ✅/❌ Endpoint Transfers cumple OpenAPI 3.0: `[pendiente]`
- ✅/❌ Endpoint Balance cumple OpenAPI 3.0: `[pendiente]`
- ✅/❌ Endpoint Loan Payment cumple OpenAPI 3.0: `[pendiente]`
- ✅/❌ Annotations de seguridad presentes: `[pendiente]`
- ✅/❌ Validaciones Jakarta Validation correctas: `[pendiente]`
- ✅/❌ Exception handling implementado: `[pendiente]`
- ✅/❌ Unit tests con cobertura >80%: `[pendiente]`

## Métrica: Conformidad OpenAPI
- Endpoints válidos según spec OpenAPI: `X/3 (X%)`
- Swagger UI renderiza sin errores: ✅/❌
