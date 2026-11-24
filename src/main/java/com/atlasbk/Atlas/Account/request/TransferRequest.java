package com.atlasbk.Atlas.Account.request;

import java.math.BigDecimal;

public record TransferRequest(
        BigDecimal value,
        Integer recvAccNum,
        String accPass
) {
}
