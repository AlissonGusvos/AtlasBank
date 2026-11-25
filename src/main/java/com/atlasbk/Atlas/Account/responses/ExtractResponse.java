package com.atlasbk.Atlas.Account.responses;

import com.atlasbk.Atlas.Account.Types.OpType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExtractResponse(
        OpType opType,
        BigDecimal value,
        LocalDateTime date,
        String transferNameReceiver,
        String direction
) {
}
