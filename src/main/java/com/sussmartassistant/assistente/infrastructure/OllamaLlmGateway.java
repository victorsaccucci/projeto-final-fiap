package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.LlmGateway;
import com.sussmartassistant.assistente.application.LlmRequest;
import com.sussmartassistant.assistente.application.LlmResponse;
import com.sussmartassistant.shared.domain.LlmUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class OllamaLlmGateway implements LlmGateway {

    private static final Logger log = LoggerFactory.getLogger(OllamaLlmGateway.class);

    private final RestClient restClient;

    public OllamaLlmGateway(String baseUrl, int timeoutMs) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeoutMs);
        factory.setReadTimeout(timeoutMs);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

    @Override
    public LlmResponse enviar(LlmRequest request) {
        try {
            log.info("Enviando prompt ao Ollama (model: {})", request.model());

            Map<String, Object> body = Map.of(
                    "model", request.model(),
                    "prompt", request.prompt(),
                    "stream", false
            );

            Map<?, ?> response = restClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response == null || !response.containsKey("response")) {
                throw new LlmUnavailableException("Resposta inválida do Ollama: campo 'response' ausente");
            }

            String responseText = String.valueOf(response.get("response"));
            log.info("Resposta recebida do Ollama ({} caracteres)", responseText.length());
            return new LlmResponse(responseText);

        } catch (LlmUnavailableException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao comunicar com Ollama: {}", e.getMessage());
            throw new LlmUnavailableException("Ollama indisponível: " + e.getMessage(), e);
        }
    }
}
