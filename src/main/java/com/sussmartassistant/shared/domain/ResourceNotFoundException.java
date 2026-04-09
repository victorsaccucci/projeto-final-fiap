package com.sussmartassistant.shared.domain;

/**
 * Exceção para recursos não encontrados (Paciente, Prontuário, etc.).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}
