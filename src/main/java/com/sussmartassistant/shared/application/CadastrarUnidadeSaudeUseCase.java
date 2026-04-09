package com.sussmartassistant.shared.application;

import com.sussmartassistant.shared.domain.DomainException;
import com.sussmartassistant.shared.domain.TipoUnidadeSaude;
import com.sussmartassistant.shared.domain.UnidadeSaude;

import java.util.List;

public class CadastrarUnidadeSaudeUseCase {

    private final UnidadeSaudeRepository unidadeSaudeRepository;

    public CadastrarUnidadeSaudeUseCase(UnidadeSaudeRepository unidadeSaudeRepository) {
        this.unidadeSaudeRepository = unidadeSaudeRepository;
    }

    public UnidadeSaude executar(String nome, String cnes, String endereco,
                                  TipoUnidadeSaude tipo, List<String> especialidadesDisponiveis) {
        if (unidadeSaudeRepository.existePorCnes(cnes)) {
            throw new DomainException("Unidade de Saúde com CNES " + cnes + " já está cadastrada no sistema");
        }
        UnidadeSaude unidade = UnidadeSaude.criar(nome, cnes, endereco, tipo, especialidadesDisponiveis);
        return unidadeSaudeRepository.salvar(unidade);
    }
}
