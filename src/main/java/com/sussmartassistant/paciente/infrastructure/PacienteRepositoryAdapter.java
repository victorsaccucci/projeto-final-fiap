package com.sussmartassistant.paciente.infrastructure;

import com.sussmartassistant.paciente.application.PacienteRepository;
import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.shared.domain.CPF;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PacienteRepositoryAdapter implements PacienteRepository {

    private final JpaPacienteRepository jpa;

    public PacienteRepositoryAdapter(JpaPacienteRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    @CachePut(value = "paciente", key = "#result.id()", condition = "#result != null")
    public Paciente salvar(Paciente paciente) {
        PacienteEntity entity = PacienteEntity.fromDomain(paciente);
        return jpa.save(entity).toDomain();
    }

    @Override
    public Optional<Paciente> buscarPorCpf(CPF cpf) {
        return jpa.findByCpf(cpf.valor()).map(PacienteEntity::toDomain);
    }

    @Override
    @Cacheable(value = "paciente", key = "#id")
    public Optional<Paciente> buscarPorId(UUID id) {
        return jpa.findById(id).map(PacienteEntity::toDomain);
    }

    @Override
    public boolean existePorCpf(CPF cpf) {
        return jpa.existsByCpf(cpf.valor());
    }
}
