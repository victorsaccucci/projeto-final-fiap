package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.shared.domain.DomainException;
import com.sussmartassistant.shared.domain.GravidadeAlergia;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.util.UUID;

public class RegistrarAlergiaUseCase {

    private final AlergiaRepository alergiaRepository;
    private final PacienteRepository pacienteRepository;

    public RegistrarAlergiaUseCase(AlergiaRepository alergiaRepository, PacienteRepository pacienteRepository) {
        this.alergiaRepository = alergiaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public Alergia executar(UUID pacienteId, String substancia, GravidadeAlergia gravidade,
                            String reacaoObservada, UUID registradoPorId) {
        pacienteRepository.buscarPorId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado: " + pacienteId));

        if (alergiaRepository.existePorPacienteESubstancia(pacienteId, substancia)) {
            throw new DomainException("Alergia à substância '" + substancia + "' já consta no prontuário do paciente");
        }

        Alergia alergia = Alergia.criar(pacienteId, substancia, gravidade, reacaoObservada, registradoPorId);
        return alergiaRepository.salvar(alergia);
    }
}
