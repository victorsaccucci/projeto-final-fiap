package com.sussmartassistant.paciente.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaAlergiaRepository extends JpaRepository<AlergiaEntity, UUID> {
    List<AlergiaEntity> findByPacienteId(UUID pacienteId);
    boolean existsByPacienteIdAndSubstanciaIgnoreCase(UUID pacienteId, String substancia);
}
