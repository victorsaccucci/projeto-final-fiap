package com.sussmartassistant.paciente.infrastructure.dto;

import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados de um medicamento em uso")
public record MedicamentoResponse(
        UUID id,
        UUID pacienteId,
        String nome,
        String dosagem,
        String frequencia,
        LocalDate dataInicio,
        LocalDate dataDescontinuacao,
        boolean ativo,
        Instant registradoEm,
        UUID registradoPorId
) {
    public static MedicamentoResponse from(MedicamentoEmUso m) {
        return new MedicamentoResponse(m.getId(), m.getPacienteId(), m.getNome(), m.getDosagem(),
                m.getFrequencia(), m.getDataInicio(), m.getDataDescontinuacao(), m.isAtivo(),
                m.getRegistradoEm(), m.getRegistradoPorId());
    }
}
