package com.sussmartassistant.shared.domain;

/**
 * Exceção de domínio para acesso negado a recursos protegidos.
 * Separada da AccessDeniedException do Spring Security para manter o domínio puro.
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String mensagem) {
        super(mensagem);
    }
}
