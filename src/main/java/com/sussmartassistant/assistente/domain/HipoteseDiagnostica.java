package com.sussmartassistant.assistente.domain;

import com.sussmartassistant.shared.domain.NivelConfianca;

import java.util.UUID;

/**
 * Entidade de domínio HipoteseDiagnostica — POJO puro, sem dependências de framework.
 */
public class HipoteseDiagnostica {

    private UUID id;
    private UUID solicitacaoId;
    private String codigoCid;
    private String justificativa;
    private NivelConfianca confianca;
    private boolean reincidencia;
    private String alertaContraindicacao;
    private int ordem;

    public HipoteseDiagnostica(UUID id, UUID solicitacaoId, String codigoCid, String justificativa,
                                NivelConfianca confianca, boolean reincidencia,
                                String alertaContraindicacao, int ordem) {
        this.id = id;
        this.solicitacaoId = solicitacaoId;
        this.codigoCid = codigoCid;
        this.justificativa = justificativa;
        this.confianca = confianca;
        this.reincidencia = reincidencia;
        this.alertaContraindicacao = alertaContraindicacao;
        this.ordem = ordem;
    }

    public static HipoteseDiagnostica criar(UUID solicitacaoId, String codigoCid, String justificativa,
                                             NivelConfianca confianca, boolean reincidencia,
                                             String alertaContraindicacao, int ordem) {
        return new HipoteseDiagnostica(UUID.randomUUID(), solicitacaoId, codigoCid, justificativa,
                confianca, reincidencia, alertaContraindicacao, ordem);
    }

    public UUID getId() { return id; }
    public UUID getSolicitacaoId() { return solicitacaoId; }
    public String getCodigoCid() { return codigoCid; }
    public String getJustificativa() { return justificativa; }
    public NivelConfianca getConfianca() { return confianca; }
    public boolean isReincidencia() { return reincidencia; }
    public String getAlertaContraindicacao() { return alertaContraindicacao; }
    public int getOrdem() { return ordem; }

    public void setReincidencia(boolean reincidencia) { this.reincidencia = reincidencia; }
    public void setAlertaContraindicacao(String alertaContraindicacao) { this.alertaContraindicacao = alertaContraindicacao; }
}
