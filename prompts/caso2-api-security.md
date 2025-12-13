# Prompt Análisis de Seguridad APIs - v1.0

## Contexto
Analiza simultáneamente 5 especificaciones OpenAPI para validar cumplimiento de seguridad (OWASP API Security Top 10 + CNBV). Usa contexto largo para incluir las cinco especificaciones y aplica Chain-of-Thought (CoT) para razonar paso a paso.

## Especificaciones a incluir (pega el contenido completo de cada YAML)
- `payment-service-api.yaml`
- `loan-service-api.yaml`
- `card-service-api.yaml`
- `notification-service-api.yaml`
- `audit-service-api.yaml`

## Instrucciones del Prompt
Rol del sistema: "Eres un auditor de seguridad de APIs experto en OWASP API Security Top 10, OAuth2/JWT, y regulaciones CNBV."

Pasos CoT obligatorios (el modelo debe mostrarlos explícitamente):
1. Identificar `security schemes` declarados.
2. Verificar `@SecurityRequirement` o equivalentes en endpoints críticos.
3. Validar presencia de rate limiting (cabeceras `X-RateLimit-*`).
4. Revisar validaciones de input (schemas, patterns, constraints).
5. Analizar respuestas de error (no exponer stack traces ni información sensible).
6. Generar recomendaciones de hardening priorizadas.

Formato de salida esperado:
1. **Razonamiento paso a paso (CoT visible)** para cada especificación.
2. **Tabla de Resultados de Seguridad** con columnas: API Spec | Security Scheme | Auth en Endpoints | Rate Limit | Input Validation | Error Handling | Cumple OWASP.
3. **Vulnerabilidades Detectadas** listadas por severidad (CRITICAL, HIGH, MEDIUM, LOW).
4. **Métricas agregadas**:
   - APIs que cumplen 100% criterios: X/5 (X%)
   - Endpoints sin autenticación detectados: XX
   - Vulnerabilidades CRITICAL: XX
   - Vulnerabilidades HIGH: XX
   - Score promedio de seguridad: XX/100

## Cadena de llamadas sugerida
1. Cargar los 5 YAML completos en el prompt.
2. Pedir al modelo que ejecute los 6 pasos de CoT con numeración explícita.
3. Solicitar la tabla y métricas al final, separadas del razonamiento.

## Output del LLM
- Razonamiento CoT: `[pendiente]`
- Tabla de Resultados de Seguridad: `[pendiente]`
- Vulnerabilidades detectadas: `[pendiente]`
- Métricas agregadas: `[pendiente]`
