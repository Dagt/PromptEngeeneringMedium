# Prompt Generación de Endpoints REST - v1.0

## Contexto
Eres un arquitecto experto en Spring Boot 3.2 + Java 21 que debe generar **tres endpoints REST nuevos** manteniendo el mismo patrón de arquitectura, seguridad y pruebas que los ejemplos proporcionados. Sigue estrictamente las convenciones de OpenAPI 3.0, Jakarta Validation 3.0, seguridad JWT con autorización basada en roles y manejo centralizado de excepciones mediante `@ControllerAdvice`. Produce el código en **formato markdown** con bloques de código Java y las anotaciones OpenAPI completas. Asegúrate de que los DTO sean `record`, que exista separación Controller/Service/DTO/Exceptions, y que incluyas pruebas unitarias con JUnit 5 + Mockito y pruebas de contrato con REST Assured.

### Few-Shot: Endpoints de referencia (mantén el mismo estilo)
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

### Endpoints a generar (salida esperada)
1. **POST /api/v1/transfers** - Crear transferencia entre cuentas (validar fondos y retorno de transacción creada).
2. **GET /api/v1/accounts/{accountId}/balance** - Consultar saldo con caché y validación de UUID.
3. **PATCH /api/v1/loans/{loanId}/payment** - Registrar pago de préstamo parcial/total.

### Instrucciones de salida
- Incluye: Controller, DTOs (records), Service con lógica mínima y validación de fondos, Repository/mock gateway, excepciones personalizadas y `@ControllerAdvice`, seguridad con `@SecurityRequirement` y `@PreAuthorize` donde aplique.
- Añade `@ApiResponses` detalladas (201/200, 400, 401, 403, 404, 409, 500 según corresponda).
- Validaciones Jakarta: montos positivos, UUID válidos, patrones de ID, límites de longitud.
- Caching: usar `@CacheResult` para balance.
- Tests: añade ejemplos de pruebas unitarias JUnit 5 + Mockito (servicio) y REST Assured (controller) con nombres dados.
- Observabilidad: incluir logs con SLF4J y métrica simple de Micrometer (`Counter`).
- Entrega en bloques de código por componente: `Controller`, `DTOs`, `Service`, `Repository`, `Exceptions`, `ControllerAdvice`, `SecurityConfig (si aplica)`, `Tests`.

### Prompt a ejecutar en el LLM
"""
Sistema: Eres un ingeniero backend senior especializado en Spring Boot 3.2, seguridad JWT y diseño de APIs bancarias. Cumple estrictamente OpenAPI 3.0, Jakarta Validation 3.0 y las buenas prácticas OWASP.
Usuario: Con base en los ejemplos proporcionados, genera los siguientes endpoints manteniendo el mismo estilo arquitectónico y de anotaciones:
- POST /api/v1/transfers (validar fondos, retorna transacción creada, id formato ^TXN-[0-9]{12}$, roles USER/ADMIN)
- GET /api/v1/accounts/{accountId}/balance (UUID, caché, solo roles USER/ADMIN)
- PATCH /api/v1/loans/{loanId}/payment (UUID, montos positivos, soporta pagos parciales, roles USER)
Requisitos:
1) Usa records para requests/responses.
2) Anotaciones OpenAPI completas con `@Operation`, `@ApiResponses`, `@SecurityRequirement`.
3) Seguridad: JWT + `@PreAuthorize`.
4) Validaciones: Jakarta Validation (montos > 0, UUID, patrones, longitudes).
5) Excepciones: `@ControllerAdvice` con manejo de `BusinessException`, `NotFoundException`, `InsufficientFundsException`.
6) Pruebas: JUnit 5 + Mockito para servicios y REST Assured para controllers.
7) Observabilidad: SLF4J logs y Micrometer Counter por endpoint.
Formato de salida: Bloques de código agrupados por componente (Controller, DTOs, Services, Repositories, Exceptions, ControllerAdvice, SecurityConfig, Tests). No incluyas texto adicional fuera del código.
"""

## JSON Schema de OpenAPI
Schema de validación para cada operación generada. Se asume que el LLM devuelve fragmentos OpenAPI embebidos en anotaciones, pero se valida la forma general de las operaciones esperadas.
```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "title": "OpenAPIOperation",
  "type": "object",
  "required": ["paths", "components", "openapi"],
  "properties": {
    "openapi": {"type": "string", "pattern": "^3\\.0\\.\d+$"},
    "paths": {
      "type": "object",
      "properties": {
        "/api/v1/transfers": {
          "type": "object",
          "properties": {
            "post": {
              "type": "object",
              "required": ["summary", "requestBody", "responses", "security"],
              "properties": {
                "summary": {"type": "string"},
                "security": {"type": "array", "minItems": 1},
                "requestBody": {
                  "type": "object",
                  "required": ["content"],
                  "properties": {
                    "content": {"type": "object"}
                  }
                },
                "responses": {
                  "type": "object",
                  "required": ["201", "400", "401"],
                  "properties": {
                    "201": {"type": "object"},
                    "400": {"type": "object"},
                    "401": {"type": "object"}
                  }
                }
              }
            }
          }
        },
        "/api/v1/accounts/{accountId}/balance": {
          "type": "object",
          "properties": {
            "get": {
              "type": "object",
              "required": ["summary", "responses", "security", "parameters"],
              "properties": {
                "parameters": {"type": "array", "minItems": 1},
                "responses": {"type": "object", "required": ["200", "404", "401"]},
                "security": {"type": "array", "minItems": 1}
              }
            }
          }
        },
        "/api/v1/loans/{loanId}/payment": {
          "type": "object",
          "properties": {
            "patch": {
              "type": "object",
              "required": ["summary", "requestBody", "responses", "security"],
              "properties": {
                "responses": {"type": "object", "required": ["200", "400", "401", "404", "409"]}
              }
            }
          }
        }
      }
    },
    "components": {
      "type": "object",
      "properties": {
        "schemas": {
          "type": "object",
          "patternProperties": {
            ".+": {"type": "object", "required": ["type"]}
          }
        },
        "securitySchemes": {
          "type": "object",
          "required": ["bearer-jwt"],
          "properties": {
            "bearer-jwt": {
              "type": "object",
              "required": ["type", "scheme"],
              "properties": {
                "type": {"const": "http"},
                "scheme": {"const": "bearer"}
              }
            }
          }
        }
      },
      "required": ["schemas", "securitySchemes"]
    }
  }
}
```

## Output del LLM
_Pendiente de ejecución; pegar aquí los 3 endpoints generados por el LLM siguiendo el formato indicado._

## Validación OpenAPI
- ✅ /❌ Endpoint Transfers cumple especificación OpenAPI 3.0
- ✅ /❌ Endpoint Balance cumple especificación OpenAPI 3.0
- ✅ /❌ Endpoint Loan Payment cumple especificación OpenAPI 3.0
- ✅ /❌ Annotations de seguridad presentes
- ✅ /❌ Validaciones Jakarta Validation correctas
- ✅ /❌ Exception handling implementado
- ✅ /❌ Unit tests con cobertura >80%

## Métrica: Conformidad OpenAPI
Endpoints válidos según spec OpenAPI: X/3 (X%)
Swagger UI renderiza sin errores: ✅ /❌
