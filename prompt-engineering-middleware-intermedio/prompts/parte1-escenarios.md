# Parte 1: Aplicación de Técnicas Avanzadas

Completa las plantillas para los dos escenarios. Incluye 2 técnicas por escenario y justificación en 3-4 líneas, más el riesgo si no se aplican.

## Escenario A: Generación de Microservicios con Few-Shot
- Técnica 1: Few-Shot + System Instructions
  - Justificación: Los tres microservicios de referencia permiten forzar consistencia de arquitectura, convenciones de nombres, seguridad y pruebas al extrapolar el patrón a los 12 restantes. Fijar un rol experto en Spring Boot y banca reduce variaciones estilísticas y garantiza que se usen las mismas anotaciones OpenAPI, políticas de seguridad y manejo de excepciones.
- Técnica 2: JSON Schema para estructura de artefactos y OpenAPI
  - Justificación: Un esquema rígido para controllers, DTOs, excepciones y pruebas asegura que cada servicio generado incluya validaciones, seguridad y respuestas estandarizadas. Complementa el Few-Shot validando automáticamente la forma del output (anotaciones, códigos HTTP, DTOs) y reduce omisiones en campos críticos.
- Riesgo sin estas técnicas: Se producirían servicios inconsistentes en endpoints, seguridad y manejo de errores, aumentando el costo de retrabajo y la probabilidad de vulnerabilidades o incumplimiento regulatorio. Sin esquema ni ejemplos, el LLM podría generar DTOs incompletos y tests insuficientes.

## Escenario B: Análisis de Cumplimiento de OpenAPI en 15 Especificaciones
- Técnica 1: Long Context + Segmentación guiada
  - Justificación: Cargar las 15 especificaciones (~800-1200 líneas cada una) en una sola sesión permite comparar convenciones y detectar divergencias globales (naming, versionado, seguridad). Dividir el análisis por criterios (security schemes, rate limiting, versionado) evita pérdida de contexto y mantiene trazabilidad por archivo.
- Técnica 2: Chain-of-Thought con Debiasing explícito
  - Justificación: Forzar pasos secuenciales (identificar esquema de seguridad, validar anotaciones por endpoint, revisar errores y rate limiting) reduce saltos lógicos y omisiones. Añadir instrucciones de debiasing (verificar contraejemplos, no asumir seguridad implícita) disminuye falsos positivos/negativos al evaluar cumplimiento.
- Riesgo sin estas técnicas: El modelo puede pasar por alto endpoints sin autenticación, duplicar criterios o asumir cumplimiento por patrones comunes, generando reportes incompletos y decisiones erróneas de gobernanza de API. También se perdería visibilidad de violaciones de seguridad críticas por falta de razonamiento paso a paso.
