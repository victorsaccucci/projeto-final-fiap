package com.sussmartassistant.metricas.application;

import com.sussmartassistant.metricas.domain.MetricasAssistente;

import java.time.LocalDate;

/**
 * Consulta métricas de uso do assistente inteligente (solicitações, concluídas, erros).
 */
public class ConsultarMetricasAssistenteUseCase {

    private final MetricasRepository metricasRepository;

    public ConsultarMetricasAssistenteUseCase(MetricasRepository metricasRepository) {
        this.metricasRepository = metricasRepository;
    }

    public MetricasAssistente executar(LocalDate inicio, LocalDate fim) {
        return metricasRepository.buscarMetricasAssistente(inicio, fim);
    }
}
