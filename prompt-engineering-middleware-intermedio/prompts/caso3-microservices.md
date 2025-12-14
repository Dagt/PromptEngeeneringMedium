# Prompt Chaining - Microservices Decomposition - v1.0
## Prompt 1: Extracción de Bounded Contexts (DDD)
[Escribe el prompt 1 y deja espacio para el JSON de salida.]
### Output Prompt 1
[JSON con bounded contexts identificados]
## Prompt 2: Diseño de Microservicios
[Prompt 2 usando el output del prompt 1]
### Output Prompt 2
[Arquitectura de microservicios propuesta con responsabilidades]
## Prompt 3: Comunicación Inter-Servicios
[Prompt 3 usando outputs anteriores]
### Output Prompt 3
[Diagrama de comunicación: REST/Kafka/gRPC por caso de uso]
## Prompt 4: Estrategia de Migración
[Prompt 4]
### Output Prompt 4
[Plan de migración incremental con fases]
## Validación de Arquitectura
- ✅ /❌ Cada microservicio tiene una sola responsabilidad
- ✅ /❌ No hay dependencias cíclicas entre servicios
- ✅ /❌ Transacciones ACID manejadas correctamente
- ✅ /❌ Eventual consistency manejada donde es aceptable
- ✅ /❌ Fallback strategies para dependencias externas (buró)
- ✅ /❌ Data ownership bien definido (no shared databases)
- ✅ /❌ Estrategia de migración es incremental (low risk)
## Métricas
- Microservicios propuestos: X
- Promedio de responsabilidades por servicio: X (objetivo: 1-3)
- Llamadas síncronas vs asíncronas: XX% / XX%
- Riesgo de migración: LOW/MEDIUM/HIGH
- Complejidad estimada (story points): XXX
