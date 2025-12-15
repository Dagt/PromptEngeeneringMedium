package com.nova.bdp.api.controller;

import com.nova.bdp.api.dto.AccountBalanceResponse;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/api/v1/accounts/{accountId}/balance")
    @Operation(summary = "Get account balance by ID", security = @SecurityRequirement(name = "bearer-jwt"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PreAuthorize("hasRole('USER')")
    @Cacheable(value = "balances", key = "#accountId")
    @Timed(value = "api.accounts.balance.time", description = "Time taken to retrieve account balance")
    public ResponseEntity<AccountBalanceResponse> getBalance(@PathVariable UUID accountId) {
        log.info("Request for balance of account {}", accountId);
        // Implementation logic here
        return ResponseEntity.ok(null); // Placeholder
    }
}
