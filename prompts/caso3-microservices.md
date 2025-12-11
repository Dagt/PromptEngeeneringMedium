# Caso 3 – Microservicios (Prompt Chaining 4 fases)

## Prompt 1 – Bounded contexts (JSON)
[System] Define bounded contexts para BDP core: Account, Transaction, Customer, Loan, Notification.
[User] Entrega JSON con campos: contextName, aggregates, invariants (ACID/eventual), db (PostgreSQL/Mongo/Redis), eventosClaves.

## Prompt 2 – Diseño de microservicios
[System] Diseña microservicios Spring Boot 3.2/Java 21, REST/GraphQL, DTOs, mapping entidad-tabla, validaciones Bean Validation.
[User] Genera APIs y entidades para cada bounded context, incluye resiliencia (retry/backoff, circuit breaker), observabilidad (Micrometer/OTel), seguridad JWT/MTLS.

## Prompt 3 – Comunicación inter-servicios
[System] Define eventos Kafka por contexto, tópicos, claves partición, esquemas (Avro/JSON Schema), políticas de versionado.
[User] Agrega patrones de orquestación/coreografía y sagas para garantizar consistencia entre Account-Transaction-Loan; especifica timeouts y DLQ.

## Prompt 4 – Estrategia de migración incremental
[System] Planifica migración desde core legado a microservicios.
[User] Incluye paralelismo, doble escritura controlada, reconciliación, feature flags, canary releases, pruebas con Testcontainers, rollback.

## Outputs esperados (resumen)
- JSON de bounded contexts con agregados y invariantes.
- Lista de microservicios con endpoints clave y modelos.
- Mapa de eventos Kafka y contratos.
- Plan de migración con hitos y validaciones.

## Checklist de validación
1. Cada servicio tiene bounded context claro y dueño de datos.
2. Seguridad: JWT + MTLS documentado en APIs internas/externas.
3. Resiliencia: timeouts, retries, circuit breaker definidos.
4. Eventos versionados con compatibilidad hacia atrás.
5. Estrategia de rollback y feature flags incluida.
6. Pruebas automatizadas (REST Assured, Testcontainers) referenciadas.

## Métricas
- Cobertura dominios: 100%
- Compatibilidad de eventos: 95%
- Claridad de migración: 90%
- Nivel de detalle de resiliencia: 92%
