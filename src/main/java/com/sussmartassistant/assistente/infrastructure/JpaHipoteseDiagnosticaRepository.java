package com.sussmartassistant.assistente.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaHipoteseDiagnosticaRepository extends JpaRepository<HipoteseDiagnosticaEntity, UUID> {
    List<HipoteseDiagnosticaEntity> findBySolicitacaoIdOrderByOrdemAsc(UUID solicitacaoId);
}
