package com.sussmartassistant.paciente.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaPacienteRepository extends JpaRepository<PacienteEntity, UUID> {
    Optional<PacienteEntity> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
