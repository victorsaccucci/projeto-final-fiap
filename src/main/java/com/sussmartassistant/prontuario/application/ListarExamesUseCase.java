package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.shared.domain.PageResult;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.UUID;

public class ListarExamesUseCase {

    private final ProntuarioRepository prontuarioRepository;
    private final ExameRepository exameRepository;

    public ListarExamesUseCase(ProntuarioRepository prontuarioRepository,
                                ExameRepository exameRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.exameRepository = exameRepository;
    }

    public PageResult<Exame> executar(UUID pacienteId, String tipo, LocalDate inicio, LocalDate fim,
                                       int page, int size) {
        prontuarioRepository.buscarPorPacienteId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado para paciente: " + pacienteId));

        if (tipo != null && !tipo.isBlank()) {
            return exameRepository.buscarPorPacienteIdETipo(pacienteId, tipo, page, size);
        }
        if (inicio != null && fim != null) {
            return exameRepository.buscarPorPacienteIdEPeriodo(pacienteId, inicio, fim, page, size);
        }
        return exameRepository.buscarPorPacienteId(pacienteId, page, size);
    }
}
