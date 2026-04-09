package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.ProcessarSolicitacaoIAUseCase;
import com.sussmartassistant.assistente.application.SolicitacaoIAPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Publicador síncrono para uso em dev (sem RabbitMQ).
 * Em vez de publicar na fila, processa a solicitação diretamente.
 */
public class SolicitacaoIASyncPublisher implements SolicitacaoIAPublisher {

    private static final Logger log = LoggerFactory.getLogger(SolicitacaoIASyncPublisher.class);

    private final ProcessarSolicitacaoIAUseCase processarSolicitacao;

    public SolicitacaoIASyncPublisher(ProcessarSolicitacaoIAUseCase processarSolicitacao) {
        this.processarSolicitacao = processarSolicitacao;
    }

    @Override
    public void publicar(UUID solicitacaoId) {
        log.info("Processando solicitação IA {} de forma síncrona (RabbitMQ indisponível)", solicitacaoId);
        processarSolicitacao.executar(solicitacaoId);
    }
}
