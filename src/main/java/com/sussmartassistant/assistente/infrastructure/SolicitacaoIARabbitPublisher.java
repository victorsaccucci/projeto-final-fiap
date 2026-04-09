package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.SolicitacaoIAPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

/**
 * Publicador de solicitações de IA na fila RabbitMQ.
 */
public class SolicitacaoIARabbitPublisher implements SolicitacaoIAPublisher {

    private static final Logger log = LoggerFactory.getLogger(SolicitacaoIARabbitPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public SolicitacaoIARabbitPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicar(UUID solicitacaoId) {
        log.info("Publicando solicitação IA {} na fila {}", solicitacaoId, AssistenteRabbitConfig.QUEUE);
        rabbitTemplate.convertAndSend(
                AssistenteRabbitConfig.EXCHANGE,
                AssistenteRabbitConfig.ROUTING_KEY,
                solicitacaoId.toString()
        );
    }
}
