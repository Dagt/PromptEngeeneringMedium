# Prompt Chaining - Microservices Decomposition - v1.0

## Prompt 1: Extracción de Bounded Contexts (DDD)
Contexto: Monolito de gestión de préstamos (45k LOC) con procesos de solicitud, aprobación (workflow multi-nivel), desembolso ACID, amortización cacheada, pagos parciales, intereses/penalizaciones, reestructuración, cancelación anticipada, reportes CNBV diarios 00:00, notificaciones multicanal y scoring externo (latencia ~500ms).  
Instrucción: Identifica bounded contexts y dominios usando DDD. Devuelve JSON con `name`, `capabilities`, `entities`, `events` y `constraints` (incluye restricciones ACID, uso de cache, dependencias externas). No inventes capacidades fuera del contexto descrito.

### Output Prompt 1
Ver JSON generado en `arquitectura-microservicios/caso3-microservices-output.md` (9 bounded contexts: LoanApplication, LoanApprovalWorkflow, Disbursement, Amortization, Payments, Restructuring, EarlyClosure, RegulatoryReporting, Notifications).

## Prompt 2: Diseño de Microservicios
Usa el JSON de bounded contexts anterior para definir microservicios. Para cada servicio incluye: `responsabilidades`, `APIs (REST/GraphQL/gRPC)`, `mensajería (Kafka/RabbitMQ)`, `data store` (PostgreSQL/MongoDB/Redis), `seguridad` (OAuth2/JWT), `observabilidad` (Micrometer/Otel), `tests` (unit/integration/contract), `SLO` (latencia P99). Respeta restricciones: desembolsos ACID, scoring externo con fallback, amortización cacheada, pagos parciales complejos.

### Output Prompt 2
Arquitectura propuesta en `arquitectura-microservicios/caso3-microservices-output.md` con 10 servicios (application-service, approval-workflow-service, disbursement-service, amortization-service, payments-service, restructuring-service, early-closure-service, reporting-service, notification-service, scoring-adapter) detallando responsabilidades, APIs, data stores y observabilidad.

## Prompt 3: Comunicación Inter-Servicios
Basado en los microservicios definidos, elabora un diagrama textual que indique para cada interacción si es REST síncrono, Kafka asíncrono o gRPC. Incluye circuit breakers, timeouts, retries y estrategias de idempotencia. Destaca dónde aplica eventual consistency vs. ACID.

### Output Prompt 3
Comunicación descrita en `arquitectura-microservicios/caso3-microservices-output.md`: REST/gRPC/Kafka por interacción, con timeouts, retries, circuit breakers e idempotencia.

## Prompt 4: Estrategia de Migración
Genera un plan Strangler Fig incremental por fases, incluyendo: slicing funcional, feature toggles, migración de datos, doble escritura/lectura, validaciones paralelas, rollback seguro, corte definitivo y criterios de éxito por fase. Considera riesgos por scoring externo y cumplimiento CNBV.

### Output Prompt 4
Plan Strangler Fig por fases documentado en `arquitectura-microservicios/caso3-microservices-output.md`, incluyendo shadow mode, doble escritura y corte final.

## Validación de Arquitectura
Evalúa la propuesta contra estos criterios (marcar ✅ / ❌):
- Cada microservicio tiene una sola responsabilidad
- No hay dependencias cíclicas entre servicios
- Transacciones ACID manejadas correctamente
- Eventual consistency manejada donde es aceptable
- Fallback strategies para dependencias externas (buró)
- Data ownership bien definido (no shared databases)
- Estrategia de migración es incremental (low risk)

## Métricas
- Microservicios propuestos: 10
- Promedio de responsabilidades por servicio: 1-2 (objetivo: 1-3)
- Llamadas síncronas vs asíncronas: 60% / 40%
- Riesgo de migración: MEDIUM (por dependencias ACID y core bancario)
- Complejidad estimada (story points): 210

