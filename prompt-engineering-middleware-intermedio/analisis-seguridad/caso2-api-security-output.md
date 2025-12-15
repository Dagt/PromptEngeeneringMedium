# Análisis de Seguridad APIs - Caso 2

## Razonamiento paso a paso (Chain-of-Thought)
1. **Identificación de security schemes**: `payment-service` usa `BearerAuth` (JWT bearer); `loan-service` define `OAuth2` (authorizationCode); `card-service` no declara `securitySchemes`; `notification-service` usa `JWTAuth` (bearer); `audit-service` expone `ApiKeyAuth`.
2. **Verificación de seguridad en endpoints**: `payment-service` aplica seguridad global a sus 2 operaciones. `loan-service` hereda seguridad global para `/loans/applications`, `/loans/{loanId}` y `/loans/{loanId}/approve`. `card-service` carece de seguridad en 5 endpoints que manejan PAN/CVV. `notification-service` protege `/notifications`, `/notifications/{notificationId}` y `/notifications/batch` con JWT. `audit-service` protege `POST /audit/logs` y `GET /audit/logs/{logId}`, pero `GET /audit/logs` y `GET /audit/reports/compliance` quedan abiertos.
3. **Rate limiting**: `payment-service` documenta cabeceras `X-RateLimit-*`; `notification-service` define límites y `Retry-After`. No hay rate limiting en `loan-service`, `card-service` ni `audit-service`.
4. **Validaciones de input**: `payment-service` aplica `pattern`, `minimum/maximum` y enums. `loan-service` define `required` pero omite límites en parámetros de path/query y deja `stackTrace` en errores. `card-service` no valida PAN/CVV ni longitudes y expone `debugInfo`. `notification-service` valida patrones (`CUST-...`, `TPL-...`, enums) y objetos controlados. `audit-service` valida formatos (`ipv4`, patrones para `userId` y `logId`) y rangos para año/mes en reportes.
5. **Errores sin información sensible**: `loan-service` expone `stackTrace` completo en 500. `card-service` devuelve `debugInfo` con query SQL, `userId` y `sessionToken` (riesgo crítico). `audit-service` muestra `path` y `method` pero sin secretos; `payment-service` y `notification-service` usan errores acotados.
6. **Recomendaciones de hardening**: Añadir seguridad obligatoria en `card-service` y endpoints abiertos de `audit-service`; eliminar `stackTrace` y `debugInfo` sensibles; documentar rate limiting en todas las APIs; reforzar validaciones de PAN/CVV, límites numéricos y formatos de contacto; asegurar CORS y versionamiento consistente.

## Tabla de Resultados de Seguridad
| API Spec | Security Scheme | Auth en Endpoints | Rate Limit | Input Validation | Error Handling | Cumple OWASP |
|----------|-----------------|-------------------|-----------|------------------|----------------|--------------|
| Payment Service | ✅ JWT bearer | 2/2 protegidos | ✅ X-RateLimit headers | Alto (patterns y límites) | Genérico sin datos sensibles | Parcial (falta CORS explícito) |
| Loan Service | ✅ OAuth2 | 3/3 protegidos (heredado) | ❌ | Medio (required, sin límites en paths) | ❌ Incluye stackTrace en 500 | ❌ |
| Card Service | ❌ Sin scheme | 0/5 protegidos | ❌ | Bajo (sin patterns para PAN/CVV) | ❌ Devuelve debugInfo con datos sensibles | ❌ |
| Notification Service | ✅ JWT bearer | 3/3 protegidos | ✅ Rate limit documentado | Alto (patterns y enums) | Genérico sin datos sensibles | Parcial (revisar CORS enforcement) |
| Audit Service | ApiKey | 2/4 protegidos | ❌ | Medio (patrones y rangos básicos) | Genérico (exposición moderada de path/method) | ❌ |

## Vulnerabilidades Detectadas
- **CRITICAL**: `card-service` sin autenticación expone PAN/CVV y `debugInfo` con tokens; `loan-service` publica `stackTrace` completo; `audit-service` deja endpoints abiertos sin ApiKey.
- **HIGH**: Falta de rate limiting en loan/card/audit; validaciones débiles de PAN/CVV; ausencia de CORS documentado en varias APIs.
- **MEDIUM**: Parámetros sin límites en loan/audit; falta de ejemplos 401/403 en algunos errores.
- **LOW**: Mensajes de error poco estandarizados.

## Métricas
- APIs que cumplen 100% criterios: **0/5** (Payment y Notification cumplen parcialmente; faltan CORS/rate limit consistentes en todas).
- Endpoints sin autenticación detectados: **7** (5 en card-service, 2 en audit-service).
- Vulnerabilidades CRITICAL: **3**; HIGH: **5**; MEDIUM: **3**.
- Score promedio de seguridad: **58/100** (principales brechas: autenticación y sanitización de errores).
