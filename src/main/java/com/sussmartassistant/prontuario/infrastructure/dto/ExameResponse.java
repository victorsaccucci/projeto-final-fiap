package com.sussmartassistant.prontuario.infrastructure.dto;

import com.sussmartassistant.prontuario.domain.Exame;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados de um exame registrado")
public record ExameResponse(
        UUID id,
        UUID prontuarioId,
        String tipo,
        LocalDate dataRealizacao,
        String resultado,
        UUID unidadeSaudeOrigemId,
        Instant registradoEm,
        UUID registradoPorId
) {
    public static ExameResponse from(Exame e) {
        return new ExameResponse(e.getId(), e.getProntuarioId(), e.getTipo(), e.getDataRealizacao(),
                e.getResultado(), e.getUnidadeSaudeOrigemId(), e.getRegistradoEm(), e.getRegistradoPorId());
    }
}
