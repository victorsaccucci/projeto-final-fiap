package com.sussmartassistant.assistente.application;

import com.sussmartassistant.assistente.domain.SolicitacaoIA;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.util.UUID;

/**
 * Use case para consultar o resultado de uma solicitação de IA.
 */
public class ConsultarResultadoIAUseCase {

    private final SolicitacaoIARepository solicitacaoRepository;

    public ConsultarResultadoIAUseCase(SolicitacaoIARepository solicitacaoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
    }

    public SolicitacaoIA executar(UUID solicitacaoId) {
        return solicitacaoRepository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação IA não encontrada: " + solicitacaoId));
    }
}
