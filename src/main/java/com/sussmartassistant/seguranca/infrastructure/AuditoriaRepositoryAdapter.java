package com.sussmartassistant.seguranca.infrastructure;

import com.sussmartassistant.seguranca.application.AuditoriaRepository;
import com.sussmartassistant.seguranca.domain.EventoAuditoria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AuditoriaRepositoryAdapter implements AuditoriaRepository {

    private final JpaEventoAuditoriaRepository jpa;

    public AuditoriaRepositoryAdapter(JpaEventoAuditoriaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void registrar(EventoAuditoria evento) {
        jpa.save(EventoAuditoriaEntity.fromDomain(evento));
    }

    @Override
    public List<EventoAuditoria> buscarPorProntuarioId(UUID prontuarioId) {
        return jpa.findByProntuarioIdOrderByDataHoraDesc(prontuarioId).stream()
                .map(EventoAuditoriaEntity::toDomain)
                .toList();
    }
}
