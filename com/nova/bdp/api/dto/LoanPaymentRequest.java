package com.nova.bdp.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record LoanPaymentRequest(
    @NotNull @Positive BigDecimal amount
) {}
