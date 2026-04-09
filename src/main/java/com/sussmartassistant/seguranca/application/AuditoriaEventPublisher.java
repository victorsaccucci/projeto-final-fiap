package com.sussmartassistant.seguranca.application;

import com.sussmartassistant.shared.domain.TipoOperacaoAuditoria;

import java.util.UUID;

/**
 * Port para publicação de eventos de auditoria.
 * A implementação pode ser via RabbitMQ (assíncrona) ou síncrona (fallback).
 */
public interface AuditoriaEventPublisher {
    void publicar(UUID usuarioId, TipoOperacaoAuditoria tipoOperacao, UUID prontuarioId, String detalhes);
}
