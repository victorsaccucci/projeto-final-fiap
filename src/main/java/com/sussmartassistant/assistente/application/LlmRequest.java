package com.sussmartassistant.assistente.application;

/**
 * Request para envio ao LLM.
 */
public record LlmRequest(String model, String prompt) {}
