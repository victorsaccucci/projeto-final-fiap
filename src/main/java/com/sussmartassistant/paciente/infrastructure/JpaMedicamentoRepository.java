package com.sussmartassistant.paciente.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaMedicamentoRepository extends JpaRepository<MedicamentoEmUsoEntity, UUID> {
    List<MedicamentoEmUsoEntity> findByPacienteIdAndAtivoTrue(UUID pacienteId);
    boolean existsByPacienteIdAndNomeIgnoreCaseAndDosagemIgnoreCaseAndAtivoTrue(
            UUID pacienteId, String nome, String dosagem);
}
