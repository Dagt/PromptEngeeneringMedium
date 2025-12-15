package com.nova.bdp.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LoanPaymentResponse(
    UUID loanId,
    UUID paymentId,
    BigDecimal paidAmount,
    BigDecimal remainingBalance,
    OffsetDateTime paymentDate,
    String status
) {}
