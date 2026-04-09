package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.*;
import com.sussmartassistant.prontuario.application.ConsultarProntuarioCompletoUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AssistenteModuleConfig {

    @Value("${assistente.ollama.url:http://localhost:11434}")
    private String ollamaUrl;

    @Value("${assistente.ollama.model:llama3}")
    private String ollamaModel;

    @Value("${assistente.ollama.timeout:30000}")
    private int ollamaTimeout;

    @Bean
    public PromptBuilder promptBuilder() {
        return new ContextualPromptBuilder();
    }

    @Bean
    public ConsultarResultadoIAUseCase consultarResultadoIAUseCase(
            SolicitacaoIARepository solicitacaoRepository) {
        return new ConsultarResultadoIAUseCase(solicitacaoRepository);
    }

    // ── Prod profile: RabbitMQ + Ollama real ──

    @Configuration
    @Profile("prod")
    static class ProdAssistenteConfig {

        @Value("${assistente.ollama.url:http://localhost:11434}")
        private String ollamaUrl;

        @Value("${assistente.ollama.model:llama3}")
        private String ollamaModel;

        @Value("${assistente.ollama.timeout:30000}")
        private int ollamaTimeout;

        @Bean
        public LlmGateway llmGateway() {
            return new OllamaLlmGateway(ollamaUrl, ollamaTimeout);
        }

        @Bean
        public ProcessarSolicitacaoIAUseCase processarSolicitacaoIAUseCase(
                SolicitacaoIARepository solicitacaoRepository,
                ConsultarProntuarioCompletoUseCase consultarProntuario,
                PromptBuilder promptBuilder,
                LlmGateway llmGateway) {
            return new ProcessarSolicitacaoIAUseCase(solicitacaoRepository, consultarProntuario,
                    promptBuilder, llmGateway, ollamaModel);
        }

        @Bean
        public SolicitacaoIAPublisher solicitacaoIAPublisher(
                org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate) {
            return new SolicitacaoIARabbitPublisher(rabbitTemplate);
        }

        @Bean
        public SolicitarHipotesesDiagnosticasUseCase solicitarHipotesesDiagnosticasUseCase(
                SolicitacaoIARepository solicitacaoRepository,
                SolicitacaoIAPublisher publisher) {
            return new SolicitarHipotesesDiagnosticasUseCase(solicitacaoRepository, publisher);
        }
    }

    // ── Dev/Test profile: Sync processing + Mock LLM fallback ──

    @Configuration
    @Profile({"dev", "test"})
    static class DevAssistenteConfig {

        @Value("${assistente.ollama.url:http://localhost:11434}")
        private String ollamaUrl;

        @Value("${assistente.ollama.model:llama3}")
        private String ollamaModel;

        @Value("${assistente.ollama.timeout:30000}")
        private int ollamaTimeout;

        @Bean
        public LlmGateway llmGateway() {
            // Tenta Ollama primeiro, fallback para mock
            LlmGateway ollamaGateway = new OllamaLlmGateway(ollamaUrl, ollamaTimeout);
            return new MockLlmGateway(ollamaGateway);
        }

        @Bean
        public ProcessarSolicitacaoIAUseCase processarSolicitacaoIAUseCase(
                SolicitacaoIARepository solicitacaoRepository,
                ConsultarProntuarioCompletoUseCase consultarProntuario,
                PromptBuilder promptBuilder,
                LlmGateway llmGateway) {
            return new ProcessarSolicitacaoIAUseCase(solicitacaoRepository, consultarProntuario,
                    promptBuilder, llmGateway, ollamaModel);
        }

        @Bean
        public SolicitacaoIAPublisher solicitacaoIAPublisher(
                ProcessarSolicitacaoIAUseCase processarSolicitacao) {
            return new SolicitacaoIASyncPublisher(processarSolicitacao);
        }

        @Bean
        public SolicitarHipotesesDiagnosticasUseCase solicitarHipotesesDiagnosticasUseCase(
                SolicitacaoIARepository solicitacaoRepository,
                SolicitacaoIAPublisher publisher) {
            return new SolicitarHipotesesDiagnosticasUseCase(solicitacaoRepository, publisher);
        }
    }
}
