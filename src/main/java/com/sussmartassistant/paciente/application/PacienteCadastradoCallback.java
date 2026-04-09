package com.sussmartassistant.paciente.application;

import java.util.UUID;

/**
 * Port para ações pós-cadastro de paciente (ex: criação de prontuário).
 * Desacopla o módulo Paciente do módulo Prontuário.
 */
public interface PacienteCadastradoCallback {
    void onPacienteCadastrado(UUID pacienteId);
}
