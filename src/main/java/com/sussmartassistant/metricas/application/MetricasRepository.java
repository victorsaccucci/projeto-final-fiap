package com.sussmartassistant.metricas.application;

import com.sussmartassistant.metricas.domain.DiagnosticoFrequente;
import com.sussmartassistant.metricas.domain.MetricasAssistente;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Port para consulta de métricas agregadas.
 */
public interface MetricasRepository {

    long contarAtendimentosPorPeriodoEUnidade(LocalDate inicio, LocalDate fim, UUID unidadeId);

    List<DiagnosticoFrequente> buscarDiagnosticosFrequentes(LocalDate inicio, LocalDate fim, UUID unidadeId);

    MetricasAssistente buscarMetricasAssistente(LocalDate inicio, LocalDate fim);
}
