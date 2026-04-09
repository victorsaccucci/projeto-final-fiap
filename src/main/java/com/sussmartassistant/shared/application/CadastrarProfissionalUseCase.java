package com.sussmartassistant.shared.application;

import com.sussmartassistant.shared.domain.DomainException;
import com.sussmartassistant.shared.domain.ProfissionalSaude;

import java.util.UUID;

public class CadastrarProfissionalUseCase {

    private final ProfissionalSaudeRepository profissionalSaudeRepository;

    public CadastrarProfissionalUseCase(ProfissionalSaudeRepository profissionalSaudeRepository) {
        this.profissionalSaudeRepository = profissionalSaudeRepository;
    }

    public ProfissionalSaude executar(String nome, String registroProfissional,
                                       String especialidade, UUID unidadeSaudeId) {
        if (profissionalSaudeRepository.existePorRegistro(registroProfissional)) {
            throw new DomainException("Profissional com registro " + registroProfissional + " já está cadastrado no sistema");
        }
        ProfissionalSaude profissional = ProfissionalSaude.criar(nome, registroProfissional, especialidade, unidadeSaudeId);
        return profissionalSaudeRepository.salvar(profissional);
    }
}
