package com.sussmartassistant.seguranca.infrastructure.dto;

public record LoginResponse(
        String token,
        String role,
        String username
) {}
