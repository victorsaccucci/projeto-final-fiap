package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.prontuario.domain.Prontuario;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.UUID;

public class RegistrarExameUseCase {

    private final ProntuarioRepository prontuarioRepository;
    private final ExameRepository exameRepository;

    public RegistrarExameUseCase(ProntuarioRepository prontuarioRepository,
                                  ExameRepository exameRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.exameRepository = exameRepository;
    }

    public Exame executar(UUID pacienteId, String tipo, LocalDate dataRealizacao, String resultado,
                           UUID unidadeSaudeOrigemId, UUID registradoPorId) {
        Prontuario prontuario = prontuarioRepository.buscarPorPacienteId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado para paciente: " + pacienteId));

        Exame exame = Exame.criar(prontuario.getId(), tipo, dataRealizacao, resultado,
                unidadeSaudeOrigemId, registradoPorId);

        return exameRepository.salvar(exame);
    }
}
