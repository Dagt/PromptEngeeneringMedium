# Parte 3.2: Justificación de Técnicas

Redacta entre 200-300 palabras explicando:
- Por qué cada técnica avanzada fue necesaria (Few-Shot, JSON Schema, System Instructions, Long Context, Chain-of-Thought, Prompt Chaining, Debiasing).
- Cómo impactó cada técnica en las métricas antes/después.
- Lecciones aprendidas sobre prompt engineering avanzado para backend/middleware.

La combinación de técnicas avanzadas permitió transformar prompts genéricos en artefactos reproducibles con trazabilidad y métricas. **System Instructions** fijó el rol experto en banca y Spring Boot, evitando salidas diluidas y reduciendo variabilidad en estilo. **Few-Shot** ofreció patrones concretos de controllers, validaciones y seguridad; al introducir ejemplos completos, la conformidad OpenAPI y el uso correcto de DTOs subió de 0% a >90%. **JSON Schema** añadió una capa de contrato: validó que los endpoints incluyeran anotaciones, respuestas y constraints, elevando la cobertura de validaciones y seguridad en las métricas.

Para análisis masivo, **Long Context** permitió revisar 15 especificaciones sin perder correlaciones globales (versionado, naming, rate limiting), mientras que **Chain-of-Thought** forzó un recorrido paso a paso que redujo omisiones en esquemas de seguridad y manejo de errores. Al combinar **CoT con Debiasing**, el modelo contrastó supuestos (ej., no asumir JWT por default) y disminuyó falsos positivos, mejorando el score de cumplimiento OWASP y detectando endpoints sin autenticación.

En el diseño de arquitectura, **Prompt Chaining** descompuso el problema en fases (bounded contexts, servicios, comunicación, migración), evitando decisiones monolíticas y asegurando criterios de responsabilidad única y ausencia de dependencias cíclicas. Iterar los prompts con métricas (openapi compliance, security, tests) mostró mejoras cuantificables: cada refinamiento acercó los scores a >95% y guió la inclusión de pruebas y observabilidad. Lección clave: tratar prompts como código versionado, con contratos formales (JSON Schema) y pasos de razonamiento explícitos, maximiza consistencia y reduce retrabajo en entornos regulados de middleware.
