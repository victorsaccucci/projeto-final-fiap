package com.sussmartassistant.metricas.application;

import com.sussmartassistant.metricas.domain.DiagnosticoFrequente;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Consulta os diagnósticos (CIDs) mais frequentes por período e unidade.
 */
public class ConsultarDiagnosticosFrequentesUseCase {

    private final MetricasRepository metricasRepository;

    public ConsultarDiagnosticosFrequentesUseCase(MetricasRepository metricasRepository) {
        this.metricasRepository = metricasRepository;
    }

    public List<DiagnosticoFrequente> executar(LocalDate inicio, LocalDate fim, UUID unidadeId) {
        return metricasRepository.buscarDiagnosticosFrequentes(inicio, fim, unidadeId);
    }
}
