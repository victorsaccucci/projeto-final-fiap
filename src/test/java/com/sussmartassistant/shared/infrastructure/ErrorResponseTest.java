package com.sussmartassistant.shared.infrastructure;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void deveCriarErrorResponseSimples() {
        ErrorResponse response = new ErrorResponse(400, "Erro de domínio", "abc-123");
        assertEquals(400, response.status());
        assertEquals("Erro de domínio", response.mensagem());
        assertEquals("abc-123", response.correlationId());
        assertNotNull(response.timestamp());
        assertNull(response.errosValidacao());
    }

    @Test
    void deveCriarErrorResponseComErrosValidacao() {
        Map<String, String> erros = Map.of("cpf", "CPF inválido");
        ErrorResponse response = new ErrorResponse(400, "Erro de validação", "abc-123", erros);
        assertEquals(400, response.status());
        assertNotNull(response.errosValidacao());
        assertEquals("CPF inválido", response.errosValidacao().get("cpf"));
    }

    @Test
    void deveGerarTimestampAutomaticamente() {
        ErrorResponse response = new ErrorResponse(500, "Erro interno", null);
        assertNotNull(response.timestamp());
        assertFalse(response.timestamp().isBlank());
    }
}
