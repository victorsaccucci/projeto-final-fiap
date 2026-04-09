package com.sussmartassistant.assistente.infrastructure.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SolicitarHipotesesRequest(
        @NotNull(message = "pacienteId é obrigatório")
        UUID pacienteId,

        @NotNull(message = "profissionalId é obrigatório")
        UUID profissionalId,

        @NotEmpty(message = "sintomas não pode ser vazio")
        List<String> sintomas
) {}
