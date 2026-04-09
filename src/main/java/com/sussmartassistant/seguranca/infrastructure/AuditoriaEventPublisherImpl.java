package com.sussmartassistant.seguranca.infrastructure;

import com.sussmartassistant.seguranca.application.AuditoriaEventPublisher;
import com.sussmartassistant.seguranca.application.AuditoriaRepository;
import com.sussmartassistant.seguranca.domain.EventoAuditoria;
import com.sussmartassistant.seguranca.infrastructure.dto.AuditoriaEventMessage;
import com.sussmartassistant.shared.domain.TipoOperacaoAuditoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Publica eventos de auditoria na fila RabbitMQ.
 * Se RabbitMQ estiver indisponível (ou em dev), persiste de forma síncrona como fallback.
 */
@Component
public class AuditoriaEventPublisherImpl implements AuditoriaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaEventPublisherImpl.class);

    private final RabbitTemplate rabbitTemplate;
    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaEventPublisherImpl(@Autowired(required = false) RabbitTemplate rabbitTemplate,
                                        AuditoriaRepository auditoriaRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    public void publicar(UUID usuarioId, TipoOperacaoAuditoria tipoOperacao,
                         UUID prontuarioId, String detalhes) {
        String correlationId = MDC.get("correlationId");
        UUID eventId = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();

        if (rabbitTemplate != null) {
            try {
                AuditoriaEventMessage message = new AuditoriaEventMessage(
                        eventId, usuarioId, tipoOperacao.name(), prontuarioId, agora, detalhes, correlationId);
                rabbitTemplate.convertAndSend(
                        AuditoriaRabbitConfig.EXCHANGE,
                        AuditoriaRabbitConfig.ROUTING_KEY,
                        message);
                log.debug("Evento de auditoria publicado na fila: {}", eventId);
                return;
            } catch (AmqpException e) {
                log.warn("RabbitMQ indisponível — persistindo auditoria de forma síncrona: {}", e.getMessage());
            }
        }

        persistirSincrono(eventId, usuarioId, tipoOperacao, prontuarioId, agora, detalhes, correlationId);
    }

    private void persistirSincrono(UUID id, UUID usuarioId, TipoOperacaoAuditoria tipoOperacao,
                                    UUID prontuarioId, LocalDateTime dataHora,
                                    String detalhes, String correlationId) {
        EventoAuditoria evento = new EventoAuditoria(
                id, usuarioId, tipoOperacao, prontuarioId, dataHora, detalhes, correlationId);
        auditoriaRepository.registrar(evento);
        log.info("Evento de auditoria persistido de forma síncrona: {}", id);
    }
}
