package com.sussmartassistant.seguranca.infrastructure;

import com.sussmartassistant.seguranca.application.AuditoriaRepository;
import com.sussmartassistant.seguranca.domain.EventoAuditoria;
import com.sussmartassistant.seguranca.infrastructure.dto.AuditoriaEventMessage;
import com.sussmartassistant.shared.domain.TipoOperacaoAuditoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Consumer que consome eventos de auditoria da fila RabbitMQ e persiste no banco.
 */
@Component
@Profile("prod")
public class AuditoriaEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaEventConsumer.class);

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaEventConsumer(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @RabbitListener(queues = AuditoriaRabbitConfig.QUEUE, autoStartup = "${spring.rabbitmq.listener.simple.auto-startup:true}")
    public void consumir(AuditoriaEventMessage message) {
        log.debug("Consumindo evento de auditoria: {}", message.id());
        try {
            EventoAuditoria evento = new EventoAuditoria(
                    message.id(),
                    message.usuarioId(),
                    TipoOperacaoAuditoria.valueOf(message.tipoOperacao()),
                    message.prontuarioId(),
                    message.dataHora(),
                    message.detalhes(),
                    message.correlationId());
            auditoriaRepository.registrar(evento);
            log.info("Evento de auditoria persistido: {}", message.id());
        } catch (Exception e) {
            log.error("Erro ao processar evento de auditoria {}: {}", message.id(), e.getMessage());
            throw e; // Let RabbitMQ send to DLQ
        }
    }
}
