package com.sussmartassistant.shared.domain;

/**
 * Exceção para quando o serviço Ollama/LLM está indisponível ou timeout.
 */
public class LlmUnavailableException extends RuntimeException {

    public LlmUnavailableException(String mensagem) {
        super(mensagem);
    }

    public LlmUnavailableException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
