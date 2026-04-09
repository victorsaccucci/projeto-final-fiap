package com.sussmartassistant.assistente.domain;

import com.sussmartassistant.shared.domain.StatusSolicitacaoIA;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidade de domínio SolicitacaoIA — POJO puro, sem dependências de framework.
 */
public class SolicitacaoIA {

    private UUID id;
    private UUID pacienteId;
    private UUID profissionalId;
    private StatusSolicitacaoIA status;
    private String sintomasInformados;
    private String promptEnviado;
    private String respostaLlm;
    private Instant criadoEm;
    private Instant concluidoEm;
    private List<HipoteseDiagnostica> hipoteses;

    public SolicitacaoIA(UUID id, UUID pacienteId, UUID profissionalId, StatusSolicitacaoIA status,
                         String sintomasInformados, String promptEnviado, String respostaLlm,
                         Instant criadoEm, Instant concluidoEm) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.profissionalId = profissionalId;
        this.status = status;
        this.sintomasInformados = sintomasInformados;
        this.promptEnviado = promptEnviado;
        this.respostaLlm = respostaLlm;
        this.criadoEm = criadoEm;
        this.concluidoEm = concluidoEm;
        this.hipoteses = new ArrayList<>();
    }

    public static SolicitacaoIA criar(UUID pacienteId, UUID profissionalId, String sintomasInformados) {
        return new SolicitacaoIA(UUID.randomUUID(), pacienteId, profissionalId,
                StatusSolicitacaoIA.PENDENTE, sintomasInformados, null, null, Instant.now(), null);
    }

    public void iniciarProcessamento() {
        this.status = StatusSolicitacaoIA.PROCESSANDO;
    }

    public void concluir(String promptEnviado, String respostaLlm, List<HipoteseDiagnostica> hipoteses) {
        this.status = StatusSolicitacaoIA.CONCLUIDA;
        this.promptEnviado = promptEnviado;
        this.respostaLlm = respostaLlm;
        this.concluidoEm = Instant.now();
        this.hipoteses = hipoteses != null ? hipoteses : new ArrayList<>();
    }

    public void marcarErro(String mensagemErro) {
        this.status = StatusSolicitacaoIA.ERRO;
        this.respostaLlm = mensagemErro;
        this.concluidoEm = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getPacienteId() { return pacienteId; }
    public UUID getProfissionalId() { return profissionalId; }
    public StatusSolicitacaoIA getStatus() { return status; }
    public String getSintomasInformados() { return sintomasInformados; }
    public String getPromptEnviado() { return promptEnviado; }
    public String getRespostaLlm() { return respostaLlm; }
    public Instant getCriadoEm() { return criadoEm; }
    public Instant getConcluidoEm() { return concluidoEm; }
    public List<HipoteseDiagnostica> getHipoteses() { return hipoteses; }
    public void setHipoteses(List<HipoteseDiagnostica> hipoteses) { this.hipoteses = hipoteses; }
}
