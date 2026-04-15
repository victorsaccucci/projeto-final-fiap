package com.sussmartassistant.paciente.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados para registro de medicamento em uso")
public record RegistrarMedicamentoRequest(
        @NotBlank(message = "Nome do medicamento é obrigatório")
        @Schema(description = "Nome do medicamento", example = "Losartana")
        String nome,

        @NotBlank(message = "Dosagem é obrigatória")
        @Schema(description = "Dosagem do medicamento", example = "50mg")
        String dosagem,

        @NotBlank(message = "Frequência é obrigatória")
        @Schema(description = "Frequência de uso", example = "1x ao dia")
        String frequencia,

        @NotNull(message = "Data de início é obrigatória")
        @Schema(description = "Data de início do uso", example = "2024-01-15")
        LocalDate dataInicio,

        @Schema(description = "ID do profissional que registrou (opcional, usa o do token JWT)")
        UUID registradoPorId
) {}
