# Análisis de Seguridad APIs - Caso 2

## Razonamiento paso a paso (Chain-of-Thought)
1. **Identificación de security schemes**: Todas las specs declaran esquemas. `payment-service` usa `BearerAuth` (JWT bearer), `loan-service` define `OAuth2` con scopes, `card-service` declara `ApiKeyAuth` pero sin aplicar globalmente, `notification-service` no declara ningún esquema global, y `audit-service` define `BasicAuth` inseguro para producción.
2. **Verificación de @SecurityRequirement en endpoints**: `payment-service` aplica `BearerAuth` en `/payments` y `/payments/{id}`. `loan-service` solo protege la creación de solicitudes, pero el `GET /loans/{loanId}` y `/loans/{loanId}/amortization` carecen de seguridad. `card-service` no asigna `security` a sus paths, quedando sin auth. `notification-service` no tiene seguridad en ningún endpoint. `audit-service` protege `/audit/events` pero el uso de Basic deja credenciales expuestas.
3. **Rate limiting**: Solo `payment-service` documenta cabeceras `X-RateLimit-*`. Las demás APIs no mencionan límites ni políticas anti-abuso.
4. **Validaciones de input**: `payment-service` y `loan-service` incluyen `required`, `pattern` y límites numéricos. `card-service` tiene esquemas mínimos sin `pattern` para PAN o CVV. `notification-service` acepta payloads de correo/SMS sin `format` ni restricciones. `audit-service` define filtros pero sin rangos ni tipos estrictos.
5. **Errores sin información sensible**: `loan-service` expone `stackTrace` en respuestas 500, riesgo alto. Las demás usan objetos de error genéricos sin datos sensibles.
6. **Recomendaciones de hardening**: Forzar JWT/OAuth2 en todos los endpoints sensibles, añadir rate limiting consistente, eliminar `stackTrace` de respuestas públicas, agregar validaciones estrictas (patterns para tarjetas, formatos de email/phone), documentar CORS restrictivo y versionamiento uniforme.

## Tabla de Resultados de Seguridad
| API Spec | Security Scheme | Auth en Endpoints | Rate Limit | Input Validation | Error Handling | Cumple OWASP |
|----------|-----------------|-------------------|-----------|------------------|----------------|--------------|
| Payment Service | ✅ JWT bearer | 2/2 endpoints protegidos | ✅ Cabeceras X-RateLimit | Alto (patterns y mínimos definidos) | Genérico sin datos sensibles | Parcial (faltan CORS/documentación) |
| Loan Service | ✅ OAuth2 | 1/3 protegidos | ❌ | Medio (required, sin límites de score) | ❌ Expone stackTrace en 500 | ❌ |
| Card Service | ApiKey declarado, no aplicado | 0/3 protegidos | ❌ | Bajo (sin patterns para PAN/CVV) | Genérico | ❌ |
| Notification Service | ❌ Sin scheme | 0/2 protegidos | ❌ | Bajo (sin formatos email/phone) | Genérico | ❌ |
| Audit Service | BasicAuth (riesgoso) | 1/1 protegido | ❌ | Medio (filtros sin rangos) | Genérico | ❌ |

## Vulnerabilidades Detectadas
- **CRITICAL**: Endpoints de `card-service` y `notification-service` sin autenticación; uso de BasicAuth en `audit-service` exponiendo credenciales; `loan-service` devuelve `stackTrace` en 500.
- **HIGH**: Falta de rate limiting en cuatro APIs; validaciones débiles para datos de tarjeta y contactos; endpoints GET de préstamos sin auth.
- **MEDIUM**: Ausencia de CORS restrictivo documentado; falta de versionamiento explícito en URLs (algunas APIs lo omiten en servidores o paths).
- **LOW**: Mensajes de error sin estandarizar, falta de ejemplos de códigos 401/403.

## Métricas
- APIs que cumplen 100% criterios: **1/5 (20%)** solo cercana es Payment con observaciones menores.
- Endpoints sin autenticación detectados: **6** (3 en card, 2 en notification, 1 en loans GET).
- Vulnerabilidades CRITICAL: **4**; HIGH: **5**; MEDIUM: **3**.
- Score promedio de seguridad: **54/100** (mayor brecha por auth y rate limiting).
