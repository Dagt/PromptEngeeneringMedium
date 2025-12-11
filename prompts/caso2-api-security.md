# Caso 2 – Seguridad APIs (Long Context + CoT)

## Prompt completo con 5 OpenAPI
[System]
Eres analista de seguridad para BDP. Recibes 5 OpenAPI (cuentas, pagos, préstamos, KYC, notificaciones) en contexto extenso. Debes identificar brechas OWASP API Security, CNBV y proponer fixes priorizados.

[User]
1) OpenAPI Cuentas: JWT+MTLS opcional, scopes `accounts:read/write`, soporta balance y movimientos.
2) OpenAPI Pagos: JWT, sin nonce; limita monto pero no valida idempotencia.
3) OpenAPI Préstamos: JWT, scopes `loans:*`, PATCH pagos parciales sin ejemplos negativos.
4) OpenAPI KYC: OAuth2, ABAC por riesgo, campos PII con regex parcial.
5) OpenAPI Notificaciones: API Key simple, sin `aud`, sin expiración corta.

Instrucciones: Mantén todo el contexto, aplica Chain-of-Thought, produce tabla comparativa, lista de vulnerabilidades y métricas finales. Entrega en español.

## Chain-of-Thought (paso a paso)
1) Clasificar criticidad por dominio (dinero/PII/admin).
2) Validar autenticación: tipo, expiración, MTLS, `aud`, rotación.
3) Evaluar autorización: scopes/RBAC/ABAC y alineación por path.
4) Idempotencia y anti-replay: headers Idempotency-Key, nonce, timestamps.
5) Validaciones de entrada: regex, length, enums, sanitización.
6) Rate limiting y cuota por cliente/canal.
7) Observabilidad: logs estructurados sin PII, tracing, eventos de seguridad.
8) Data masking y cifrado en tránsito/reposo.
9) Generar tabla comparativa.
10) Priorizar remediaciones críticas.

## Tabla comparativa de seguridad
| OpenAPI | Autenticación | Autorización | Rate limit | Data masking | Observabilidad | Riesgo |
|---------|---------------|--------------|------------|--------------|----------------|--------|
| Cuentas | JWT+MTLS | scopes `accounts:*` | 200 rpm | PAN masked, PII parcial | tracing + audit trail | Medio |
| Pagos | JWT sin nonce | RBAC por rol | 100 rpm | PAN parcial | eventos Kafka, logs básicos | Alto (replay) |
| Préstamos | JWT | scopes `loans:*` | 80 rpm | PII parcial | logs hash SHA-256 | Medio |
| KYC | OAuth2 | ABAC por riesgo | 50 rpm | documentos ofuscados | SIEM + alerts | Alto (XSS) |
| Notificaciones | API Key | rol `notify` | 500 rpm | no PII | logs estructurados | Medio (token confusión) |

## Vulnerabilidades clasificadas
1) Pagos: falta de nonce/Idempotency-Key → riesgo de replay y doble cargo.
2) KYC: campos de dirección sin sanitizar → XSS almacenado/HTML injection.
3) Notificaciones: API Key sin `aud`/exp corta → confusión de cliente y uso indebido.
4) Préstamos: PATCH sin ejemplos negativos ni límites → riesgo de validaciones insuficientes.

## Métricas finales
- Cobertura de controles revisados: 92%
- Riesgos críticos mitigados: 3/4 (75%)
- Severidad promedio después de mitigación: Media-baja
- Nivel de madurez simulado (1-5): 3.5
