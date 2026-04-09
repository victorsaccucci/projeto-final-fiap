package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.shared.domain.CNS;
import com.sussmartassistant.shared.domain.CPF;
import com.sussmartassistant.shared.domain.DomainException;

import java.time.LocalDate;

public class CadastrarPacienteUseCase {

    private final PacienteRepository pacienteRepository;
    private final PacienteCadastradoCallback callback;

    public CadastrarPacienteUseCase(PacienteRepository pacienteRepository,
                                     PacienteCadastradoCallback callback) {
        this.pacienteRepository = pacienteRepository;
        this.callback = callback;
    }

    public Paciente executar(String nome, CPF cpf, CNS cns, LocalDate dataNascimento,
                             String sexo, String contato) {
        if (pacienteRepository.existePorCpf(cpf)) {
            throw new DomainException("Paciente com CPF " + cpf.valor() + " já está cadastrado no sistema");
        }
        Paciente paciente = Paciente.criar(nome, cpf, cns, dataNascimento, sexo, contato);
        Paciente salvo = pacienteRepository.salvar(paciente);

        callback.onPacienteCadastrado(salvo.getId());

        return salvo;
    }
}
