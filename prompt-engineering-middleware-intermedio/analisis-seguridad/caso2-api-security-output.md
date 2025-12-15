# Prompt Análisis de Seguridad APIs - v1.1

## Contexto
- Rol: Senior Backend / Middleware Security Engineer.
- Objetivo: Análisis de cumplimiento OWASP API Security Top 10 y regulaciones CNBV sobre 5 especificaciones OpenAPI existentes en el repositorio.
- Técnica obligatoria: Long Context para cargar los 5 YAML completos en una sola ejecución, aplicando Chain-of-Thought detallado.
- Alcance: No modificar objetivos, criterios ni simplificar el análisis; no generar el análisis todavía (solo el prompt).

## Input explícito (pegar los 5 YAML completos)

=== openapi-specs/payment-service-api.yaml ===
openapi: 3.0.3
info:
  title: Payment Service API
  description: API para procesamiento de pagos bancarios - Banco del Pacífico
  version: 1.0.0
  contact:
    name: Nova Solution Systems
    email: api-support@novasolutionsystems.com

servers:
  - url: https://api.bancoPacifico.com.mx/api/v1
    description: Production server
  - url: https://api-staging.bancoPacifico.com.mx/api/v1
    description: Staging server

security:
  - BearerAuth: []

components:
  securitySchemes:
    BearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
      description: JWT token obtenido del endpoint /auth/login

  schemas:
    PaymentRequest:
      type: object
      required:
        - sourceAccountId
        - destinationAccountId
        - amount
        - currency
      properties:
        sourceAccountId:
          type: string
          pattern: '^ACC-[0-9]{10}$'
          example: ACC-1234567890
        destinationAccountId:
          type: string
          pattern: '^ACC-[0-9]{10}$'
          example: ACC-0987654321
        amount:
          type: number
          format: double
          minimum: 0.01
          maximum: 1000000
          example: 15000.50
        currency:
          type: string
          enum: [MXN, USD]
          example: MXN
        reference:
          type: string
          maxLength: 100
          example: Pago de servicios

    PaymentResponse:
      type: object
      properties:
        transactionId:
          type: string
          example: TXN-2025012700001
        status:
          type: string
          enum: [PENDING, COMPLETED, FAILED]
          example: COMPLETED
        timestamp:
          type: string
          format: date-time
        amount:
          type: number
          format: double

    ErrorResponse:
      type: object
      properties:
        code:
          type: string
        message:
          type: string
        timestamp:
          type: string
          format: date-time

paths:
  /payments:
    post:
      summary: Crear nuevo pago
      description: Procesa una transferencia entre cuentas bancarias
      operationId: createPayment
      security:
        - BearerAuth: []
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/PaymentRequest'
      responses:
        '201':
          description: Pago creado exitosamente
          headers:
            X-RateLimit-Limit:
              schema:
                type: integer
              description: Número máximo de requests por hora
            X-RateLimit-Remaining:
              schema:
                type: integer
              description: Requests restantes en la ventana actual
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/PaymentResponse'
        '400':
          description: Request inválido
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '401':
          description: No autorizado
        '403':
          description: Sin permisos suficientes
        '409':
          description: Fondos insuficientes
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

  /payments/{transactionId}:
    get:
      summary: Consultar estado de pago
      operationId: getPaymentStatus
      security:
        - BearerAuth: []
      parameters:
        - name: transactionId
          in: path
          required: true
          schema:
            type: string
            pattern: '^TXN-[0-9]{13}$'
      responses:
        '200':
          description: Pago encontrado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/PaymentResponse'
        '404':
          description: Pago no encontrado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

=== openapi-specs/loan-service-api.yaml ===
openapi: 3.0.3
info:
  title: Loan Service API
  description: API para gestión de préstamos - Banco del Pacífico
  version: 1.0.0
  contact:
    name: Nova Solution Systems
    email: api-support@novasolutionsystems.com

servers:
  - url: https://api.bancoPacifico.com.mx/api/v1
    description: Production server

security:
  - OAuth2: [read:loans, write:loans]

components:
  securitySchemes:
    OAuth2:
      type: oauth2
      flows:
        authorizationCode:
          authorizationUrl: https://auth.bancoPacifico.com.mx/oauth/authorize
          tokenUrl: https://auth.bancoPacifico.com.mx/oauth/token
          scopes:
            read:loans: Leer información de préstamos
            write:loans: Crear y modificar préstamos

  schemas:
    LoanApplicationRequest:
      type: object
      required:
        - customerId
        - amount
        - termMonths
      properties:
        customerId:
          type: string
          example: CUST-12345
        amount:
          type: number
          format: double
          example: 250000
        termMonths:
          type: integer
          example: 24
        purpose:
          type: string

    LoanResponse:
      type: object
      properties:
        loanId:
          type: string
          example: LOAN-2025-001234
        status:
          type: string
          enum: [PENDING_APPROVAL, APPROVED, REJECTED, DISBURSED]
        approvedAmount:
          type: number
          format: double
        interestRate:
          type: number
          format: double
        creditScore:
          type: integer
          description: Score crediticio del cliente
          example: 720

    ErrorResponse:
      type: object
      properties:
        error:
          type: string
        message:
          type: string
        stackTrace:
          type: string
          description: Stack trace completo del error (solo en desarrollo)
        internalCode:
          type: string

paths:
  /loans/applications:
    post:
      summary: Solicitar nuevo préstamo
      operationId: createLoanApplication
      security:
        - OAuth2: [write:loans]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/LoanApplicationRequest'
      responses:
        '201':
          description: Solicitud creada
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LoanResponse'
        '400':
          description: Datos inválidos
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '500':
          description: Error interno del servidor
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
              example:
                error: InternalServerError
                message: Error al procesar la solicitud
                stackTrace: |
                  java.lang.NullPointerException: Cannot invoke method getCustomer() on null object
                  at com.banco.loans.service.LoanService.processApplication(LoanService.java:145)
                  at com.banco.loans.controller.LoanController.createLoan(LoanController.java:78)
                internalCode: DB_CONNECTION_FAILED

  /loans/{loanId}:
    get:
      summary: Obtener detalles de préstamo
      operationId: getLoanById
      parameters:
        - name: loanId
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Préstamo encontrado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LoanResponse'
        '404':
          description: Préstamo no encontrado

  /loans/{loanId}/approve:
    post:
      summary: Aprobar préstamo (ADMIN only)
      operationId: approveLoan
      parameters:
        - name: loanId
          in: path
          required: true
          schema:
            type: string
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                approvedAmount:
                  type: number
                  format: double
                notes:
                  type: string
      responses:
        '200':
          description: Préstamo aprobado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LoanResponse'
        '403':
          description: No autorizado para aprobar préstamos

=== openapi-specs/card-service-api.yaml ===
openapi: 3.0.3
info:
  title: Card Service API
  description: API para gestión de tarjetas de crédito y débito
  version: 1.0.0
  contact:
    name: Card Management Team
    email: cards@bancoPacifico.com.mx

servers:
  - url: https://api.bancoPacifico.com.mx
    description: Production server

components:
  schemas:
    CardRequest:
      type: object
      properties:
        customerId:
          type: string
        cardType:
          type: string
          description: Tipo de tarjeta
        creditLimit:
          type: number

    CardResponse:
      type: object
      properties:
        cardId:
          type: string
        cardNumber:
          type: string
          description: Número completo de tarjeta (16 dígitos)
          example: "4532-1488-0343-6467"
        cvv:
          type: string
          description: Código de seguridad
          example: "123"
        expiryDate:
          type: string
          example: "12/2027"
        pin:
          type: string
          description: PIN de 4 dígitos
          example: "1234"
        customerId:
          type: string
        balance:
          type: number
        creditLimit:
          type: number

    TransactionRequest:
      type: object
      properties:
        cardNumber:
          type: string
        amount:
          type: number
        merchantId:
          type: string

    ErrorResponse:
      type: object
      properties:
        errorCode:
          type: integer
        errorMessage:
          type: string
        debugInfo:
          type: object
          properties:
            databaseQuery:
              type: string
            userId:
              type: string
            sessionToken:
              type: string

paths:
  /cards:
    post:
      summary: Crear nueva tarjeta
      operationId: createCard
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CardRequest'
      responses:
        '200':
          description: Tarjeta creada
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/CardResponse'
        '500':
          description: Error del servidor
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
              example:
                errorCode: 500
                errorMessage: Database connection failed
                debugInfo:
                  databaseQuery: "INSERT INTO cards (customer_id, card_number, cvv, pin) VALUES (?, ?, ?, ?)"
                  userId: "admin@bancoPacifico.com.mx"
                  sessionToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0"

    get:
      summary: Listar todas las tarjetas
      operationId: listCards
      parameters:
        - name: customerId
          in: query
          required: false
          schema:
            type: string
      responses:
        '200':
          description: Lista de tarjetas
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/CardResponse'

  /cards/{cardId}:
    get:
      summary: Obtener detalles de tarjeta
      operationId: getCard
      parameters:
        - name: cardId
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Detalles de la tarjeta
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/CardResponse'

  /cards/{cardId}/transactions:
    post:
      summary: Procesar transacción con tarjeta
      operationId: processTransaction
      parameters:
        - name: cardId
          in: path
          required: true
          schema:
            type: string
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/TransactionRequest'
      responses:
        '200':
          description: Transacción procesada
        '400':
          description: Error en la transacción

  /cards/{cardId}/block:
    put:
      summary: Bloquear tarjeta
      operationId: blockCard
      parameters:
        - name: cardId
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Tarjeta bloqueada

=== openapi-specs/notification-service-api.yaml ===
openapi: 3.0.3
info:
  title: Notification Service API
  description: |
    API para envío de notificaciones multicanal (Email, SMS, Push) - Banco del Pacífico

    **Políticas de Seguridad:**
    - Todos los endpoints requieren autenticación JWT
    - Rate limiting: 100 requests/minuto por cliente
    - CORS: Solo dominios autorizados (*.bancoPacifico.com.mx)
  version: 2.1.0
  contact:
    name: Nova Solution Systems - Notifications Team
    email: notifications@novasolutionsystems.com

servers:
  - url: https://api.bancoPacifico.com.mx/api/v2
    description: Production server (v2)
  - url: https://api.bancoPacifico.com.mx/api/v1
    description: Production server (v1 - deprecated)
  - url: https://api-staging.bancoPacifico.com.mx/api/v2
    description: Staging server

security:
  - JWTAuth: []

components:
  securitySchemes:
    JWTAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
      description: |
        JWT token con claims: sub (userId), roles (array), exp (timestamp).
        Obtener token en POST /auth/login

  schemas:
    NotificationRequest:
      type: object
      required:
        - recipientId
        - channel
        - template
      properties:
        recipientId:
          type: string
          pattern: '^CUST-[0-9]{10}$'
          description: ID del cliente destinatario
          example: CUST-1234567890
        channel:
          type: string
          enum: [EMAIL, SMS, PUSH, WHATSAPP]
          description: Canal de envío
        template:
          type: string
          pattern: '^TPL-[A-Z0-9]{8}$'
          description: ID del template a usar
          example: TPL-PAYMENT1
        variables:
          type: object
          additionalProperties:
            type: string
          maxProperties: 20
          description: Variables para el template (max 20)
        priority:
          type: string
          enum: [LOW, NORMAL, HIGH, URGENT]
          default: NORMAL

    NotificationResponse:
      type: object
      properties:
        notificationId:
          type: string
          example: NOTIF-2025012700001
        status:
          type: string
          enum: [QUEUED, SENT, DELIVERED, FAILED]
        sentAt:
          type: string
          format: date-time
        channel:
          type: string

    ErrorResponse:
      type: object
      properties:
        code:
          type: string
          description: Código de error público
          example: INVALID_RECIPIENT
        message:
          type: string
          description: Mensaje descriptivo para el usuario
          example: El ID del destinatario no es válido
        timestamp:
          type: string
          format: date-time
        requestId:
          type: string
          description: ID para rastrear el request en logs

paths:
  /notifications:
    post:
      summary: Enviar notificación
      description: Encola una notificación para envío a través del canal especificado
      operationId: sendNotification
      security:
        - JWTAuth: []
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/NotificationRequest'
      responses:
        '202':
          description: Notificación aceptada y encolada
          headers:
            X-RateLimit-Limit:
              schema:
                type: integer
              description: Requests máximos por minuto
              example: 100
            X-RateLimit-Remaining:
              schema:
                type: integer
              description: Requests restantes en ventana actual
              example: 87
            X-RateLimit-Reset:
              schema:
                type: integer
              description: Timestamp cuando se resetea el límite
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/NotificationResponse'
        '400':
          description: Request inválido
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '401':
          description: Token JWT inválido o expirado
        '403':
          description: Sin permisos para enviar notificaciones
        '429':
          description: Rate limit excedido
          headers:
            Retry-After:
              schema:
                type: integer
              description: Segundos para reintentar

  /notifications/{notificationId}:
    get:
      summary: Consultar estado de notificación
      operationId: getNotificationStatus
      security:
        - JWTAuth: []
      parameters:
        - name: notificationId
          in: path
          required: true
          schema:
            type: string
            pattern: '^NOTIF-[0-9]{13}$'
          description: ID único de la notificación
      responses:
        '200':
          description: Estado de la notificación
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/NotificationResponse'
        '404':
          description: Notificación no encontrada
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

  /notifications/batch:
    post:
      summary: Envío masivo de notificaciones (ADMIN only)
      operationId: sendBatchNotifications
      security:
        - JWTAuth: []
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required:
                - notifications
              properties:
                notifications:
                  type: array
                  items:
                    $ref: '#/components/schemas/NotificationRequest'
                  minItems: 1
                  maxItems: 100
                  description: Máximo 100 notificaciones por batch
      responses:
        '202':
          description: Batch aceptado
          content:
            application/json:
              schema:
                type: object
                properties:
                  batchId:
                    type: string
                  totalQueued:
                    type: integer
        '403':
          description: Requiere rol ADMIN

=== openapi-specs/audit-service-api.yaml ===
openapi: 3.0.3
info:
  title: Audit Service API
  description: API para auditoría y trazabilidad de operaciones bancarias - Banco del Pacífico
  version: 1.0.0
  contact:
    name: Compliance & Audit Team
    email: audit@bancoPacifico.com.mx

servers:
  - url: https://api.bancoPacifico.com.mx/api/v1
    description: Production server

components:
  securitySchemes:
    ApiKeyAuth:
      type: apiKey
      in: header
      name: X-API-Key
      description: API Key proporcionada por el equipo de compliance

  schemas:
    AuditLogRequest:
      type: object
      required:
        - action
        - userId
      properties:
        action:
          type: string
          enum: [CREATE, UPDATE, DELETE, VIEW, EXPORT]
        userId:
          type: string
          pattern: '^USR-[0-9]{6}$'
          example: USR-123456
        resource:
          type: string
          example: /api/v1/accounts/ACC-1234567890
        ipAddress:
          type: string
          format: ipv4
        metadata:
          type: object
          additionalProperties: true

    AuditLogResponse:
      type: object
      properties:
        logId:
          type: string
          example: AUD-2025012700001
        timestamp:
          type: string
          format: date-time
        action:
          type: string
        userId:
          type: string
        resource:
          type: string
        ipAddress:
          type: string
        geoLocation:
          type: object
          properties:
            country:
              type: string
            city:
              type: string

    ErrorResponse:
      type: object
      properties:
        errorCode:
          type: string
        errorMessage:
          type: string
        path:
          type: string
          description: Endpoint que generó el error
        method:
          type: string
          description: Método HTTP usado
        timestamp:
          type: string
          format: date-time

paths:
  /audit/logs:
    post:
      summary: Registrar evento de auditoría
      operationId: createAuditLog
      security:
        - ApiKeyAuth: []
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/AuditLogRequest'
      responses:
        '201':
          description: Log de auditoría creado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AuditLogResponse'
        '400':
          description: Request inválido
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '401':
          description: API Key inválida

    get:
      summary: Consultar logs de auditoría
      description: Permite filtrar logs por fecha, usuario, acción, etc.
      operationId: queryAuditLogs
      parameters:
        - name: startDate
          in: query
          schema:
            type: string
            format: date
        - name: endDate
          in: query
          schema:
            type: string
            format: date
        - name: userId
          in: query
          schema:
            type: string
        - name: action
          in: query
          schema:
            type: string
        - name: limit
          in: query
          schema:
            type: integer
            default: 100
      responses:
        '200':
          description: Lista de logs de auditoría
          content:
            application/json:
              schema:
                type: object
                properties:
                  logs:
                    type: array
                    items:
                      $ref: '#/components/schemas/AuditLogResponse'
                  totalCount:
                    type: integer
        '500':
          description: Error al consultar logs
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
              example:
                errorCode: DATABASE_ERROR
                errorMessage: Failed to execute query on audit_logs table
                path: /api/v1/audit/logs
                method: GET
                timestamp: "2025-01-27T10:30:00Z"

  /audit/logs/{logId}:
    get:
      summary: Obtener log específico
      operationId: getAuditLogById
      security:
        - ApiKeyAuth: []
      parameters:
        - name: logId
          in: path
          required: true
          schema:
            type: string
            pattern: '^AUD-[0-9]{13}$'
      responses:
        '200':
          description: Log encontrado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AuditLogResponse'
        '404':
          description: Log no encontrado

  /audit/reports/compliance:
    get:
      summary: Generar reporte de cumplimiento CNBV
      description: |
        Genera reporte regulatorio para la CNBV con todas las operaciones del período.
        Incluye logs de acceso, modificaciones, y exportaciones de datos sensibles.
      operationId: generateComplianceReport
      parameters:
        - name: year
          in: query
          required: true
          schema:
            type: integer
            minimum: 2020
            maximum: 2030
        - name: month
          in: query
          required: true
          schema:
            type: integer
            minimum: 1
            maximum: 12
        - name: format
          in: query
          schema:
            type: string
            enum: [PDF, CSV, XML]
            default: PDF
      responses:
        '200':
          description: Reporte generado
          content:
            application/pdf:
              schema:
                type: string
                format: binary
        '400':
          description: Parámetros inválidos

## Proceso de análisis (Chain-of-Thought obligatorio)
El LLM debe razonar paso a paso, sin omitir ni reordenar:
1.  **Identificar security schemes declarados** (`securitySchemes`, tipos OAuth2/JWT bearer, scopes).
2.  **Verificar @SecurityRequirement en endpoints críticos** (pagos, préstamos, tarjetas, notificaciones, auditoría) y cuantificar cobertura.
3.  **Validar presencia de rate limiting** (headers `X-RateLimit-*` u otros mecanismos documentados).
4.  **Revisar validaciones de input** (schemas, `required`, `pattern`, `format`, límites numéricos) en todos los request bodies y parámetros.
5.  **Analizar error responses** para confirmar que no exponen stack traces ni información sensible; evaluar consistencia de códigos y payloads.
6.  **Generar recomendaciones de hardening** específicas por API, alineadas a OWASP y CNBV.

## Formato de salida obligatorio
- **Razonamiento paso a paso** completo del Chain-of-Thought con referencias a cada archivo YAML.
- **Tabla comparativa de seguridad** (una fila por API) con columnas mínimas: `API Spec`, `Security Scheme`, `Auth en Endpoints`, `Rate Limit`, `Input Validation`, `Error Handling`, `Cumple OWASP`.
- **Lista de vulnerabilidades** clasificadas por severidad (CRITICAL, HIGH, MEDIUM, LOW) con evidencia y referencia de archivo/endpoint.
- **Métricas globales**: cantidad de APIs compliant, endpoints inseguros, conteo por severidad, score promedio de seguridad.

## Instrucciones de guardado
- Pega el **resultado completo del LLM** en `analisis-seguridad/caso2-api-security-output.md` dentro del repositorio, preservando el formato anterior.

## Criterios de evaluación
- Alineación con OWASP API Security Top 10 y requisitos CNBV.
- Versionamiento obligatorio `/api/v1` documentado en las especificaciones.
- Seguridad: `securitySchemes` definidos y aplicados; endpoints críticos con autenticación/autorización.
- Rate limiting documentado y verificable.
- Validación de entrada exhaustiva; ausencia de stack traces o datos sensibles en errores.
- Tono técnico, corporativo y evaluable; sin supuestos ni invención de datos fuera de los YAML provistos.
