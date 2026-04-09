package com.sussmartassistant.metricas.domain;

/**
 * Métricas de uso do assistente inteligente.
 */
public record MetricasAssistente(
        long totalSolicitacoes,
        long totalConcluidas,
        long totalErros
) {}
