package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.MedicamentoEmUso;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicamentoRepository {
    MedicamentoEmUso salvar(MedicamentoEmUso medicamento);
    Optional<MedicamentoEmUso> buscarPorId(UUID id);
    List<MedicamentoEmUso> buscarAtivosPorPacienteId(UUID pacienteId);
    boolean existeAtivoPorPacienteNomeDosagem(UUID pacienteId, String nome, String dosagem);
}
