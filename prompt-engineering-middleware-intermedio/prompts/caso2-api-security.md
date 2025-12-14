# Prompt Análisis de Seguridad APIs - v1.0

## Contexto
Eres un auditor de seguridad especializado en OWASP API Security Top 10 y cumplimiento CNBV. Analiza 5 especificaciones OpenAPI ubicadas en `openapi-specs/`: `payment-service-api.yaml`, `loan-service-api.yaml`, `card-service-api.yaml`, `notification-service-api.yaml`, `audit-service-api.yaml`. Debes cargar todas en un solo contexto (long context) y aplicar Chain-of-Thought paso a paso. No inventes datos: solo concluye con evidencia explícita.

## Prompt (Long Context + CoT)
1. Carga el contenido completo de cada archivo, delimitado con encabezados claros: `=== PAYMENT ===`, `=== LOAN ===`, etc.  
2. Sigue estrictamente estos pasos (sin saltar ninguno):
   - Paso 1: Identificar `securitySchemes` declarados y tipos (OAuth2, JWT bearer).  
   - Paso 2: Verificar `@SecurityRequirement` o equivalentes en endpoints críticos (pagos, préstamos, tarjetas, notificaciones, auditoría).  
   - Paso 3: Validar presencia de rate limiting (headers `X-RateLimit-*`).  
   - Paso 4: Revisar validaciones de input (schemas, patterns, formatos, required).  
   - Paso 5: Analizar `responses` de error y confirmar que no exponen información sensible.  
   - Paso 6: Generar recomendaciones de hardening específicas por API.
3. Usa un tono objetivo y marca cada afirmación con el archivo donde se encontró la evidencia.  
4. No asumas cumplimiento si no está definido.  
5. Devuelve también métricas agregadas solicitadas.

## Razonamiento del LLM (Chain-of-Thought)
_Pegar aquí el análisis paso a paso generado por el modelo, referenciado por archivo._

## Tabla de Resultados de Seguridad
| API Spec | Security Scheme | Auth en Endpoints | Rate Limit | Input Validation | Error Handling | Cumple OWASP |
|----------|----------------|-------------------|-----------|-----------------|----------------|--------------|
| Payment Service | ✅ /❌ | X/Y endpoints | ✅ /❌ | X% | ✅ /❌ | ✅ /❌ |
| Loan Service | ✅ /❌ | X/Y endpoints | ✅ /❌ | X% | ✅ /❌ | ✅ /❌ |
| Card Service | ✅ /❌ | X/Y endpoints | ✅ /❌ | X% | ✅ /❌ | ✅ /❌ |
| Notification Service | ✅ /❌ | X/Y endpoints | ✅ /❌ | X% | ✅ /❌ | ✅ /❌ |
| Audit Service | ✅ /❌ | X/Y endpoints | ✅ /❌ | X% | ✅ /❌ | ✅ /❌ |

## Vulnerabilidades Detectadas
_Listar vulnerabilidades por severidad (CRITICAL, HIGH, MEDIUM, LOW) con referencia al archivo y endpoint._

## Métricas
- APIs que cumplen 100% criterios: X/5 (X%)
- Endpoints sin autenticación detectados: XX
- Vulnerabilidades CRITICAL: XX
- Vulnerabilidades HIGH: XX
- Score promedio de seguridad: XX/100

