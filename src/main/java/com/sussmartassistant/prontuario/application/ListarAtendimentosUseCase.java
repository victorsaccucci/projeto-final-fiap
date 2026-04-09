package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.shared.domain.PageResult;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.UUID;

public class ListarAtendimentosUseCase {

    private final ProntuarioRepository prontuarioRepository;
    private final AtendimentoRepository atendimentoRepository;

    public ListarAtendimentosUseCase(ProntuarioRepository prontuarioRepository,
                                      AtendimentoRepository atendimentoRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    public PageResult<RegistroAtendimento> executar(UUID pacienteId, LocalDate inicio, LocalDate fim,
                                                     String cid, int page, int size) {
        prontuarioRepository.buscarPorPacienteId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado para paciente: " + pacienteId));

        if (cid != null && !cid.isBlank()) {
            return atendimentoRepository.buscarPorPacienteIdECid(pacienteId, cid, page, size);
        }
        if (inicio != null && fim != null) {
            return atendimentoRepository.buscarPorPacienteIdEPeriodo(pacienteId, inicio, fim, page, size);
        }
        return atendimentoRepository.buscarPorPacienteId(pacienteId, page, size);
    }
}
