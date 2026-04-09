package com.sussmartassistant.prontuario.infrastructure.dto;

import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados de um registro de atendimento")
public record AtendimentoResponse(
        UUID id,
        UUID prontuarioId,
        UUID profissionalId,
        UUID unidadeSaudeId,
        LocalDate data,
        String queixaPrincipal,
        String anamnese,
        String diagnosticoCid,
        String prescricoes,
        String observacoes,
        Instant criadoEm
) {
    public static AtendimentoResponse from(RegistroAtendimento a) {
        return new AtendimentoResponse(a.getId(), a.getProntuarioId(), a.getProfissionalId(),
                a.getUnidadeSaudeId(), a.getData(), a.getQueixaPrincipal(), a.getAnamnese(),
                a.getDiagnosticoCid(), a.getPrescricoes(), a.getObservacoes(), a.getCriadoEm());
    }
}
