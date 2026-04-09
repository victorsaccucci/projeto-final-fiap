package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.domain.DomainException;
import com.sussmartassistant.shared.domain.LlmUnavailableException;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;
import com.sussmartassistant.shared.domain.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Tratamento global de exceções — converte exceções em respostas HTTP padronizadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String CORRELATION_ID_KEY = "correlationId";

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.warn("Erro de validação de domínio: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        log.warn("Exceção de domínio: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("Erro de validação: {}", erros);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos dados de entrada",
                MDC.get(CORRELATION_ID_KEY),
                erros
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.warn("JSON malformado: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "JSON malformado ou tipos incompatíveis na requisição");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Falha de autenticação: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Autenticação necessária para acessar este recurso");
    }

    @ExceptionHandler(com.sussmartassistant.shared.domain.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleDomainAccessDeniedException(
            com.sussmartassistant.shared.domain.AccessDeniedException ex) {
        log.warn("Acesso negado (domínio): {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "Acesso negado: você não tem permissão para este recurso");
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleSpringAccessDeniedException(
            org.springframework.security.access.AccessDeniedException ex) {
        log.warn("Acesso negado: {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "Acesso negado: você não tem permissão para este recurso");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(LlmUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleLlmUnavailableException(LlmUnavailableException ex) {
        log.error("Serviço LLM indisponível: {}", ex.getMessage());
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de IA temporariamente indisponível");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Erro interno inesperado", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String mensagem) {
        ErrorResponse response = new ErrorResponse(status.value(), mensagem, MDC.get(CORRELATION_ID_KEY));
        return ResponseEntity.status(status).body(response);
    }
}
