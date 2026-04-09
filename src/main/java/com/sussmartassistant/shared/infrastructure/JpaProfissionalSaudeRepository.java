package com.sussmartassistant.shared.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaProfissionalSaudeRepository extends JpaRepository<ProfissionalSaudeEntity, UUID> {
    Optional<ProfissionalSaudeEntity> findByRegistroProfissional(String registroProfissional);
    boolean existsByRegistroProfissional(String registroProfissional);
}
