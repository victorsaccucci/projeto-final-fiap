package com.sussmartassistant.prontuario.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados para registro de atendimento")
public record RegistrarAtendimentoRequest(
        @NotNull(message = "ID do profissional é obrigatório")
        @Schema(description = "ID do profissional de saúde", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID profissionalId,

        @Schema(description = "ID da unidade de saúde")
        UUID unidadeSaudeId,

        @NotNull(message = "Data do atendimento é obrigatória")
        @Schema(description = "Data do atendimento", example = "2024-06-15")
        LocalDate data,

        @NotBlank(message = "Queixa principal é obrigatória")
        @Schema(description = "Queixa principal do paciente", example = "Dor de cabeça persistente")
        String queixaPrincipal,

        @Schema(description = "Anamnese detalhada")
        String anamnese,

        @Schema(description = "Código CID do diagnóstico", example = "R51")
        String diagnosticoCid,

        @Schema(description = "Prescrições médicas")
        String prescricoes,

        @Schema(description = "Observações adicionais")
        String observacoes
) {}
