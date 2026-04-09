package com.sussmartassistant.shared.infrastructure;

import java.time.Instant;
import java.util.Map;

/**
 * Resposta padronizada de erro para todas as exceções da API.
 */
public record ErrorResponse(
        int status,
        String mensagem,
        String timestamp,
        String correlationId,
        Map<String, String> errosValidacao
) {

    public ErrorResponse(int status, String mensagem, String correlationId) {
        this(status, mensagem, Instant.now().toString(), correlationId, null);
    }

    public ErrorResponse(int status, String mensagem, String correlationId, Map<String, String> errosValidacao) {
        this(status, mensagem, Instant.now().toString(), correlationId, errosValidacao);
    }
}
