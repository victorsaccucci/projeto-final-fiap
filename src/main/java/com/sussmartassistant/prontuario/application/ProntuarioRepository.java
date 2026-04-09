package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.prontuario.domain.Prontuario;

import java.util.Optional;
import java.util.UUID;

public interface ProntuarioRepository {
    Prontuario salvar(Prontuario prontuario);
    Optional<Prontuario> buscarPorPacienteId(UUID pacienteId);
}
