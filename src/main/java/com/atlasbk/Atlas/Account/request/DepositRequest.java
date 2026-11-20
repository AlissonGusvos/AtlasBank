package com.atlasbk.Atlas.Account.request;

import java.math.BigDecimal;

public record DepositRequest(
        BigDecimal value,
        String accPass
) {
}
