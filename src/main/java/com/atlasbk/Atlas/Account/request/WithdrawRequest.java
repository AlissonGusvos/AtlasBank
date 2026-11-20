package com.atlasbk.Atlas.Account.request;

import java.math.BigDecimal;

public record WithdrawRequest(
        BigDecimal value,
        String accPass
) {
}
