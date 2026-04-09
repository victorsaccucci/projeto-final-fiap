package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.shared.domain.CPF;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

public class BuscarPacientePorCpfUseCase {

    private final PacienteRepository pacienteRepository;

    public BuscarPacientePorCpfUseCase(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente executar(CPF cpf) {
        return pacienteRepository.buscarPorCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente com CPF " + cpf.valor() + " não encontrado"));
    }
}
