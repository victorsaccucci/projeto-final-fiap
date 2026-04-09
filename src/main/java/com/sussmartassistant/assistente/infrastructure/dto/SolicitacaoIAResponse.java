package com.sussmartassistant.assistente.infrastructure.dto;

import com.sussmartassistant.assistente.domain.SolicitacaoIA;
import com.sussmartassistant.shared.domain.StatusSolicitacaoIA;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SolicitacaoIAResponse(
        UUID id,
        UUID pacienteId,
        UUID profissionalId,
        StatusSolicitacaoIA status,
        String sintomasInformados,
        Instant criadoEm,
        Instant concluidoEm,
        List<HipoteseDiagnosticaResponse> hipoteses
) {
    public static SolicitacaoIAResponse from(SolicitacaoIA s) {
        List<HipoteseDiagnosticaResponse> hipoteses = s.getHipoteses() != null
                ? s.getHipoteses().stream().map(HipoteseDiagnosticaResponse::from).toList()
                : List.of();

        return new SolicitacaoIAResponse(
                s.getId(), s.getPacienteId(), s.getProfissionalId(),
                s.getStatus(), s.getSintomasInformados(),
                s.getCriadoEm(), s.getConcluidoEm(), hipoteses
        );
    }
}
