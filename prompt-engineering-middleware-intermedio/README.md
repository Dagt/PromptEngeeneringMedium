# Assignment: Prompt Engineering Avanzado - Middleware & Backend

Este repositorio contiene la estructura solicitada para el assignment de Prompt Engineering avanzado enfocado en la vertical de Middleware & Backend. Incluye las respuestas de la Parte 1 y los artefactos de prompts, esquemas y plantillas necesarios para ejecutar las Partes 2 y 3 en un LLM.

## Parte 1: Aplicación de Técnicas Avanzadas

### Escenario A: Generación de Microservicios con Few-Shot
- **Técnica 1:** Few-Shot con microservicios de referencia.
  - **Justificación:** Los tres servicios completos (AccountService, TransactionService, CustomerService) fuerzan al modelo a replicar controladores, servicios, repositorios, DTOs, seguridad y pruebas con exactamente los mismos patrones de arquitectura y nombres de paquetes.
- **Técnica 2:** System Instructions + JSON Schema de salida.
  - **Justificación:** Fijar el rol del LLM como arquitecto backend bancario y exigir un JSON Schema de la estructura de salida garantiza paquetes, anotaciones de seguridad, validaciones y pruebas homogéneas y verificables automáticamente.
- **Riesgo sin estas técnicas:** Se perdería homogeneidad de capas (p. ej. @PreAuthorize, manejo de excepciones), aparecerían DTOs inconsistentes y pruebas incompletas, generando retrabajo masivo al alinear 12 servicios adicionales.

### Escenario B: Análisis de Cumplimiento de OpenAPI en 15 Especificaciones
- **Técnica 1:** Long Context + segmentación.
  - **Justificación:** Permite cargar las 15 especificaciones (800-1200 líneas cada una) en una sola sesión, con delimitadores claros por archivo, evitando truncamientos y preservando convenciones de naming, versionamiento y seguridad.
- **Técnica 2:** Chain-of-Thought con checklist debiased.
  - **Justificación:** Guía el análisis paso a paso (security schemes → endpoints críticos → rate limiting → validaciones → errores), con recordatorios explícitos de basarse solo en evidencia para reducir falsos positivos.
- **Riesgo sin estas técnicas:** El modelo podría omitir partes de las especificaciones por límites de contexto o emitir conclusiones sin fundamento, dejando pasar incumplimientos críticos de seguridad y versionamiento.

## Estructura del repositorio

```
prompt-engineering-middleware-intermedio/
├── README.md
├── prompts/
│   ├── caso1-endpoints.md
│   ├── caso2-api-security.md
│   ├── caso3-microservices.md
│   ├── caso4-iteration.md
│   └── parte3-justificacion.md
├── openapi-specs/
│   ├── audit-service-api.yaml
│   ├── card-service-api.yaml
│   ├── loan-service-api.yaml
│   ├── notification-service-api.yaml
│   └── payment-service-api.yaml
├── endpoints-generados/
├── analisis-seguridad/
├── arquitectura-microservicios/
├── outputs/
└── screenshots/
```

Las carpetas `outputs/`, `endpoints-generados/`, `analisis-seguridad/`, `arquitectura-microservicios/` y `screenshots/` incluyen marcadores `.gitkeep` para que puedan recibir las evidencias y resultados tras ejecutar los prompts en el LLM.
