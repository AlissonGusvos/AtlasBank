package com.atlasbk.Atlas.Account.request;

public record DepositRequest(
        double value,
        String accPass
) {
}
