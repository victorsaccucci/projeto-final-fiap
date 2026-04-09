package com.sussmartassistant.shared.domain;

/**
 * Exceção para dados de entrada inválidos ou campos obrigatórios ausentes.
 */
public class ValidationException extends DomainException {

    public ValidationException(String mensagem) {
        super(mensagem);
    }
}
