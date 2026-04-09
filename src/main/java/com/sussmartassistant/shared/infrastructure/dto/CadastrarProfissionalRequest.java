package com.sussmartassistant.shared.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Dados para cadastro de novo profissional de saúde")
public record CadastrarProfissionalRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Schema(description = "Nome do profissional", example = "Dr. João Silva")
        String nome,

        @NotBlank(message = "Registro profissional é obrigatório")
        @Schema(description = "CRM ou registro profissional", example = "CRM-SP 123456")
        String registroProfissional,

        @Schema(description = "Especialidade do profissional", example = "Cardiologia")
        String especialidade,

        @NotNull(message = "Unidade de saúde é obrigatória")
        @Schema(description = "ID da unidade de saúde vinculada")
        UUID unidadeSaudeId
) {}
