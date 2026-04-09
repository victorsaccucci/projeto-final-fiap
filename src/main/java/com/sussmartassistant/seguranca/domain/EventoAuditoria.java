package com.sussmartassistant.seguranca.domain;

import com.sussmartassistant.shared.domain.TipoOperacaoAuditoria;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Record de domínio representando um evento de auditoria.
 */
public record EventoAuditoria(
        UUID id,
        UUID usuarioId,
        TipoOperacaoAuditoria tipoOperacao,
        UUID prontuarioId,
        LocalDateTime dataHora,
        String detalhes,
        String correlationId
) {
    public EventoAuditoria {
        if (id == null) id = UUID.randomUUID();
        if (dataHora == null) dataHora = LocalDateTime.now();
    }
}
