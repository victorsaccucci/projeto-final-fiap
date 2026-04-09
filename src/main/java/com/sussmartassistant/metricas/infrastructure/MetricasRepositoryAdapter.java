package com.sussmartassistant.metricas.infrastructure;

import com.sussmartassistant.metricas.application.MetricasRepository;
import com.sussmartassistant.metricas.domain.DiagnosticoFrequente;
import com.sussmartassistant.metricas.domain.MetricasAssistente;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Repository
public class MetricasRepositoryAdapter implements MetricasRepository {

    private final JpaMetricasAtendimentoRepository atendimentoRepo;
    private final JpaMetricasSolicitacaoRepository solicitacaoRepo;

    public MetricasRepositoryAdapter(JpaMetricasAtendimentoRepository atendimentoRepo,
                                      JpaMetricasSolicitacaoRepository solicitacaoRepo) {
        this.atendimentoRepo = atendimentoRepo;
        this.solicitacaoRepo = solicitacaoRepo;
    }

    @Override
    public long contarAtendimentosPorPeriodoEUnidade(LocalDate inicio, LocalDate fim, UUID unidadeId) {
        return atendimentoRepo.countByPeriodoAndUnidade(inicio, fim, unidadeId);
    }

    @Override
    public List<DiagnosticoFrequente> buscarDiagnosticosFrequentes(LocalDate inicio, LocalDate fim, UUID unidadeId) {
        return atendimentoRepo.findDiagnosticosFrequentes(inicio, fim, unidadeId).stream()
                .map(row -> new DiagnosticoFrequente(
                        (String) row[0],
                        (String) row[0], // CID code used as description (no separate description table)
                        (Long) row[1]))
                .toList();
    }

    @Override
    public MetricasAssistente buscarMetricasAssistente(LocalDate inicio, LocalDate fim) {
        Instant inicioInstant = inicio != null ? inicio.atStartOfDay().toInstant(ZoneOffset.UTC) : null;
        Instant fimInstant = fim != null ? fim.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC) : null;

        long total = solicitacaoRepo.countByPeriodo(inicioInstant, fimInstant);
        long concluidas = solicitacaoRepo.countConcluidasByPeriodo(inicioInstant, fimInstant);
        long erros = solicitacaoRepo.countErrosByPeriodo(inicioInstant, fimInstant);

        return new MetricasAssistente(total, concluidas, erros);
    }
}
