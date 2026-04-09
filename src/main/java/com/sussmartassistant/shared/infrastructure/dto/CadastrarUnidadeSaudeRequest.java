package com.sussmartassistant.shared.infrastructure.dto;

import com.sussmartassistant.shared.domain.TipoUnidadeSaude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Dados para cadastro de nova unidade de saúde")
public record CadastrarUnidadeSaudeRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Schema(description = "Nome da unidade de saúde", example = "UBS Vila Mariana")
        String nome,

        @NotBlank(message = "CNES é obrigatório")
        @Schema(description = "Código CNES da unidade", example = "2345678")
        String cnes,

        @Schema(description = "Endereço da unidade", example = "Rua Domingos de Morais, 2187")
        String endereco,

        @NotNull(message = "Tipo é obrigatório")
        @Schema(description = "Tipo da unidade de saúde", example = "UBS")
        TipoUnidadeSaude tipo,

        @Schema(description = "Especialidades disponíveis na unidade", example = "[\"Clínica Geral\", \"Pediatria\"]")
        List<String> especialidadesDisponiveis
) {}
