package com.sussmartassistant.assistente.application;

import java.util.UUID;

/**
 * Port para publicação de solicitações de IA na fila de mensagens.
 */
public interface SolicitacaoIAPublisher {
    void publicar(UUID solicitacaoId);
}
