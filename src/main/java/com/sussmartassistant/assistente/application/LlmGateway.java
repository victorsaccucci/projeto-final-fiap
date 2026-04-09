package com.sussmartassistant.assistente.application;

/**
 * Contrato abstrato para LLM — permite trocar Ollama por outro provedor.
 */
public interface LlmGateway {
    LlmResponse enviar(LlmRequest request);
}
