package com.sussmartassistant.shared.application;

import com.sussmartassistant.shared.domain.UnidadeSaude;

import java.util.List;
import java.util.Optional;

public interface UnidadeSaudeRepository {
    UnidadeSaude salvar(UnidadeSaude unidade);
    Optional<UnidadeSaude> buscarPorCnes(String cnes);
    List<UnidadeSaude> listarTodas();
    boolean existePorCnes(String cnes);
}
