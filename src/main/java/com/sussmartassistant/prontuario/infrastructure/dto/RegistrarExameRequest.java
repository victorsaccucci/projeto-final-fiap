package com.sussmartassistant.prontuario.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados para registro de exame")
public record RegistrarExameRequest(
        @NotBlank(message = "Tipo do exame é obrigatório")
        @Schema(description = "Tipo do exame", example = "Hemograma")
        String tipo,

        @NotNull(message = "Data de realização é obrigatória")
        @Schema(description = "Data de realização do exame", example = "2024-06-10")
        LocalDate dataRealizacao,

        @Schema(description = "Resultado do exame")
        String resultado,

        @Schema(description = "ID da unidade de saúde de origem")
        UUID unidadeSaudeOrigemId,

        @Schema(description = "ID do profissional que registrou (opcional, usa o do token JWT)")
        UUID registradoPorId
) {}
