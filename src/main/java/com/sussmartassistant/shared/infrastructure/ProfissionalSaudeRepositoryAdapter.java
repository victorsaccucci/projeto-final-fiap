package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.application.ProfissionalSaudeRepository;
import com.sussmartassistant.shared.domain.ProfissionalSaude;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProfissionalSaudeRepositoryAdapter implements ProfissionalSaudeRepository {

    private final JpaProfissionalSaudeRepository jpa;

    public ProfissionalSaudeRepositoryAdapter(JpaProfissionalSaudeRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public ProfissionalSaude salvar(ProfissionalSaude profissional) {
        ProfissionalSaudeEntity entity = ProfissionalSaudeEntity.fromDomain(profissional);
        return jpa.save(entity).toDomain();
    }

    @Override
    public Optional<ProfissionalSaude> buscarPorRegistro(String registroProfissional) {
        return jpa.findByRegistroProfissional(registroProfissional).map(ProfissionalSaudeEntity::toDomain);
    }

    @Override
    public boolean existePorRegistro(String registroProfissional) {
        return jpa.existsByRegistroProfissional(registroProfissional);
    }
}
