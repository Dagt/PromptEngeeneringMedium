# Output LLM - Endpoints REST Caso 1

```java
package com.nova.bdp.api.controller;

import com.nova.bdp.api.dto.*;
import com.nova.bdp.api.service.AccountBalanceService;
import com.nova.bdp.api.service.LoanPaymentService;
import com.nova.bdp.api.service.TransferService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class BankingController {
    private static final Logger log = LoggerFactory.getLogger(BankingController.class);

    private final TransferService transferService;
    private final AccountBalanceService accountBalanceService;
    private final LoanPaymentService loanPaymentService;
    private final Counter transferCounter;
    private final Timer transferTimer;

    public BankingController(TransferService transferService,
                             AccountBalanceService accountBalanceService,
                             LoanPaymentService loanPaymentService,
                             MeterRegistry meterRegistry) {
        this.transferService = transferService;
        this.accountBalanceService = accountBalanceService;
        this.loanPaymentService = loanPaymentService;
        this.transferCounter = meterRegistry.counter("transfers.created");
        this.transferTimer = meterRegistry.timer("transfers.latency");
    }

    @PostMapping("/transfers")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create transfer", security = @SecurityRequirement(name = "bearer-jwt"))
    @ApiResponse(responseCode = "201", description = "Transfer created")
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "409", description = "Insufficient funds", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        return transferTimer.record(() -> {
            log.info("Creating transfer from {} to {} amount {}", request.fromAccountId(), request.toAccountId(), request.amount());
            TransferResponse response = transferService.createTransfer(request);
            transferCounter.increment();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        });
    }

    @GetMapping("/accounts/{accountId}/balance")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get account balance", security = @SecurityRequirement(name = "bearer-jwt"))
    @ApiResponse(responseCode = "200", description = "Balance retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable UUID accountId,
            @RequestHeader(name = "If-None-Match", required = false) String etag) {
        log.debug("Fetching balance for account {}", accountId);
        BalanceResponse response = accountBalanceService.getBalance(accountId, etag);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/loans/{loanId}/payment")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register loan payment", security = @SecurityRequirement(name = "bearer-jwt"))
    @ApiResponse(responseCode = "200", description = "Payment registered")
    @ApiResponse(responseCode = "400", description = "Invalid payment", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "409", description = "Conflict or invalid state", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<LoanPaymentResponse> registerPayment(
            @PathVariable UUID loanId,
            @Valid @RequestBody LoanPaymentRequest request) {
        log.info("Registering payment for loan {} with amount {}", loanId, request.amount());
        LoanPaymentResponse response = loanPaymentService.registerPayment(loanId, request);
        return ResponseEntity.ok(response);
    }
}
```

```java
package com.nova.bdp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransferRequest(
        @NotNull UUID fromAccountId,
        @NotNull UUID toAccountId,
        @Positive BigDecimal amount,
        @Size(max = 140) String description
) {}

public record TransferResponse(
        @NotNull UUID transferId,
        @NotNull UUID fromAccountId,
        @NotNull UUID toAccountId,
        @Positive BigDecimal amount,
        @NotNull OffsetDateTime createdAt,
        @NotBlank String status
) {}

public record BalanceResponse(
        @NotNull UUID accountId,
        @PositiveOrZero BigDecimal available,
        @PositiveOrZero BigDecimal ledger,
        @NotNull OffsetDateTime asOf,
        String etag
) {}

public record LoanPaymentRequest(
        @Positive BigDecimal amount,
        @NotNull OffsetDateTime paymentDate,
        @Size(max = 140) String note
) {}

public record LoanPaymentResponse(
        @NotNull UUID loanId,
        @Positive BigDecimal amountApplied,
        @PositiveOrZero BigDecimal remainingBalance,
        @NotBlank String status,
        @NotNull OffsetDateTime processedAt
) {}

public record ErrorResponse(String code, String message) {}
```

```java
package com.nova.bdp.api.controller;

import com.nova.bdp.api.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", ex.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("CONSTRAINT_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("CONFLICT", ex.getMessage()));
    }
}
```

```java
package com.nova.bdp.api.controller;

import com.nova.bdp.api.dto.*;
import com.nova.bdp.api.service.AccountBalanceService;
import com.nova.bdp.api.service.LoanPaymentService;
import com.nova.bdp.api.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankingControllerTest {

    @Mock TransferService transferService;
    @Mock AccountBalanceService accountBalanceService;
    @Mock LoanPaymentService loanPaymentService;
    @InjectMocks BankingController controller;

    @Test
    void createTransfer_returnsCreated() {
        when(transferService.createTransfer(any())).thenReturn(new TransferResponse(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN, OffsetDateTime.now(), "COMPLETED"));
        ResponseEntity<TransferResponse> response = controller.createTransfer(new TransferRequest(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN, "test"));
        assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void getBalance_returnsOk() {
        UUID id = UUID.randomUUID();
        when(accountBalanceService.getBalance(any(), any())).thenReturn(new BalanceResponse(id, BigDecimal.TEN, BigDecimal.TEN, OffsetDateTime.now(), "etag"));
        ResponseEntity<BalanceResponse> response = controller.getBalance(id, null);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void registerPayment_returnsOk() {
        UUID loanId = UUID.randomUUID();
        when(loanPaymentService.registerPayment(any(), any())).thenReturn(new LoanPaymentResponse(loanId, BigDecimal.ONE, BigDecimal.ONE, "APPLIED", OffsetDateTime.now()));
        ResponseEntity<LoanPaymentResponse> response = controller.registerPayment(loanId, new LoanPaymentRequest(BigDecimal.ONE, OffsetDateTime.now(), "note"));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
```
```
@mvcIntegrationTest
class BankingControllerIT {
    // Plantilla REST Assured
    // given().auth().oauth2(token).contentType(JSON).body(payload).when().post("/api/v1/transfers").then().statusCode(201);
}
```
