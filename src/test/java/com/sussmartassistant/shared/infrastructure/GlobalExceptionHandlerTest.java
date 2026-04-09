package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.domain.DomainException;
import com.sussmartassistant.shared.domain.LlmUnavailableException;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;
import com.sussmartassistant.shared.domain.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void deveRetornar400ParaDomainException() {
        ResponseEntity<ErrorResponse> response = handler.handleDomainException(
                new DomainException("CPF inválido"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals("CPF inválido", response.getBody().mensagem());
    }

    @Test
    void deveRetornar401ParaAuthenticationException() {
        ResponseEntity<ErrorResponse> response = handler.handleAuthenticationException(
                new BadCredentialsException("Credenciais inválidas"));
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode().value());
    }

    @Test
    void deveRetornar403ParaAccessDeniedException() {
        ResponseEntity<ErrorResponse> response = handler.handleSpringAccessDeniedException(
                new org.springframework.security.access.AccessDeniedException("Sem permissão"));
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
    }

    @Test
    void deveRetornar403ParaDomainAccessDeniedException() {
        ResponseEntity<ErrorResponse> response = handler.handleDomainAccessDeniedException(
                new com.sussmartassistant.shared.domain.AccessDeniedException("Sem permissão"));
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
    }

    @Test
    void deveRetornar400ParaValidationException() {
        ResponseEntity<ErrorResponse> response = handler.handleValidationException(
                new ValidationException("Campo obrigatório ausente"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals("Campo obrigatório ausente", response.getBody().mensagem());
    }

    @Test
    void deveRetornar404ParaResourceNotFoundException() {
        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFoundException(
                new ResourceNotFoundException("Paciente não encontrado"));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
        assertEquals("Paciente não encontrado", response.getBody().mensagem());
    }

    @Test
    void deveRetornar503ParaLlmUnavailableException() {
        ResponseEntity<ErrorResponse> response = handler.handleLlmUnavailableException(
                new LlmUnavailableException("Ollama timeout"));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatusCode().value());
        assertEquals("Serviço de IA temporariamente indisponível", response.getBody().mensagem());
    }

    @Test
    void deveRetornar500ParaExcecaoGenerica_SemExporStackTrace() {
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(
                new RuntimeException("NullPointerException interna"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertEquals("Erro interno do servidor", response.getBody().mensagem());
        // Garante que a mensagem genérica não expõe detalhes internos
        assertFalse(response.getBody().mensagem().contains("NullPointer"));
    }
}
