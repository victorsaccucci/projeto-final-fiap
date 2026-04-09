package com.sussmartassistant.metricas.infrastructure;

import com.sussmartassistant.assistente.infrastructure.SolicitacaoIAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface JpaMetricasSolicitacaoRepository extends JpaRepository<SolicitacaoIAEntity, UUID> {

    @Query("SELECT COUNT(s) FROM SolicitacaoIAEntity s " +
           "WHERE (:inicio IS NULL OR s.criadoEm >= :inicio) " +
           "AND (:fim IS NULL OR s.criadoEm <= :fim)")
    long countByPeriodo(@Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query("SELECT COUNT(s) FROM SolicitacaoIAEntity s " +
           "WHERE s.status = com.sussmartassistant.shared.domain.StatusSolicitacaoIA.CONCLUIDA " +
           "AND (:inicio IS NULL OR s.criadoEm >= :inicio) " +
           "AND (:fim IS NULL OR s.criadoEm <= :fim)")
    long countConcluidasByPeriodo(@Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @Query("SELECT COUNT(s) FROM SolicitacaoIAEntity s " +
           "WHERE s.status = com.sussmartassistant.shared.domain.StatusSolicitacaoIA.ERRO " +
           "AND (:inicio IS NULL OR s.criadoEm >= :inicio) " +
           "AND (:fim IS NULL OR s.criadoEm <= :fim)")
    long countErrosByPeriodo(@Param("inicio") Instant inicio, @Param("fim") Instant fim);
}
