# Parte 3.2 - Justificación de Técnicas (200-300 palabras)

El uso de **Few-Shot** fue esencial para capturar patrones de arquitectura ya aprobados (controladores, DTOs, seguridad, pruebas). Al proporcionar ejemplos completos, el modelo replicó nombres de paquetes, anotaciones y convenciones de errores, elevando las métricas de documentación y seguridad desde 0% hasta superar el 90% tras la primera iteración. 

La incorporación de **JSON Schema** permitió validar estructuralmente el output del LLM (paths esperados, `securitySchemes`, `responses`). Este esquema actuó como contrato de salida y filtro automático: las desviaciones eran visibles inmediatamente, lo que redujo el gap de conformidad OpenAPI y evitó respuestas no parseables. 

Las **System Instructions** fijaron el rol del modelo (arquitecto backend bancario) y priorizaron requisitos regulatorios y de performance. Esto mitigó alucinaciones sobre dominios no bancarios y mejoró la precisión al solicitar validaciones ACID, seguridad JWT y observabilidad. 

Para el análisis de múltiples OpenAPI, **Long Context** y **Chain-of-Thought** fueron críticos. El primero evitó truncamientos al cargar cinco especificaciones simultáneamente; el segundo guió un checklist paso a paso (schemes → auth → rate limit → validaciones → errores), reduciendo sesgos de confirmación y elevando la completitud de hallazgos. 

En la iteración del endpoint de transferencias, el **Prompt Chaining** implícito (v1 → v2 → v3) facilitó incrementos controlados. Cada iteración añadió cobertura de seguridad, pruebas y observabilidad, elevando el score total hasta el umbral >95% esperado. 

Lección principal: combinar contratos estructurales (JSON Schema) con ejemplos concretos (Few-Shot) y razonamiento guiado (CoT) acelera la convergencia a outputs conformes y auditables en contextos bancarios sensibles a regulación y disponibilidad.

