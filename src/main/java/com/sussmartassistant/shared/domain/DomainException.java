package com.sussmartassistant.shared.domain;

/**
 * Exceção base para violações de regras de domínio.
 * Não depende de nenhum framework — pertence à camada de domínio pura.
 */
public class DomainException extends RuntimeException {

    public DomainException(String mensagem) {
        super(mensagem);
    }

    public DomainException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
