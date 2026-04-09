package com.sussmartassistant.metricas.infrastructure.dto;

import com.sussmartassistant.metricas.domain.MetricasAssistente;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Métricas de uso do assistente inteligente")
public record MetricasAssistenteResponse(
        @Schema(description = "Total de solicitações ao assistente") long totalSolicitacoes,
        @Schema(description = "Total de solicitações concluídas") long totalConcluidas,
        @Schema(description = "Total de solicitações com erro") long totalErros,
        @Schema(description = "Data início do período") LocalDate inicio,
        @Schema(description = "Data fim do período") LocalDate fim
) {
    public static MetricasAssistenteResponse from(MetricasAssistente m, LocalDate inicio, LocalDate fim) {
        return new MetricasAssistenteResponse(
                m.totalSolicitacoes(), m.totalConcluidas(), m.totalErros(), inicio, fim);
    }
}
