package com.sussmartassistant.assistente.application;

import com.sussmartassistant.assistente.domain.SolicitacaoIA;
import com.sussmartassistant.shared.domain.ValidationException;

import java.util.List;
import java.util.UUID;

/**
 * Use case para solicitar hipóteses diagnósticas.
 * Cria a solicitação com status PENDENTE e publica na fila RabbitMQ para processamento assíncrono.
 */
public class SolicitarHipotesesDiagnosticasUseCase {

    private final SolicitacaoIARepository solicitacaoRepository;
    private final SolicitacaoIAPublisher publisher;

    public SolicitarHipotesesDiagnosticasUseCase(SolicitacaoIARepository solicitacaoRepository,
                                                  SolicitacaoIAPublisher publisher) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.publisher = publisher;
    }

    public UUID executar(UUID pacienteId, List<String> sintomas, UUID profissionalId) {
        if (sintomas == null || sintomas.isEmpty()) {
            throw new ValidationException("É necessário informar ao menos um sintoma para solicitar hipóteses diagnósticas");
        }

        List<String> sintomasValidos = sintomas.stream()
                .filter(s -> s != null && !s.isBlank())
                .toList();

        if (sintomasValidos.isEmpty()) {
            throw new ValidationException("É necessário informar ao menos um sintoma válido (não vazio)");
        }

        String sintomasTexto = String.join("; ", sintomasValidos);
        SolicitacaoIA solicitacao = SolicitacaoIA.criar(pacienteId, profissionalId, sintomasTexto);
        solicitacaoRepository.salvar(solicitacao);

        publisher.publicar(solicitacao.getId());

        return solicitacao.getId();
    }
}
