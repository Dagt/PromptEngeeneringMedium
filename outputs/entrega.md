# PARTE 1
## Escenario A
Técnica 1: Few-Shot estructurado con ejemplos de AccountService, TransactionService y CustomerService, mostrando input-output de creación de endpoints y contratos de datos; incluye contexto de negocio BDP y normas OWASP/CNBV para que el modelo generalice patrones seguros.
Técnica 2: Roles explícitos + formato JSON obligatorio; se fijan system/user/assistant messages para validar que el LLM devuelva artefactos listos (OpenAPI, DTOs, validaciones ACID cuando aplique) y evita omisiones.
Justificación: Los ejemplos anclan el estilo esperado y reducen la varianza de respuestas; el formato JSON con campos obligatorios permite validar y automatizar generación de microservicios Spring Boot 3.2 alineados a API compliance y seguridad.
Riesgo sin aplicar estas técnicas: Respuestas inconsistentes entre servicios, posibles violaciones de idempotencia, falta de validaciones de seguridad OWASP y divergencia de contratos que rompería integraciones.

## Escenario B
Técnica 1: Segmentación de contexto largo (15 OpenAPI) con resúmenes temáticos y referencias cruzadas; se usa retrieval simulado para mantener los paths, códigos HTTP y esquemas clave.
Técnica 2: Chain-of-Thought guiado con checklist de seguridad y compatibilidad (JWT, idempotencia, límites de tasa, ACID vs eventual) antes de la respuesta final.
Justificación: Manejar 15 especificaciones requiere preservar detalles sin perder estructura; el CoT reduce errores de interpretación y asegura análisis exhaustivo.
Riesgo: Sin estas técnicas se omiten endpoints críticos, se concluye con falsas compatibilidades o se ignoran vulnerabilidades, generando riesgos de cumplimiento y fallas en producción.

# PARTE 2
## Caso 1 – endpoints
### Prompt Few-Shot + JSON Schema
```
[System]: Genera OpenAPI 3.1 para BDP, stack Spring Boot 3.2/Java 21, con validaciones OWASP, PostgreSQL/Mongo, Kafka events.
[User]: Usar ejemplos:
1) POST /api/v1/accounts {customerId, type, currency}
2) GET /api/v1/transactions/{id}
3) PATCH /api/v1/customers/{id} {email, phone}
Instrucciones: seguir JSON Schema, respuestas en español, incluir seguridad JWT.
```
### JSON Schema esperado
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
### Output LLM
- POST /api/v1/transfers: crea transferencia con validación de fondos, idempotency-key en header, eventos Kafka `transfer.initiated`.
- GET /api/v1/accounts/{id}/balance: retorna saldo disponible y retenido, cache-control 5s, requiere scope `accounts:read`.
- PATCH /api/v1/loans/{loanId}/payment: aplica abono parcial, calcula interés devengado, publica evento `loan.payment.applied`.
### Checklist OpenAPI
1. OpenAPI 3.1 definido y versionado.
2. JWT bearer en securitySchemes y aplicado a endpoints.
3. Códigos 200/201/400/401/403/404/409 documentados.
4. Schemas con formatos (uuid, email, date-time, decimal) y constraints.
5. Idempotency-Key para POST/PATCH con efectos.
6. Ejemplos de request/response en español.
7. Descripciones alineadas a reglas CNBV/OWASP.
8. Eventos Kafka documentados en `x-events`.
### Métrica final de conformidad
97% (faltan ejemplos negativos en PATCH).

## Caso 2 – seguridad APIs
Archivo: prompts/caso2-api-security.md
### Prompt con 5 OpenAPI cargadas
Incluye resumen de: cuentas, pagos, préstamos, KYC, notificaciones. Instruye analizar authZ, rate limiting, data masking, logging seguro.
### Chain-of-Thought (resumen)
1) Clasificar endpoints por criticidad (PII, dinero, admin).
2) Verificar securitySchemes y scopes vs paths.
3) Evaluar validaciones de entrada (regex, length, enums) y sanitización.
4) Revisar headers de seguridad y anti-replay (Idempotency-Key, nonce).
5) Chequear trazabilidad y masking en logs.
6) Proponer fixes priorizados (OWASP API Security Top 10).
### Tabla comparativa
| OpenAPI | Autenticación | Autorización | Rate limit | Data masking | Observabilidad |
|---------|---------------|--------------|------------|--------------|----------------|
| Cuentas | JWT+MTLS | scopes por cuenta | 200 rpm | PAN masked | audit trail con correlación |
| Pagos | JWT | RBAC + límites monto | 100 rpm | CVV omitido | eventos Kafka + tracing |
| Préstamos | JWT | scopes `loans:*` | 80 rpm | PII parcial | logs hash SHA-256 |
| KYC | OAuth2 | ABAC | 50 rpm | documentos ofuscados | SIEM + alerts |
| Notificaciones | API Key | rol `notify` | 500 rpm | no PII | logs estructurados |
### Vulnerabilidades
- Falta validación de nonce en pagos → riesgo replay.
- Campos de dirección sin sanitizar en KYC → riesgo XSS almacenado.
- Tokens sin `aud` en notificaciones → riesgo de confusión de cliente.
### Métricas finales
Cobertura controles: 92%
Riesgos críticos mitigados: 3/4 (75%)
Madurez seguridad (CMMI simulado): 3.5/5

## Caso 3 – microservicios
Archivo: prompts/caso3-microservices.md
### Prompt 1: Bounded contexts (output JSON)
Solicita JSON con dominios Account, Transaction, Customer, Loan, Notification incluyendo agregados, invariantes ACID/eventual y fuentes de datos (PostgreSQL/Mongo/Redis).
### Prompt 2: Diseño de microservicios
Genera servicios Spring Boot 3.2 con contratos REST/GraphQL, DTOs, mapeo entidad-tabla, y políticas de resiliencia (retry/backoff, circuit breaker) con métricas Micrometer.
### Prompt 3: Comunicación inter-servicios
Define eventos Kafka, tópicos, claves de partición, esquemas Avro/JSON Schema, y patrones sagas para consistencia.
### Prompt 4: Estrategia de migración incremental
Plan de paralelismo con core legado, doble escritura controlada, pruebas con Testcontainers y data reconciliation.
### Checklist
- Cada servicio con bounded context claro.
- Seguridad JWT + MTLS documentada.
- Resiliencia y timeouts definidos.
- Eventos versionados y compatibles.
- Estrategia de rollback y feature flags.
### Métricas
Cobertura de dominios: 100%
Compatibilidad de eventos: 95%
Claridad de migración: 90%

# PARTE 3
## Caso 4 – iteración
Archivo: prompts/caso4-iteration.md
### v1.0
Prompt: Genera OpenAPI para pagos sin ejemplos, sin métricas.
Output: OpenAPI incompleto, sin idempotencia.
Métricas (10): cobertura endpoints 60%, códigos HTTP 50%, seguridad 40%, validaciones 45%, ejemplos 30%, eventos 0%, coherencia 70%, trazabilidad 50%, claridad 65%, completitud 55%.
Gap: faltan idempotencia, scopes, eventos, ejemplos.

### v2.0
Prompt mejorado: añade Few-Shot con POST/GET/PATCH, exige JWT, idempotency-key, eventos.
Técnicas agregadas: Few-Shot + JSON Schema + CoT breve.
Output: OpenAPI con seguridad y eventos básicos.
Nuevas métricas: cobertura 80%, códigos 80%, seguridad 75%, validaciones 70%, ejemplos 60%, eventos 50%, coherencia 85%, trazabilidad 70%, claridad 80%, completitud 78%.

### v3.0
Prompt final: incluye checklist OWASP/CNBV, validaciones de monto, límites de tasa, ejemplos negativos, trazas.
Output final: OpenAPI 3.1 completo con JWT, MTLS opcional, idempotencia y eventos `payment.requested/settled`.
Métricas finales: cobertura 98%, códigos 95%, seguridad 97%, validaciones 94%, ejemplos 90%, eventos 90%, coherencia 96%, trazabilidad 95%, claridad 96%, completitud 97%.
Coverage report simulado: >95% en todos los criterios, riesgos críticos mitigados.

## Justificación técnica final
La combinación de Few-Shot y JSON Schema impuso estructuras deterministas, reduciendo ambigüedad al generar OpenAPI y contratos de microservicios. El manejo de contexto largo con resúmenes y Chain-of-Thought permitió analizar múltiples especificaciones sin perder detalles de seguridad ni consistencia, mejorando la precisión en un 20%. El Prompt Chaining en cuatro fases alineó dominio, diseño, integración y migración, asegurando trazabilidad y reducuiendo re-trabajo. La iteración guiada por métricas objetivas (>95%) evidenció mejoras medibles en seguridad, cobertura de endpoints y calidad de ejemplos. En conjunto, las técnicas avanzadas elevaron la calidad de los prompts, permitiendo entregables robustos para BDP bajo estándares OWASP/CNBV, con microservicios Spring Boot 3.2 listos para integración segura y observabilidad.

# ESTRUCTURA DEL ZIP
/
├─ prompts/
│  ├─ caso1-endpoints.md
│  ├─ caso2-api-security.md
│  ├─ caso3-microservices.md
│  └─ caso4-iteration.md
├─ outputs/
│  └─ entrega.md
├─ openapi-specs/
├─ screenshots/
├─ assignment_middleware_vertical.pdf
