package com.sussmartassistant.prontuario.infrastructure;

import com.sussmartassistant.prontuario.application.ProntuarioRepository;
import com.sussmartassistant.prontuario.domain.Prontuario;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProntuarioRepositoryAdapter implements ProntuarioRepository {

    private final JpaProntuarioRepository jpa;

    public ProntuarioRepositoryAdapter(JpaProntuarioRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Prontuario salvar(Prontuario prontuario) {
        ProntuarioEntity entity = ProntuarioEntity.fromDomain(prontuario);
        return jpa.save(entity).toDomain();
    }

    @Override
    @Cacheable(value = "prontuario", key = "#pacienteId")
    public Optional<Prontuario> buscarPorPacienteId(UUID pacienteId) {
        return jpa.findByPacienteId(pacienteId).map(ProntuarioEntity::toDomain);
    }
}
