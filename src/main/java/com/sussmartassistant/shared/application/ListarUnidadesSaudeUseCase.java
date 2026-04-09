package com.sussmartassistant.shared.application;

import com.sussmartassistant.shared.domain.UnidadeSaude;

import java.util.List;

public class ListarUnidadesSaudeUseCase {

    private final UnidadeSaudeRepository unidadeSaudeRepository;

    public ListarUnidadesSaudeUseCase(UnidadeSaudeRepository unidadeSaudeRepository) {
        this.unidadeSaudeRepository = unidadeSaudeRepository;
    }

    public List<UnidadeSaude> executar() {
        return unidadeSaudeRepository.listarTodas();
    }
}
