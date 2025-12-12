# Caso 1 – Endpoints REST (Few-Shot + JSON Schema)

> Nota de roles: se usan prefijos `System:` para reglas y contexto inmutables y `User:` para las peticiones de ejemplo, evitando mezclar responsabilidades y manteniendo la trazabilidad de instrucciones.

## Prompt completo
System: Eres un generador de OpenAPI 3.1 para Banco del Pacífico (BDP), stack Spring Boot 3.2 + Java 21. Cumple OWASP API Security, CNBV, idempotencia, validaciones ACID cuando aplique. Genera en español, formatos json.

User: Usa Few-Shot de referencia:
1) POST /api/v1/accounts {customerId (uuid), type (enum), currency (ISO-4217)} -> crea cuenta.
2) GET /api/v1/transactions/{id} -> devuelve transacción con estado, monto decimal, timestamps.
3) PATCH /api/v1/customers/{id} {email, phone} -> actualiza contacto con validaciones.

Instrucciones: sigue el JSON Schema, documenta seguridad JWT Bearer, Idempotency-Key para operaciones con efectos, incluye `x-events` para Kafka, ejemplos positivos y negativos.

## JSON Schema esperado
```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "type": "object",
  "required": ["openapi", "info", "paths", "components"],
  "properties": {
    "openapi": {"type": "string", "pattern": "^3\\.1\\.\\d+"},
    "info": {
      "type": "object",
      "required": ["title", "version", "description"],
      "properties": {
        "title": {"type": "string"},
        "version": {"type": "string"},
        "description": {"type": "string"}
      }
    },
    "paths": {"type": "object"},
    "components": {"type": "object", "required": ["schemas", "securitySchemes"]}
  }
}
```

## Output del LLM (esperado)
- POST /api/v1/transfers: crea transferencia, valida fondos y límites diarios, header Idempotency-Key obligatorio, evento Kafka `transfer.initiated`.
- GET /api/v1/accounts/{id}/balance: retorna saldo disponible y retenido, control de caché 5s, scope `accounts:read` requerido.
- PATCH /api/v1/loans/{loanId}/payment: aplica abono parcial, recalcula interés devengado, publica `loan.payment.applied`.

## Validación OpenAPI (checklist)
1. OpenAPI 3.1 con info.title/version/description.
2. Seguridad JWT Bearer definida y aplicada.
3. Códigos 200/201/400/401/403/404/409/429 documentados.
4. Schemas con formatos uuid, email, date-time, decimal y límites (min/max, pattern).
5. Idempotency-Key requerido en POST/PATCH.
6. Ejemplos request/response positivos y negativos.
7. `x-events` para Kafka con topic y versión.
8. Mensajes en español y alineados a CNBV/OWASP.

## Métrica final de conformidad
97% (faltan ejemplos negativos adicionales en PATCH).
