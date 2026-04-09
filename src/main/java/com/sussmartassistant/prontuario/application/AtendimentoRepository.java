package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.shared.domain.PageResult;

import java.time.LocalDate;
import java.util.UUID;

public interface AtendimentoRepository {
    RegistroAtendimento salvar(RegistroAtendimento atendimento);
    PageResult<RegistroAtendimento> buscarPorPacienteId(UUID pacienteId, int page, int size);
    PageResult<RegistroAtendimento> buscarPorPacienteIdEPeriodo(UUID pacienteId, LocalDate inicio, LocalDate fim, int page, int size);
    PageResult<RegistroAtendimento> buscarPorPacienteIdECid(UUID pacienteId, String cid, int page, int size);
}
