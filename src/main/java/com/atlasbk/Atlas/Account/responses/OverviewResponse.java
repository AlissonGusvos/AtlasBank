package com.atlasbk.Atlas.Account.responses;

import java.math.BigDecimal;

public record OverviewResponse(
        String name,
        BigDecimal balance
) {
}
