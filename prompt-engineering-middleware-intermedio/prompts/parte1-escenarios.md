# Parte 1: Aplicación de Técnicas Avanzadas

Completa las plantillas para los dos escenarios. Incluye 2 técnicas por escenario y justificación en 3-4 líneas, más el riesgo si no se aplican.

## Escenario A: Generación de Microservicios con Few-Shot
- Técnica 1: Plantillas Few-Shot con ejemplos completos de controller-service-repository
  - Justificación: Repetir tres ejemplos previos guía al modelo sobre anotaciones, contratos y flujo en capas, reduciendo desviaciones de estilo. Los few-shots fijan nombres, validaciones y convenciones de seguridad (JWT, roles) que un prompt genérico podría ignorar. Así se preserva consistencia con el resto del monolito/microservicio y minimiza retrabajo manual.
- Técnica 2: Output Schema (JSON + bloques de código etiquetados)
  - Justificación: Definir un esquema con claves por capa y bloques ` ```java ` obliga al modelo a devolver código separado y pegable. Evita que mezcle explicaciones con implementación y permite validar automáticamente cada fragmento. Además, facilita insertar pruebas y anotaciones OpenAPI en las secciones correctas sin pérdida de contexto.
- Riesgo sin estas técnicas: El modelo podría generar controladores sin seguridad o validaciones, o mezclar capas rompiendo la arquitectura. También se perdería tiempo limpiando texto libre y corrigiendo estilos, aumentando el riesgo de endpoints incompatibles con las pruebas automatizadas.

## Escenario B: Análisis de Cumplimiento de OpenAPI en 15 Especificaciones
- Técnica 1: Prompt Chaining con iteración por archivo y checklist estructurada
  - Justificación: Procesar cada especificación en pasos (parseo → validación → reporte) evita desbordar el contexto y permite reutilizar un checklist fijo de reglas (nombres, códigos HTTP, esquemas). Esto reduce omisiones al revisar múltiples archivos y estandariza el output por servicio.
- Técnica 2: System + JSON Schema para reportes normalizados
  - Justificación: Un mensaje de sistema que exija "no inventar campos" y un JSON Schema de resultado (por endpoint: status, hallazgos, severidad, recomendación) limita al modelo a hechos presentes en el OpenAPI. Facilita luego consumir los reportes por scripts y comparar cumplimiento entre servicios.
- Riesgo sin estas técnicas: El análisis se vuelve inconsistente entre especificaciones, con alucinaciones de rutas inexistentes o métricas no comparables. Además, cualquier error de formato rompe el pipeline automatizado que consolida hallazgos y métricas de calidad.
