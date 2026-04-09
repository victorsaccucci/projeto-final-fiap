package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.SolicitacaoIARepository;
import com.sussmartassistant.assistente.domain.SolicitacaoIA;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SolicitacaoIARepositoryAdapter implements SolicitacaoIARepository {

    private final JpaSolicitacaoIARepository jpaSolicitacaoRepository;
    private final JpaHipoteseDiagnosticaRepository jpaHipoteseRepository;

    public SolicitacaoIARepositoryAdapter(JpaSolicitacaoIARepository jpaSolicitacaoRepository,
                                           JpaHipoteseDiagnosticaRepository jpaHipoteseRepository) {
        this.jpaSolicitacaoRepository = jpaSolicitacaoRepository;
        this.jpaHipoteseRepository = jpaHipoteseRepository;
    }

    @Override
    public SolicitacaoIA salvar(SolicitacaoIA solicitacao) {
        // Delete existing hipoteses to avoid orphan issues on update
        jpaHipoteseRepository.deleteAll(
                jpaHipoteseRepository.findBySolicitacaoIdOrderByOrdemAsc(solicitacao.getId()));

        SolicitacaoIAEntity entity = SolicitacaoIAEntity.fromDomain(solicitacao);
        jpaSolicitacaoRepository.save(entity);

        // Save hipoteses separately
        if (solicitacao.getHipoteses() != null && !solicitacao.getHipoteses().isEmpty()) {
            var hipoteseEntities = solicitacao.getHipoteses().stream()
                    .map(HipoteseDiagnosticaEntity::fromDomain)
                    .toList();
            jpaHipoteseRepository.saveAll(hipoteseEntities);
        }

        return entity.toDomain();
    }

    @Override
    public Optional<SolicitacaoIA> buscarPorId(UUID id) {
        return jpaSolicitacaoRepository.findById(id)
                .map(entity -> {
                    SolicitacaoIA solicitacao = entity.toDomain();
                    var hipoteses = jpaHipoteseRepository.findBySolicitacaoIdOrderByOrdemAsc(id)
                            .stream().map(HipoteseDiagnosticaEntity::toDomain).toList();
                    solicitacao.setHipoteses(hipoteses);
                    return solicitacao;
                });
    }
}
