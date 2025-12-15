# Prompt Análisis de Seguridad APIs - v1.1

## Contexto
- Rol: Senior Backend / Middleware Security Engineer.
- Objetivo: Análisis de cumplimiento OWASP API Security Top 10 y regulaciones CNBV sobre 5 especificaciones OpenAPI existentes en el repositorio.
- Técnica obligatoria: Long Context para cargar los 5 YAML completos en una sola ejecución, aplicando Chain-of-Thought detallado.
- Alcance: No modificar objetivos, criterios ni simplificar el análisis; no generar el análisis todavía (solo el prompt).

## Input explícito (pegar los 5 YAML completos)
1. Copia y pega **íntegro** el contenido de cada archivo OpenAPI ubicado en el repositorio, respetando el orden y encabezados indicados.
2. Cada YAML debe incluir un encabezado claro con el nombre del archivo:
   - `=== openapi-specs/payment-service-api.yaml ===`
   - `=== openapi-specs/loan-service-api.yaml ===`
   - `=== openapi-specs/card-service-api.yaml ===`
   - `=== openapi-specs/notification-service-api.yaml ===`
   - `=== openapi-specs/audit-service-api.yaml ===`
3. Estos archivos YA existen en el repositorio y son la única fuente autorizada de verdad para el análisis.

## Proceso de análisis (Chain-of-Thought obligatorio)
El LLM debe razonar paso a paso, sin omitir ni reordenar:
1. **Identificar security schemes declarados** (`securitySchemes`, tipos OAuth2/JWT bearer, scopes).
2. **Verificar @SecurityRequirement en endpoints críticos** (pagos, préstamos, tarjetas, notificaciones, auditoría) y cuantificar cobertura.
3. **Validar presencia de rate limiting** (headers `X-RateLimit-*` u otros mecanismos documentados).
4. **Revisar validaciones de input** (schemas, `required`, `pattern`, `format`, límites numéricos) en todos los request bodies y parámetros.
5. **Analizar error responses** para confirmar que no exponen stack traces ni información sensible; evaluar consistencia de códigos y payloads.
6. **Generar recomendaciones de hardening** específicas por API, alineadas a OWASP y CNBV.

## Formato de salida obligatorio
- **Razonamiento paso a paso** completo del Chain-of-Thought con referencias a cada archivo YAML.
- **Tabla comparativa de seguridad** (una fila por API) con columnas mínimas: `API Spec`, `Security Scheme`, `Auth en Endpoints`, `Rate Limit`, `Input Validation`, `Error Handling`, `Cumple OWASP`.
- **Lista de vulnerabilidades** clasificadas por severidad (CRITICAL, HIGH, MEDIUM, LOW) con evidencia y referencia de archivo/endpoint.
- **Métricas globales**: cantidad de APIs compliant, endpoints inseguros, conteo por severidad, score promedio de seguridad.

## Instrucciones de guardado
- Pega el **resultado completo del LLM** en `analisis-seguridad/caso2-api-security-output.md` dentro del repositorio, preservando el formato anterior.

## Criterios de evaluación
- Alineación con OWASP API Security Top 10 y requisitos CNBV.
- Versionamiento obligatorio `/api/v1` documentado en las especificaciones.
- Seguridad: `securitySchemes` definidos y aplicados; endpoints críticos con autenticación/autorización.
- Rate limiting documentado y verificable.
- Validación de entrada exhaustiva; ausencia de stack traces o datos sensibles en errores.
- Tono técnico, corporativo y evaluable; sin supuestos ni invención de datos fuera de los YAML provistos.
