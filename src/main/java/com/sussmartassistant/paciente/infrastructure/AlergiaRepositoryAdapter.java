package com.sussmartassistant.paciente.infrastructure;

import com.sussmartassistant.paciente.application.AlergiaRepository;
import com.sussmartassistant.paciente.domain.Alergia;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AlergiaRepositoryAdapter implements AlergiaRepository {

    private final JpaAlergiaRepository jpa;

    public AlergiaRepositoryAdapter(JpaAlergiaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    @CacheEvict(value = "alergias", key = "#alergia.pacienteId")
    public Alergia salvar(Alergia alergia) {
        AlergiaEntity entity = AlergiaEntity.fromDomain(alergia);
        return jpa.save(entity).toDomain();
    }

    @Override
    @Cacheable(value = "alergias", key = "#pacienteId")
    public List<Alergia> buscarPorPacienteId(UUID pacienteId) {
        return jpa.findByPacienteId(pacienteId).stream()
                .map(AlergiaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existePorPacienteESubstancia(UUID pacienteId, String substancia) {
        return jpa.existsByPacienteIdAndSubstanciaIgnoreCase(pacienteId, substancia);
    }
}
