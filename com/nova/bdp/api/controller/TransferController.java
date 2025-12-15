package com.nova.bdp.api.controller;

import com.nova.bdp.api.dto.CreateTransferRequest;
import com.nova.bdp.api.dto.TransactionResponse;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    private static final Logger log = LoggerFactory.getLogger(TransferController.class);
    private final Counter transferCounter;

    public TransferController(MeterRegistry meterRegistry) {
        this.transferCounter = Counter.builder("api.transfers.creations")
                                      .description("Number of transfer requests")
                                      .register(meterRegistry);
    }

    @PostMapping("/api/v1/transfers")
    @Operation(summary = "Create a new transfer between accounts", security = @SecurityRequirement(name = "bearer-jwt"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Transfer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "409", description = "Conflict, e.g., insufficient funds")
    })
    @Timed(value = "api.transfers.creation.time", description = "Time taken to create a transfer")
    public ResponseEntity<TransactionResponse> createTransfer(@Valid @RequestBody CreateTransferRequest request) {
        log.info("Received transfer request from account {} to {}", request.sourceAccountId(), request.destinationAccountId());
        transferCounter.increment();
        // Implementation logic here
        return ResponseEntity.status(201).body(null); // Placeholder
    }
}
