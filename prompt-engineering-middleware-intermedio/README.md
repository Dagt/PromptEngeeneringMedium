# Assignment: Prompt Engineering Avanzado - Middleware (Intermedio)

Este repositorio sigue la estructura solicitada para la entrega de la asignación. Contiene los prompts, outputs y artefactos necesarios para documentar el trabajo práctico. La siguiente sección resume el estado actual y resuelve la **Parte 1: Aplicación de Técnicas Avanzadas**.

## Estructura
- `prompts/`: Contendrá los prompts diseñados para cada caso (caso1-endpoints, caso2-api-security, caso3-microservices, caso4-iteration).
- `outputs/`: Espacio para los outputs generados por el LLM en cada caso.
- `endpoints-generados/`: Especificaciones y código de endpoints creados.
- `analisis-seguridad/`: Reportes y tablas de cumplimiento de seguridad.
- `arquitectura-microservicios/`: Diseños y diagramas de microservicios.
- `openapi-specs/`: Copias de las especificaciones OpenAPI suministradas por el curso.
- `screenshots/`: Evidencias de ejecución de cada caso.

## Parte 1: Aplicación de Técnicas Avanzadas

### Escenario A: Generación de Microservicios con Few-Shot
- **Técnica 1: Few-Shot Prompting estructurado**  
  *Justificación:* Al incorporar los tres microservicios de referencia como ejemplos completos (controladores, servicios, repositorios y pruebas), el modelo puede inferir patrones de arquitectura, seguridad y pruebas. Esto reduce la variabilidad y asegura consistencia en naming conventions, manejo de excepciones y anotaciones de seguridad.
- **Técnica 2: JSON Schema para outputs OpenAPI y DTOs**  
  *Justificación:* Un schema explícito obliga al modelo a producir especificaciones y DTOs alineados con los contratos corporativos (OpenAPI 3.0, validaciones Jakarta, anotaciones de seguridad). Complementa al Few-Shot al validar la estructura y prevenir omisiones en endpoints o campos críticos.
- **Riesgo sin estas técnicas:** Sin ejemplos ni validación estructural, los microservicios podrían divergir en contratos, seguridad y pruebas, generando deuda técnica y posibles incumplimientos regulatorios.

### Escenario B: Análisis de Cumplimiento de OpenAPI en 15 Especificaciones
- **Técnica 1: Long Context con resumen estructurado**  
  *Justificación:* Permite cargar las 15 especificaciones (800-1200 líneas cada una) y mantener referencias cruzadas sobre naming, versionado y esquemas de seguridad. Facilita detectar inconsistencias globales en un solo pase, evitando truncamiento de contexto.
- **Técnica 2: Chain-of-Thought con checklist de estándares**  
  *Justificación:* Obliga al modelo a razonar paso a paso siguiendo los criterios corporativos (nombres, versionado, error handling, seguridad, rate limiting), reduciendo sesgos y omisiones. Proporciona trazabilidad del análisis y facilita auditar decisiones.
- **Riesgo sin estas técnicas:** El análisis podría ser superficial o inconsistente entre archivos, dejando pasar violaciones de seguridad o convenciones de API que impacten la interoperabilidad y el cumplimiento regulatorio.

## Próximos pasos
- Completar los prompts de los casos 1-4 en `prompts/` y colocar los outputs en `outputs/`.
- Copiar las especificaciones OpenAPI proporcionadas al directorio `openapi-specs/`.
- Añadir evidencias de ejecución en `screenshots/`.
