package com.sussmartassistant.metricas.infrastructure;

import com.sussmartassistant.prontuario.infrastructure.RegistroAtendimentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface JpaMetricasAtendimentoRepository extends JpaRepository<RegistroAtendimentoEntity, UUID> {

    @Query("SELECT COUNT(a) FROM RegistroAtendimentoEntity a " +
           "WHERE (:inicio IS NULL OR a.data >= :inicio) " +
           "AND (:fim IS NULL OR a.data <= :fim) " +
           "AND (:unidadeId IS NULL OR a.unidadeSaudeId = :unidadeId)")
    long countByPeriodoAndUnidade(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            @Param("unidadeId") UUID unidadeId);

    @Query("SELECT a.diagnosticoCid, COUNT(a) FROM RegistroAtendimentoEntity a " +
           "WHERE a.diagnosticoCid IS NOT NULL " +
           "AND (:inicio IS NULL OR a.data >= :inicio) " +
           "AND (:fim IS NULL OR a.data <= :fim) " +
           "AND (:unidadeId IS NULL OR a.unidadeSaudeId = :unidadeId) " +
           "GROUP BY a.diagnosticoCid " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> findDiagnosticosFrequentes(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            @Param("unidadeId") UUID unidadeId);
}
