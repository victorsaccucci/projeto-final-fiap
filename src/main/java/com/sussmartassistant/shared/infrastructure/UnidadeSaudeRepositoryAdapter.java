package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.application.UnidadeSaudeRepository;
import com.sussmartassistant.shared.domain.UnidadeSaude;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UnidadeSaudeRepositoryAdapter implements UnidadeSaudeRepository {

    private final JpaUnidadeSaudeRepository jpa;

    public UnidadeSaudeRepositoryAdapter(JpaUnidadeSaudeRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public UnidadeSaude salvar(UnidadeSaude unidade) {
        UnidadeSaudeEntity entity = UnidadeSaudeEntity.fromDomain(unidade);
        return jpa.save(entity).toDomain();
    }

    @Override
    public Optional<UnidadeSaude> buscarPorCnes(String cnes) {
        return jpa.findByCnes(cnes).map(UnidadeSaudeEntity::toDomain);
    }

    @Override
    public List<UnidadeSaude> listarTodas() {
        return jpa.findAll().stream().map(UnidadeSaudeEntity::toDomain).toList();
    }

    @Override
    public boolean existePorCnes(String cnes) {
        return jpa.existsByCnes(cnes);
    }
}
