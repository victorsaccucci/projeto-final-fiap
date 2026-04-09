package com.sussmartassistant.shared.application;

import com.sussmartassistant.shared.domain.ProfissionalSaude;

import java.util.Optional;

public interface ProfissionalSaudeRepository {
    ProfissionalSaude salvar(ProfissionalSaude profissional);
    Optional<ProfissionalSaude> buscarPorRegistro(String registroProfissional);
    boolean existePorRegistro(String registroProfissional);
}
