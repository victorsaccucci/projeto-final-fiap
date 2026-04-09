package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public class ListarMedicamentosAtivosUseCase {

    private final MedicamentoRepository medicamentoRepository;
    private final PacienteRepository pacienteRepository;

    public ListarMedicamentosAtivosUseCase(MedicamentoRepository medicamentoRepository,
                                            PacienteRepository pacienteRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<MedicamentoEmUso> executar(UUID pacienteId) {
        pacienteRepository.buscarPorId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado: " + pacienteId));

        return medicamentoRepository.buscarAtivosPorPacienteId(pacienteId);
    }
}
