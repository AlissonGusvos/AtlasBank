package com.atlasbk.Atlas.Auth.requests;

public record UserLoginRequest(
        String cpf,
        String password
) {
}
