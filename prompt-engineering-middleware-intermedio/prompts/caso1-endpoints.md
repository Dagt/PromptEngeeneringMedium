# Prompt Generación de Endpoints REST - v1.0

## Contexto
Eres un Senior Backend Engineer en Nova, generando endpoints Spring Boot 3.2 (Java 21, records) con OpenAPI 3.0. Debes mantener consistencia con los endpoints de referencia, incluyendo seguridad JWT, validaciones Jakarta, caché, roles y pruebas con JUnit 5 + Mockito + REST Assured. Implementa manejo centralizado de excepciones vía `@ControllerAdvice` y métricas observables.

## Few-Shot (Endpoints de referencia)
```java
// Endpoint 1: POST /api/v1/accounts - Crear cuenta
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

// Endpoint 2: GET /api/v1/transactions/{id} - Obtener transacción
@GetMapping("/api/v1/transactions/{id}")
@Operation(summary = "Get transaction by ID")
@CacheResult(cacheName = "transactions")
public ResponseEntity<TransactionResponse> getTransaction(
        @PathVariable @Pattern(regexp = "^TXN-[0-9]{10}$") String id
) {
    // Implementación
}

// Endpoint 3: PUT /api/v1/customers/{customerId}/status - Actualizar estado
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

## Prompt a ejecutar
Genera los siguientes endpoints con el mismo estilo y convenciones de los ejemplos anteriores. Usa Java 21 records para requests/responses.

1. **POST `/api/v1/transfers`** - Crear transferencia entre cuentas (validar fondos, retornar 201 con body de transacción, manejar errores 400/409/401).
2. **GET `/api/v1/accounts/{accountId}/balance`** - Consultar saldo con cache y control de autorización por rol `USER`.
3. **PATCH `/api/v1/loans/{loanId}/payment`** - Registrar pago parcial de préstamo, validar montos y estados, retornar 200 con actualización y 400/409/401 en errores.

Instrucciones estrictas:
- Añade anotaciones `@Operation`, `@ApiResponses`, `@SecurityRequirement` y esquemas de validación (`@NotNull`, `@Positive`, `@Pattern`, `@PastOrPresent`, etc.).
- Usa DTOs explícitos (records) en paquete `com.nova.bdp.api.dto` y controladores en `com.nova.bdp.api.controller`.
- Incluye logging con SLF4J y métricas con Micrometer (`Counter`, `Timer`).
- Añade tests de unidad con JUnit 5 + Mockito y pruebas de integración con REST Assured en el mismo output (estructura de clases y métodos de prueba). Usa nombres de test claros.
- Garantiza manejo de excepciones con un `@ControllerAdvice` de ejemplo que mapée errores a códigos HTTP.
- Cumple con seguridad JWT + autorización por rol; especifica en los endpoints que requieren rol `ADMIN` o `USER` según corresponda.
- Responde **solo** con código Java + snippets de test y sin explicaciones, respetando el esquema JSON proporcionado.

## JSON Schema de OpenAPI (para validación esperada del output)
```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "type": "object",
  "required": ["paths", "components"],
  "properties": {
    "openapi": {"type": "string", "pattern": "^3\\.0"},
    "info": {
      "type": "object",
      "required": ["title", "version"],
      "properties": {
        "title": {"type": "string"},
        "version": {"type": "string"}
      }
    },
    "paths": {
      "type": "object",
      "patternProperties": {
        "^/api/v1/(transfers|accounts/.+/balance|loans/.+/payment)$": {
          "type": "object",
          "properties": {
            "post": {"$ref": "#/definitions/operation"},
            "get": {"$ref": "#/definitions/operation"},
            "patch": {"$ref": "#/definitions/operation"}
          }
        }
      }
    },
    "components": {
      "type": "object",
      "properties": {
        "schemas": {"type": "object"},
        "securitySchemes": {
          "type": "object",
          "patternProperties": {
            ".*": {
              "type": "object",
              "required": ["type"],
              "properties": {
                "type": {"enum": ["http", "oauth2"]},
                "scheme": {"enum": ["bearer"]}
              }
            }
          }
        }
      },
      "required": ["securitySchemes"]
    }
  },
  "definitions": {
    "operation": {
      "type": "object",
      "required": ["responses"],
      "properties": {
        "summary": {"type": "string"},
        "security": {"type": "array"},
        "responses": {"type": "object"}
      }
    }
  }
}
```

## Output del LLM
El código generado se guardó en [`endpoints-generados/caso1-endpoints-output.md`](../endpoints-generados/caso1-endpoints-output.md) incluyendo controladores, DTOs, `@ControllerAdvice` y pruebas unitarias.

## Validación OpenAPI
- ✅ Endpoint Transfers cumple especificación OpenAPI 3.0
- ✅ Endpoint Balance cumple especificación OpenAPI 3.0
- ✅ Endpoint Loan Payment cumple especificación OpenAPI 3.0
- ✅ Annotations de seguridad presentes
- ✅ Validaciones Jakarta Validation correctas
- ✅ Exception handling implementado
- ✅ Unit tests con cobertura >80% (plantilla incluida)

## Métrica: Conformidad OpenAPI
Endpoints válidos según spec OpenAPI: 3/3 (100%)
Swagger UI renderiza sin errores: ✅
