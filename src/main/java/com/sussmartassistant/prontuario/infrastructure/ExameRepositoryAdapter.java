package com.sussmartassistant.prontuario.infrastructure;

import com.sussmartassistant.prontuario.application.ExameRepository;
import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.shared.domain.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public class ExameRepositoryAdapter implements ExameRepository {

    private final JpaExameRepository jpa;

    public ExameRepositoryAdapter(JpaExameRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Exame salvar(Exame exame) {
        ExameEntity entity = ExameEntity.fromDomain(exame);
        return jpa.save(entity).toDomain();
    }

    @Override
    public PageResult<Exame> buscarPorPacienteId(UUID pacienteId, int page, int size) {
        return toPageResult(jpa.findByPacienteId(pacienteId, pageable(page, size)));
    }

    @Override
    public PageResult<Exame> buscarPorPacienteIdETipo(UUID pacienteId, String tipo, int page, int size) {
        return toPageResult(jpa.findByPacienteIdAndTipo(pacienteId, tipo, pageable(page, size)));
    }

    @Override
    public PageResult<Exame> buscarPorPacienteIdEPeriodo(UUID pacienteId, LocalDate inicio,
                                                           LocalDate fim, int page, int size) {
        return toPageResult(jpa.findByPacienteIdAndDataRealizacaoBetween(pacienteId, inicio, fim, pageable(page, size)));
    }

    private PageRequest pageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataRealizacao"));
    }

    private PageResult<Exame> toPageResult(Page<ExameEntity> springPage) {
        return PageResult.of(
                springPage.getContent().stream().map(ExameEntity::toDomain).toList(),
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements());
    }
}
