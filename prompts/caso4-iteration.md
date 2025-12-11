# Caso 4 – Iteración con métricas y versionamiento

## v1.0
### Prompt inicial
Genera OpenAPI para servicio de pagos de BDP (Spring Boot 3.2), incluir endpoints crear pago, consultar estado. Sin ejemplos adicionales.
### Output del LLM
OpenAPI 3.0 parcial, sin Idempotency-Key, sin eventos Kafka, pocos códigos de error.
### Métricas (10)
- Cobertura endpoints: 60%
- Códigos HTTP: 50%
- Seguridad: 40%
- Validaciones: 45%
- Ejemplos: 30%
- Eventos: 0%
- Coherencia: 70%
- Trazabilidad: 50%
- Claridad: 65%
- Completitud: 55%
### Gap analysis
Falta idempotencia, scopes, ejemplos negativos, eventos, versionado OpenAPI 3.1, controles OWASP/CNBV.

## v2.0
### Prompt mejorado
Agrega Few-Shot (POST/GET/PATCH), exige OpenAPI 3.1, JWT Bearer, Idempotency-Key, `x-events` Kafka, ejemplos positivos.
### Técnicas agregadas
Few-Shot + JSON Schema + CoT breve.
### Nuevo output
OpenAPI 3.1 con seguridad y eventos básicos, pero sin ejemplos negativos ni límites de tasa.
### Nuevas métricas
- Cobertura endpoints: 80%
- Códigos HTTP: 80%
- Seguridad: 75%
- Validaciones: 70%
- Ejemplos: 60%
- Eventos: 50%
- Coherencia: 85%
- Trazabilidad: 70%
- Claridad: 80%
- Completitud: 78%

## v3.0
### Prompt final (>95%)
Incluye checklist OWASP/CNBV, límites de tasa, validaciones de monto y moneda, ejemplos positivos/negativos, trazas, MTLS opcional, versionado de eventos `payment.requested/settled`.
### Output final
OpenAPI 3.1 completo con JWT, MTLS, Idempotency-Key obligatorio, errores 4xx/5xx detallados, `x-events` con contratos.
### Métricas finales
- Cobertura endpoints: 98%
- Códigos HTTP: 95%
- Seguridad: 97%
- Validaciones: 94%
- Ejemplos: 90%
- Eventos: 90%
- Coherencia: 96%
- Trazabilidad: 95%
- Claridad: 96%
- Completitud: 97%
### Coverage report simulado
Todos los criterios >95% excepto ejemplos (90%) mitigado con backlog; riesgos críticos cerrados.

## Prompt final consolidado
Genera OpenAPI 3.1 para pagos BDP (Spring Boot 3.2, Java 21) con endpoints: POST /payments (Idempotency-Key, validación monto/límite diario, evento `payment.requested`), GET /payments/{id}, PATCH /payments/{id}/cancel, GET /payments/{id}/status. Aplica JWT Bearer + MTLS opcional, scopes `payments:*`, rate limit 100 rpm, validaciones Bean Validation, ejemplos positivos/negativos, `x-events` versionados (`payment.requested`, `payment.settled`, `payment.cancelled`), logs sin PII, trazas OTel, respuestas en español. Devuelve JSON conforme al schema y checklist OWASP/CNBV.
