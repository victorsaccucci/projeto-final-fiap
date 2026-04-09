package com.sussmartassistant.shared.infrastructure.dto;

import com.sussmartassistant.shared.domain.TipoUnidadeSaude;
import com.sussmartassistant.shared.domain.UnidadeSaude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Dados da unidade de saúde")
public record UnidadeSaudeResponse(
        UUID id,
        String nome,
        String cnes,
        String endereco,
        TipoUnidadeSaude tipo,
        List<String> especialidadesDisponiveis
) {
    public static UnidadeSaudeResponse from(UnidadeSaude u) {
        return new UnidadeSaudeResponse(
                u.getId(), u.getNome(), u.getCnes(), u.getEndereco(),
                u.getTipo(), u.getEspecialidadesDisponiveis()
        );
    }
}
