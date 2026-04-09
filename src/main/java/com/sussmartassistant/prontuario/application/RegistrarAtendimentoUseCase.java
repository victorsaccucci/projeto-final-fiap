package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.prontuario.domain.Prontuario;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.UUID;

public class RegistrarAtendimentoUseCase {

    private final ProntuarioRepository prontuarioRepository;
    private final AtendimentoRepository atendimentoRepository;

    public RegistrarAtendimentoUseCase(ProntuarioRepository prontuarioRepository,
                                        AtendimentoRepository atendimentoRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    public RegistroAtendimento executar(UUID pacienteId, UUID profissionalId, UUID unidadeSaudeId,
                                         LocalDate data, String queixaPrincipal, String anamnese,
                                         String diagnosticoCid, String prescricoes, String observacoes) {
        Prontuario prontuario = prontuarioRepository.buscarPorPacienteId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado para paciente: " + pacienteId));

        RegistroAtendimento atendimento = RegistroAtendimento.criar(
                prontuario.getId(), profissionalId, unidadeSaudeId,
                data, queixaPrincipal, anamnese, diagnosticoCid, prescricoes, observacoes);

        return atendimentoRepository.salvar(atendimento);
    }
}
