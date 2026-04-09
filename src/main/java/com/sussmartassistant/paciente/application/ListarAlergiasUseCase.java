package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.shared.domain.GravidadeAlergia;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ListarAlergiasUseCase {

    private static final Map<GravidadeAlergia, Integer> ORDEM_GRAVIDADE = Map.of(
            GravidadeAlergia.CRITICA, 0,
            GravidadeAlergia.MODERADA, 1,
            GravidadeAlergia.LEVE, 2
    );

    private final AlergiaRepository alergiaRepository;
    private final PacienteRepository pacienteRepository;

    public ListarAlergiasUseCase(AlergiaRepository alergiaRepository, PacienteRepository pacienteRepository) {
        this.alergiaRepository = alergiaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<Alergia> executar(UUID pacienteId) {
        pacienteRepository.buscarPorId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado: " + pacienteId));

        return alergiaRepository.buscarPorPacienteId(pacienteId).stream()
                .sorted(Comparator.comparingInt(a -> ORDEM_GRAVIDADE.getOrDefault(a.getGravidade(), 99)))
                .toList();
    }
}
