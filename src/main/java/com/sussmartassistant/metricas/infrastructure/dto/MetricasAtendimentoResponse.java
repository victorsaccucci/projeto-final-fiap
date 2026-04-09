package com.sussmartassistant.metricas.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Métricas de atendimentos por período e unidade")
public record MetricasAtendimentoResponse(
        @Schema(description = "Total de atendimentos no período") long totalAtendimentos,
        @Schema(description = "Data início do período") LocalDate inicio,
        @Schema(description = "Data fim do período") LocalDate fim,
        @Schema(description = "ID da unidade de saúde (null se todas)") UUID unidadeId
) {}
