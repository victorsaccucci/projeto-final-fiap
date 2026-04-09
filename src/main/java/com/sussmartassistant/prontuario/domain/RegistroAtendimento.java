package com.sussmartassistant.prontuario.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidade de domínio RegistroAtendimento — POJO puro, sem dependências de framework.
 */
public class RegistroAtendimento {

    private UUID id;
    private UUID prontuarioId;
    private UUID profissionalId;
    private UUID unidadeSaudeId;
    private LocalDate data;
    private String queixaPrincipal;
    private String anamnese;
    private String diagnosticoCid;
    private String prescricoes;
    private String observacoes;
    private Instant criadoEm;

    public RegistroAtendimento(UUID id, UUID prontuarioId, UUID profissionalId, UUID unidadeSaudeId,
                                LocalDate data, String queixaPrincipal, String anamnese,
                                String diagnosticoCid, String prescricoes, String observacoes,
                                Instant criadoEm) {
        this.id = id;
        this.prontuarioId = prontuarioId;
        this.profissionalId = profissionalId;
        this.unidadeSaudeId = unidadeSaudeId;
        this.data = data;
        this.queixaPrincipal = queixaPrincipal;
        this.anamnese = anamnese;
        this.diagnosticoCid = diagnosticoCid;
        this.prescricoes = prescricoes;
        this.observacoes = observacoes;
        this.criadoEm = criadoEm;
    }

    public static RegistroAtendimento criar(UUID prontuarioId, UUID profissionalId, UUID unidadeSaudeId,
                                             LocalDate data, String queixaPrincipal, String anamnese,
                                             String diagnosticoCid, String prescricoes, String observacoes) {
        return new RegistroAtendimento(UUID.randomUUID(), prontuarioId, profissionalId, unidadeSaudeId,
                data, queixaPrincipal, anamnese, diagnosticoCid, prescricoes, observacoes, Instant.now());
    }

    public UUID getId() { return id; }
    public UUID getProntuarioId() { return prontuarioId; }
    public UUID getProfissionalId() { return profissionalId; }
    public UUID getUnidadeSaudeId() { return unidadeSaudeId; }
    public LocalDate getData() { return data; }
    public String getQueixaPrincipal() { return queixaPrincipal; }
    public String getAnamnese() { return anamnese; }
    public String getDiagnosticoCid() { return diagnosticoCid; }
    public String getPrescricoes() { return prescricoes; }
    public String getObservacoes() { return observacoes; }
    public Instant getCriadoEm() { return criadoEm; }
}
