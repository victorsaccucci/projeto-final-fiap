package com.sussmartassistant.metricas.application;

import com.sussmartassistant.metricas.domain.DiagnosticoFrequente;
import com.sussmartassistant.metricas.domain.MetricasAssistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MetricasUseCasesTest {

    private MetricasRepository metricasRepository;
    private ConsultarMetricasAtendimentoUseCase consultarAtendimentos;
    private ConsultarDiagnosticosFrequentesUseCase consultarDiagnosticos;
    private ConsultarMetricasAssistenteUseCase consultarAssistente;

    @BeforeEach
    void setUp() {
        metricasRepository = new MetricasRepository() {
            @Override
            public long contarAtendimentosPorPeriodoEUnidade(LocalDate inicio, LocalDate fim, UUID unidadeId) {
                if (unidadeId != null) return 15;
                if (inicio != null && fim != null) return 42;
                return 100;
            }

            @Override
            public List<DiagnosticoFrequente> buscarDiagnosticosFrequentes(LocalDate inicio, LocalDate fim, UUID unidadeId) {
                return List.of(
                        new DiagnosticoFrequente("J06", "IVAS", 25),
                        new DiagnosticoFrequente("I10", "Hipertensão", 18),
                        new DiagnosticoFrequente("K29", "Gastrite", 12)
                );
            }

            @Override
            public MetricasAssistente buscarMetricasAssistente(LocalDate inicio, LocalDate fim) {
                return new MetricasAssistente(50, 45, 5);
            }
        };

        consultarAtendimentos = new ConsultarMetricasAtendimentoUseCase(metricasRepository);
        consultarDiagnosticos = new ConsultarDiagnosticosFrequentesUseCase(metricasRepository);
        consultarAssistente = new ConsultarMetricasAssistenteUseCase(metricasRepository);
    }

    @Test
    void deveRetornarTotalAtendimentosSemFiltro() {
        long total = consultarAtendimentos.executar(null, null, null);
        assertEquals(100, total);
    }

    @Test
    void deveRetornarTotalAtendimentosPorPeriodo() {
        long total = consultarAtendimentos.executar(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null);
        assertEquals(42, total);
    }

    @Test
    void deveRetornarTotalAtendimentosPorUnidade() {
        long total = consultarAtendimentos.executar(null, null, UUID.randomUUID());
        assertEquals(15, total);
    }

    @Test
    void deveRetornarDiagnosticosFrequentes() {
        List<DiagnosticoFrequente> diagnosticos = consultarDiagnosticos.executar(null, null, null);

        assertEquals(3, diagnosticos.size());
        assertEquals("J06", diagnosticos.get(0).cid());
        assertEquals(25, diagnosticos.get(0).count());
    }

    @Test
    void deveRetornarMetricasAssistente() {
        MetricasAssistente metricas = consultarAssistente.executar(null, null);

        assertEquals(50, metricas.totalSolicitacoes());
        assertEquals(45, metricas.totalConcluidas());
        assertEquals(5, metricas.totalErros());
    }
}
