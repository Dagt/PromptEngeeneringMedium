package com.nova.bdp.api.controller;

import com.nova.bdp.api.dto.LoanPaymentRequest;
import com.nova.bdp.api.dto.LoanPaymentResponse;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class LoanController {

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    @PatchMapping("/api/v1/loans/{loanId}/payment")
    @Operation(summary = "Register a partial payment for a loan", security = @SecurityRequirement(name = "bearer-jwt"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "409", description = "Conflict, e.g., invalid loan state for payment")
    })
    @Timed(value = "api.loans.payment.time", description = "Time taken to register a loan payment")
    public ResponseEntity<LoanPaymentResponse> registerLoanPayment(
            @PathVariable UUID loanId,
            @Valid @RequestBody LoanPaymentRequest request) {
        log.info("Received payment for loan {}", loanId);
        // Implementation logic here
        return ResponseEntity.ok(null); // Placeholder
    }
}
