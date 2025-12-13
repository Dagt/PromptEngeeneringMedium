# Prompt Chaining - Microservices Decomposition - v1.0

## Prompt 1: Extracción de Bounded Contexts (DDD)
Instrucciones:
- Rol: "Eres un arquitecto de software experto en DDD, microservicios y banca."
- Input: Descripción del monolito de gestión de préstamos (copiar el contexto del enunciado).
- Output esperado: JSON con bounded contexts identificados, atributos `name`, `responsibilities`, `core_entities`, `external_dependencies`.

### Output Prompt 1
`[pendiente: pega el JSON de bounded contexts]`

## Prompt 2: Diseño de Microservicios
Instrucciones:
- Usa el JSON del Prompt 1.
- Para cada bounded context, define un microservicio con: responsabilidades, APIs (REST/GraphQL/gRPC), data store recomendado (PostgreSQL/MongoDB/Redis), eventos clave (Kafka), seguridad (OAuth2/JWT), y consideraciones de performance.
- Asegura una sola responsabilidad por servicio y evita dependencias cíclicas.

### Output Prompt 2
`[pendiente: pega la arquitectura de microservicios propuesta]`

## Prompt 3: Comunicación Inter-Servicios
Instrucciones:
- Usa los microservicios del Prompt 2.
- Define para cada interacción: protocolo (REST sync, Kafka async, gRPC), patrones de resiliencia (retry/backoff, circuit breaker), consistencia (ACID vs eventual) y manejo de caché.
- Destaca casos donde el requerimiento de ACID es obligatorio (desembolsos) y dónde la eventual consistency es aceptable.

### Output Prompt 3
`[pendiente: pega el diagrama/listado de comunicación]`

## Prompt 4: Estrategia de Migración
Instrucciones:
- Usa los resultados anteriores.
- Propón un plan por fases siguiendo Strangler Fig Pattern, con entregables por sprint, feature flags, blue-green/canary y métricas de éxito.
- Asegura datos consistentes para el reporte CNBV diario y fallback para la consulta al buró externo.

### Output Prompt 4
`[pendiente: pega el plan de migración incremental]`

## Validación de Arquitectura
Completa tras obtener los outputs:
- ✅/❌ Cada microservicio tiene una sola responsabilidad: `[pendiente]`
- ✅/❌ No hay dependencias cíclicas entre servicios: `[pendiente]`
- ✅/❌ Transacciones ACID manejadas correctamente: `[pendiente]`
- ✅/❌ Eventual consistency manejada donde es aceptable: `[pendiente]`
- ✅/❌ Fallback strategies para dependencias externas (buró): `[pendiente]`
- ✅/❌ Data ownership bien definido (no shared databases): `[pendiente]`
- ✅/❌ Estrategia de migración es incremental (low risk): `[pendiente]`

## Métricas
- Microservicios propuestos: `X`
- Promedio de responsabilidades por servicio: `X` (objetivo: 1-3)
- Llamadas síncronas vs asíncronas: `XX% / XX%`
- Riesgo de migración: `LOW/MEDIUM/HIGH`
- Complejidad estimada (story points): `XXX`
