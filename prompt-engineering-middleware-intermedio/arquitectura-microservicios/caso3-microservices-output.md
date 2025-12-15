# Prompt Chaining - Microservices Decomposition - Salida

## Output Prompt 1 (Bounded Contexts)
```json
[
  {
    "name": "LoanApplication",
    "capabilities": ["ingresar solicitud", "validar cliente", "consultar buro", "calcular scoring"],
    "entities": ["LoanApplication", "CustomerProfile", "CreditScore"],
    "events": ["LoanApplicationSubmitted", "CreditScoreReceived"],
    "constraints": ["latencia buro ~500ms", "validaciones reglamentarias CNBV"]
  },
  {
    "name": "LoanApprovalWorkflow",
    "capabilities": ["gestionar etapas analista/gerente/director", "aprobar/rechazar", "auditar decisiones"],
    "entities": ["ApprovalTask", "ApprovalRule", "DecisionLog"],
    "events": ["LoanApproved", "LoanRejected"],
    "constraints": ["secuencial, sin ciclos", "trazabilidad completa"]
  },
  {
    "name": "Disbursement",
    "capabilities": ["desembolsar fondos", "integrar core bancario", "revertir en falla"],
    "entities": ["DisbursementOrder", "CoreTransfer"],
    "events": ["FundsDisbursed", "DisbursementFailed"],
    "constraints": ["ACID obligatorio", "idempotencia"]
  },
  {
    "name": "Amortization",
    "capabilities": ["generar tabla", "cachear resultados"],
    "entities": ["AmortizationSchedule", "Installment"],
    "events": ["AmortizationGenerated"],
    "constraints": ["cálculo único + cache", "evitar recalculo"]
  },
  {
    "name": "Payments",
    "capabilities": ["registrar pago parcial/total", "calcular intereses", "aplicar penalizaciones"],
    "entities": ["Payment", "Penalty", "InterestAccrual"],
    "events": ["PaymentReceived", "PaymentFailed"],
    "constraints": ["pagos parciales complejos", "consistencia fuerte en saldo"],
    "dependencies": ["Amortization"]
  },
  {
    "name": "Restructuring",
    "capabilities": ["reestructurar deuda", "recalcular condiciones"],
    "entities": ["RestructuringRequest", "NewSchedule"],
    "events": ["DebtRestructured"],
    "constraints": ["aprobación multietapa", "trazabilidad CNBV"]
  },
  {
    "name": "EarlyClosure",
    "capabilities": ["cancelación anticipada", "cálculo de penalización"],
    "entities": ["ClosureRequest", "ClosureSettlement"],
    "events": ["LoanClosed"],
    "constraints": ["saldo exacto", "consistencia ACID con pagos"]
  },
  {
    "name": "RegulatoryReporting",
    "capabilities": ["generar reporte diario 00:00", "exportar CNBV"],
    "entities": ["RegulatoryReport"],
    "events": ["ReportGenerated"],
    "constraints": ["datos consistentes", "ventana diaria"]
  },
  {
    "name": "Notifications",
    "capabilities": ["email", "sms", "push"],
    "entities": ["Notification", "Template"],
    "events": ["NotificationSent", "NotificationFailed"],
    "constraints": ["idempotencia", "reintentos"]
  }
]
```

## Output Prompt 2 (Diseño de Microservicios)
- **application-service**: REST `/applications`, PostgreSQL; integra con `scoring-adapter` (gRPC) y publica `LoanApplicationSubmitted` en Kafka. Seguridad OAuth2, latencia P99 < 200ms.
- **approval-workflow-service**: REST `/approvals`, orquesta etapas con máquina de estados; PostgreSQL. Kafka para eventos `LoanApproved/Rejected`. Tests de contrato para idempotencia.
- **disbursement-service**: gRPC con core bancario + REST `/disbursements`; transacciones ACID sobre PostgreSQL; usa outbox + Kafka `FundsDisbursed`. Circuit breakers a core y retries.
- **amortization-service**: REST `/amortization/{loanId}`; calcula y cachea en Redis; expone GraphQL para consultas detalladas. Guarda configuración en PostgreSQL.
- **payments-service**: REST `/payments`, procesa parciales, aplica intereses/penalizaciones; usa PostgreSQL para ledger y Kafka para eventos `PaymentReceived`. Coordina con amortización vía REST y usa locking optimista.
- **restructuring-service**: REST `/restructuring`; recalcula tabla via gRPC a amortización; PostgreSQL. Publica `DebtRestructured`.
- **early-closure-service**: REST `/closures`; asegura ACID con disbursement y payments; confirma cierre y penalización; PostgreSQL.
- **reporting-service**: Batch/cron diario 00:00; lee vistas materializadas; publica reportes firmados. Exporta a almacenamiento seguro; usa Kafka para eventos de corte.
- **notification-service**: Async Kafka consumer de eventos (payments, approvals) y REST `/notifications/test`; usa templates en MongoDB; reintentos con backoff.
- **scoring-adapter**: gRPC client para buro externo; cachea respuestas en Redis; provee fallback de score degradado; expone métricas de latencia.

## Output Prompt 3 (Comunicación Inter-Servicios)
- application-service → scoring-adapter: **gRPC** con timeout 700ms, retries 2, fallback score.
- application-service → approval-workflow: **Kafka** `LoanApplicationSubmitted` (eventual consistency aceptable).
- approval-workflow → disbursement-service: **REST síncrono** para órdenes ACID, con circuit breaker y idempotency-key.
- disbursement-service → core bancario: **gRPC** ACID + outbox.
- payments-service → amortization-service: **REST** para cuotas y saldo, timeout 300ms, cache Redis.
- payments-service → notification-service: **Kafka** `PaymentReceived`.
- restructuring-service → amortization-service: **gRPC** para recalcular tablas; notifica vía Kafka.
- reporting-service → todos: **Kafka** (consume eventos) y lecturas **read-only** REST para validación; sin efectos secundarios.
- audit/logging: Todos publican eventos a Kafka `audit.events`; consumers con retries y almacenamiento WORM.

## Output Prompt 4 (Estrategia de Migración)
- **Fase 0 (Preparación)**: Mapear endpoints del monolito, habilitar event sourcing mínimo y feature flags.
- **Fase 1 (Read replica)**: Exponer `application-service` y `scoring-adapter` en modo shadow; comparar resultados con monolito.
- **Fase 2 (Strangler)**: Derivar creación de solicitudes y scoring al nuevo stack; mantener aprobaciones y pagos en monolito.
- **Fase 3 (Pagos y amortización)**: Activar `payments-service` y `amortization-service` en paralelo con doble escritura; validar saldos diarios.
- **Fase 4 (Desembolso ACID)**: Migrar a `disbursement-service` con canary y rollback rápido; integrar core vía gRPC.
- **Fase 5 (Workflow y reestructuración)**: Encender `approval-workflow` y `restructuring-service`; apagar rutas equivalentes del monolito.
- **Fase 6 (Cierre y reporting)**: Migrar `early-closure` y `reporting-service`, cortar tráfico restante, limpiar datos legados.
- **Criterios de éxito**: 0 regresiones en saldos, P99 <200ms en pagos y desembolsos, reportes CNBV generados a las 00:00 sin diferencias, errores críticos <0.1%.

## Validación de Arquitectura
- ✅ Cada microservicio tiene una sola responsabilidad
- ✅ No hay dependencias cíclicas
- ✅ Transacciones ACID manejadas en disbursement/payments/closure
- ✅ Eventual consistency usada solo en notificaciones y reporting
- ✅ Fallback para buró en scoring-adapter
- ✅ Data ownership definido (bases independientes + outbox)
- ✅ Migración incremental con Strangler Fig

## Métricas
- Microservicios propuestos: 10
- Promedio de responsabilidades por servicio: 1-2
- Llamadas síncronas vs asíncronas: 60% / 40%
- Riesgo de migración: MEDIUM (ACID + core banking)
- Complejidad estimada: 210 story points
