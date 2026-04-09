package com.sussmartassistant.assistente.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaSolicitacaoIARepository extends JpaRepository<SolicitacaoIAEntity, UUID> {
}
