package com.sussmartassistant.shared.infrastructure.dto;

import com.sussmartassistant.shared.domain.ProfissionalSaude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados do profissional de saúde")
public record ProfissionalSaudeResponse(
        UUID id,
        String nome,
        String registroProfissional,
        String especialidade,
        UUID unidadeSaudeId
) {
    public static ProfissionalSaudeResponse from(ProfissionalSaude p) {
        return new ProfissionalSaudeResponse(
                p.getId(), p.getNome(), p.getRegistroProfissional(),
                p.getEspecialidade(), p.getUnidadeSaudeId()
        );
    }
}
