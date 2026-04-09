package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.shared.domain.DomainException;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.UUID;

public class RegistrarMedicamentoUseCase {

    private final MedicamentoRepository medicamentoRepository;
    private final PacienteRepository pacienteRepository;

    public RegistrarMedicamentoUseCase(MedicamentoRepository medicamentoRepository,
                                       PacienteRepository pacienteRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public MedicamentoEmUso executar(UUID pacienteId, String nome, String dosagem, String frequencia,
                                     LocalDate dataInicio, UUID registradoPorId) {
        pacienteRepository.buscarPorId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado: " + pacienteId));

        if (medicamentoRepository.existeAtivoPorPacienteNomeDosagem(pacienteId, nome, dosagem)) {
            throw new DomainException("Medicamento '" + nome + "' com dosagem '" + dosagem
                    + "' já está ativo para este paciente");
        }

        MedicamentoEmUso medicamento = MedicamentoEmUso.criar(pacienteId, nome, dosagem, frequencia,
                dataInicio, registradoPorId);
        return medicamentoRepository.salvar(medicamento);
    }
}
