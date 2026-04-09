package com.sussmartassistant.seguranca.infrastructure;

import com.sussmartassistant.seguranca.domain.EventoAuditoria;
import com.sussmartassistant.shared.domain.TipoOperacaoAuditoria;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "eventos_auditoria")
public class EventoAuditoriaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOperacaoAuditoria tipoOperacao;

    private UUID prontuarioId;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(columnDefinition = "TEXT")
    private String detalhes;

    private String correlationId;

    public EventoAuditoriaEntity() {}

    public static EventoAuditoriaEntity fromDomain(EventoAuditoria e) {
        EventoAuditoriaEntity entity = new EventoAuditoriaEntity();
        entity.id = e.id();
        entity.usuarioId = e.usuarioId();
        entity.tipoOperacao = e.tipoOperacao();
        entity.prontuarioId = e.prontuarioId();
        entity.dataHora = e.dataHora();
        entity.detalhes = e.detalhes();
        entity.correlationId = e.correlationId();
        return entity;
    }

    public EventoAuditoria toDomain() {
        return new EventoAuditoria(id, usuarioId, tipoOperacao, prontuarioId, dataHora, detalhes, correlationId);
    }
}
