package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.shared.domain.CPF;

import java.util.Optional;
import java.util.UUID;

public interface PacienteRepository {
    Paciente salvar(Paciente paciente);
    Optional<Paciente> buscarPorCpf(CPF cpf);
    Optional<Paciente> buscarPorId(UUID id);
    boolean existePorCpf(CPF cpf);
}
