package com.sussmartassistant.shared.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUnidadeSaudeRepository extends JpaRepository<UnidadeSaudeEntity, UUID> {
    Optional<UnidadeSaudeEntity> findByCnes(String cnes);
    boolean existsByCnes(String cnes);
}
