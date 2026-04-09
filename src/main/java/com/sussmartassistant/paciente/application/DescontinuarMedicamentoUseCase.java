package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.shared.domain.DomainException;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.util.UUID;

public class DescontinuarMedicamentoUseCase {

    private final MedicamentoRepository medicamentoRepository;

    public DescontinuarMedicamentoUseCase(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public MedicamentoEmUso executar(UUID medicamentoId) {
        MedicamentoEmUso medicamento = medicamentoRepository.buscarPorId(medicamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado: " + medicamentoId));

        if (!medicamento.isAtivo()) {
            throw new DomainException("Medicamento já está descontinuado");
        }

        medicamento.descontinuar();
        return medicamentoRepository.salvar(medicamento);
    }
}
