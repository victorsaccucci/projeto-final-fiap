package com.sussmartassistant.prontuario.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaProntuarioRepository extends JpaRepository<ProntuarioEntity, UUID> {
    Optional<ProntuarioEntity> findByPacienteId(UUID pacienteId);
}
