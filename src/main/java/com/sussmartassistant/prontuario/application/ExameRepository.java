package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.shared.domain.PageResult;

import java.time.LocalDate;
import java.util.UUID;

public interface ExameRepository {
    Exame salvar(Exame exame);
    PageResult<Exame> buscarPorPacienteId(UUID pacienteId, int page, int size);
    PageResult<Exame> buscarPorPacienteIdETipo(UUID pacienteId, String tipo, int page, int size);
    PageResult<Exame> buscarPorPacienteIdEPeriodo(UUID pacienteId, LocalDate inicio, LocalDate fim, int page, int size);
}
