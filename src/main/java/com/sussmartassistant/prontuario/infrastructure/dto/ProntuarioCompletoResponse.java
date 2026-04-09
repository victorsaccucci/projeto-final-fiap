package com.sussmartassistant.prontuario.infrastructure.dto;

import com.sussmartassistant.paciente.infrastructure.dto.AlergiaResponse;
import com.sussmartassistant.paciente.infrastructure.dto.MedicamentoResponse;
import com.sussmartassistant.prontuario.application.ProntuarioCompleto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Prontuário completo do paciente")
public record ProntuarioCompletoResponse(
        UUID prontuarioId,
        UUID pacienteId,
        Instant criadoEm,
        Instant atualizadoEm,
        List<AlergiaResponse> alergias,
        List<MedicamentoResponse> medicamentosAtivos,
        List<AtendimentoResponse> atendimentos,
        List<ExameResponse> exames
) {
    public static ProntuarioCompletoResponse from(ProntuarioCompleto pc) {
        return new ProntuarioCompletoResponse(
                pc.prontuario().getId(),
                pc.prontuario().getPacienteId(),
                pc.prontuario().getCriadoEm(),
                pc.prontuario().getAtualizadoEm(),
                pc.alergias().stream().map(AlergiaResponse::from).toList(),
                pc.medicamentosAtivos().stream().map(MedicamentoResponse::from).toList(),
                pc.atendimentos().stream().map(AtendimentoResponse::from).toList(),
                pc.exames().stream().map(ExameResponse::from).toList()
        );
    }
}
