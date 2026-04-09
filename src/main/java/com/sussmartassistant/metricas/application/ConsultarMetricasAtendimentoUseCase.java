package com.sussmartassistant.metricas.application;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Consulta o total de atendimentos por período e unidade de saúde.
 */
public class ConsultarMetricasAtendimentoUseCase {

    private final MetricasRepository metricasRepository;

    public ConsultarMetricasAtendimentoUseCase(MetricasRepository metricasRepository) {
        this.metricasRepository = metricasRepository;
    }

    public long executar(LocalDate inicio, LocalDate fim, UUID unidadeId) {
        return metricasRepository.contarAtendimentosPorPeriodoEUnidade(inicio, fim, unidadeId);
    }
}
