package com.atlasbk.Atlas.Auth.requests;

public record UserRegisterRequest(
        String email,
        String cpf,
        String password,
        String username,
        String accPass
) {
}
