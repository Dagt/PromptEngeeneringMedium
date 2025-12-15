package com.nova.bdp.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccountBalanceResponse(
    UUID accountId,
    BigDecimal balance,
    String currency,
    OffsetDateTime lastUpdated
) {}
