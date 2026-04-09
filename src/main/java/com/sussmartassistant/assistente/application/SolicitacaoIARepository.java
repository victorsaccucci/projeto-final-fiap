package com.sussmartassistant.assistente.application;

import com.sussmartassistant.assistente.domain.SolicitacaoIA;

import java.util.Optional;
import java.util.UUID;

/**
 * Port de repositório para SolicitacaoIA.
 */
public interface SolicitacaoIARepository {
    SolicitacaoIA salvar(SolicitacaoIA solicitacao);
    Optional<SolicitacaoIA> buscarPorId(UUID id);
}
