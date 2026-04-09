package com.sussmartassistant.prontuario.infrastructure;

import com.sussmartassistant.prontuario.application.AtendimentoRepository;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.shared.domain.PageResult;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public class AtendimentoRepositoryAdapter implements AtendimentoRepository {

    private final JpaAtendimentoRepository jpa;

    public AtendimentoRepositoryAdapter(JpaAtendimentoRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    @CacheEvict(value = "prontuario", allEntries = true)
    public RegistroAtendimento salvar(RegistroAtendimento atendimento) {
        RegistroAtendimentoEntity entity = RegistroAtendimentoEntity.fromDomain(atendimento);
        return jpa.save(entity).toDomain();
    }

    @Override
    public PageResult<RegistroAtendimento> buscarPorPacienteId(UUID pacienteId, int page, int size) {
        return toPageResult(jpa.findByPacienteId(pacienteId, pageable(page, size)));
    }

    @Override
    public PageResult<RegistroAtendimento> buscarPorPacienteIdEPeriodo(UUID pacienteId, LocalDate inicio,
                                                                        LocalDate fim, int page, int size) {
        return toPageResult(jpa.findByPacienteIdAndDataBetween(pacienteId, inicio, fim, pageable(page, size)));
    }

    @Override
    public PageResult<RegistroAtendimento> buscarPorPacienteIdECid(UUID pacienteId, String cid, int page, int size) {
        return toPageResult(jpa.findByPacienteIdAndDiagnosticoCid(pacienteId, cid, pageable(page, size)));
    }

    private PageRequest pageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
    }

    private PageResult<RegistroAtendimento> toPageResult(Page<RegistroAtendimentoEntity> springPage) {
        return PageResult.of(
                springPage.getContent().stream().map(RegistroAtendimentoEntity::toDomain).toList(),
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements());
    }
}
