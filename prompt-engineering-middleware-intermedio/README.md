# Assignment: Prompt Engineering Avanzado - Middleware & Backend

Este repositorio contiene la estructura solicitada para el assignment de Prompt Engineering avanzado enfocado en la vertical de Middleware & Backend. Incluye las respuestas de la Parte 1 y los artefactos de prompts, esquemas y plantillas necesarios para ejecutar las Partes 2 y 3 en un LLM.

## Parte 1: Aplicación de Técnicas Avanzadas

### Escenario A: Generación de Microservicios con Few-Shot
- **Técnica 1: Few-Shot con microservicios de referencia**  
  Usa los tres servicios completos (AccountService, TransactionService, CustomerService) como ejemplos directos para forzar consistencia en controladores, servicios, repositorios, DTOs, seguridad y pruebas. El modelo replica patrones de arquitectura y convenciones sin desviarse del blueprint existente.  
- **Técnica 2: System Instructions + JSON Schema de salida**  
  Se define en el prompt un rol de arquitecto backend y se añade un JSON Schema que describe la estructura deseada (paquetes, endpoints, anotaciones de seguridad, validaciones, pruebas). Esto asegura que cada microservicio generado cumpla con contratos de arquitectura y que el output sea parseable y verificable.  
- **Riesgo sin estas técnicas:** Se perdería homogeneidad de capas y patrones (por ejemplo, falta de @PreAuthorize o manejo de excepciones), aparecerían DTOs inconsistentes y pruebas incompletas, generando retrabajo masivo para alinear 12 servicios adicionales.

### Escenario B: Análisis de Cumplimiento de OpenAPI en 15 Especificaciones
- **Técnica 1: Long Context + Segmentación**  
  Permite cargar las 15 especificaciones (800-1200 líneas cada una) en una sola sesión, usando delimitadores por archivo para preservar el contexto completo. Facilita validar naming conventions, versionamiento y seguridad sin truncamientos ni pérdida de información.  
- **Técnica 2: Chain-of-Thought con lista de chequeo debiased**  
  Se guía al modelo a razonar paso a paso (security schemes → endpoints críticos → rate limiting → validaciones → errores) y se añaden recordatorios de evitar supuestos, evaluando solo evidencia explícita. Esto reduce sesgos de confirmación y reportes falsamente positivos.  
- **Riesgo sin estas técnicas:** El modelo podría omitir partes de las especificaciones por límites de contexto, o emitir conclusiones sin fundamento, dejando pasar incumplimientos críticos de seguridad y versionamiento.

## Estructura del repositorio

```
prompt-engineering-middleware-intermedio/
├── README.md
├── prompts/
│   ├── caso1-endpoints.md
│   ├── caso2-api-security.md
│   ├── caso3-microservices.md
│   └── caso4-iteration.md
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

