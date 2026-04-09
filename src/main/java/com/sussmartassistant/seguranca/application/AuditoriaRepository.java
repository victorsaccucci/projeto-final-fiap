package com.sussmartassistant.seguranca.application;

import com.sussmartassistant.seguranca.domain.EventoAuditoria;

import java.util.List;
import java.util.UUID;

/**
 * Port de repositório para eventos de auditoria.
 */
public interface AuditoriaRepository {
    void registrar(EventoAuditoria evento);
    List<EventoAuditoria> buscarPorProntuarioId(UUID prontuarioId);
}
