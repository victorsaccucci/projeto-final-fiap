package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.domain.HipoteseDiagnostica;
import com.sussmartassistant.shared.domain.NivelConfianca;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "hipoteses_diagnosticas")
public class HipoteseDiagnosticaEntity {

    @Id
    private UUID id;

    @Column(name = "solicitacao_id", nullable = false)
    private UUID solicitacaoId;

    @Column(nullable = false)
    private String codigoCid;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String justificativa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelConfianca confianca;

    @Column(nullable = false)
    private boolean reincidencia;

    @Column(columnDefinition = "TEXT")
    private String alertaContraindicacao;

    @Column(nullable = false)
    private int ordem;

    protected HipoteseDiagnosticaEntity() {}

    public static HipoteseDiagnosticaEntity fromDomain(HipoteseDiagnostica h) {
        HipoteseDiagnosticaEntity e = new HipoteseDiagnosticaEntity();
        e.id = h.getId();
        e.solicitacaoId = h.getSolicitacaoId();
        e.codigoCid = h.getCodigoCid();
        e.justificativa = h.getJustificativa();
        e.confianca = h.getConfianca();
        e.reincidencia = h.isReincidencia();
        e.alertaContraindicacao = h.getAlertaContraindicacao();
        e.ordem = h.getOrdem();
        return e;
    }

    public HipoteseDiagnostica toDomain() {
        return new HipoteseDiagnostica(id, solicitacaoId, codigoCid, justificativa,
                confianca, reincidencia, alertaContraindicacao, ordem);
    }

    public UUID getId() { return id; }
    public UUID getSolicitacaoId() { return solicitacaoId; }
}
