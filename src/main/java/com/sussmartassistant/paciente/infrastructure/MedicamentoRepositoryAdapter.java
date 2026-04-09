package com.sussmartassistant.paciente.infrastructure;

import com.sussmartassistant.paciente.application.MedicamentoRepository;
import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MedicamentoRepositoryAdapter implements MedicamentoRepository {

    private final JpaMedicamentoRepository jpa;

    public MedicamentoRepositoryAdapter(JpaMedicamentoRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    @CacheEvict(value = "medicamentos", key = "#medicamento.pacienteId")
    public MedicamentoEmUso salvar(MedicamentoEmUso medicamento) {
        MedicamentoEmUsoEntity entity = MedicamentoEmUsoEntity.fromDomain(medicamento);
        return jpa.save(entity).toDomain();
    }

    @Override
    public Optional<MedicamentoEmUso> buscarPorId(UUID id) {
        return jpa.findById(id).map(MedicamentoEmUsoEntity::toDomain);
    }

    @Override
    @Cacheable(value = "medicamentos", key = "#pacienteId")
    public List<MedicamentoEmUso> buscarAtivosPorPacienteId(UUID pacienteId) {
        return jpa.findByPacienteIdAndAtivoTrue(pacienteId).stream()
                .map(MedicamentoEmUsoEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existeAtivoPorPacienteNomeDosagem(UUID pacienteId, String nome, String dosagem) {
        return jpa.existsByPacienteIdAndNomeIgnoreCaseAndDosagemIgnoreCaseAndAtivoTrue(pacienteId, nome, dosagem);
    }
}
