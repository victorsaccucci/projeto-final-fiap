package com.sussmartassistant.seguranca.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para serialização do evento de auditoria na fila RabbitMQ.
 */
public record AuditoriaEventMessage(
        UUID id,
        UUID usuarioId,
        String tipoOperacao,
        UUID prontuarioId,
        LocalDateTime dataHora,
        String detalhes,
        String correlationId
) {}
