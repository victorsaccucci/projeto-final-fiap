package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.ProcessarSolicitacaoIAUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Consumer RabbitMQ que processa solicitações de IA.
 * Ativa apenas fora do profile dev (em prod, quando RabbitMQ está disponível).
 */
@Component
@Profile("prod")
public class SolicitacaoIARabbitConsumer {

    private static final Logger log = LoggerFactory.getLogger(SolicitacaoIARabbitConsumer.class);

    private final ProcessarSolicitacaoIAUseCase processarSolicitacao;

    public SolicitacaoIARabbitConsumer(ProcessarSolicitacaoIAUseCase processarSolicitacao) {
        this.processarSolicitacao = processarSolicitacao;
    }

    @RabbitListener(queues = AssistenteRabbitConfig.QUEUE)
    public void onMessage(String solicitacaoIdStr) {
        log.info("Mensagem recebida da fila {}: {}", AssistenteRabbitConfig.QUEUE, solicitacaoIdStr);
        try {
            UUID solicitacaoId = UUID.fromString(solicitacaoIdStr);
            processarSolicitacao.executar(solicitacaoId);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem da fila: {}", e.getMessage(), e);
            throw e; // Let RabbitMQ handle retry/DLQ
        }
    }
}
