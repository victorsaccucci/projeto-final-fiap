package com.sussmartassistant.prontuario.infrastructure;

import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "registros_atendimento")
public class RegistroAtendimentoEntity {

    @Id
    private UUID id;

    @Column(name = "prontuario_id", nullable = false)
    private UUID prontuarioId;

    @Column(name = "profissional_id", nullable = false)
    private UUID profissionalId;

    @Column(name = "unidade_saude_id")
    private UUID unidadeSaudeId;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private String queixaPrincipal;

    @Column(columnDefinition = "TEXT")
    private String anamnese;

    private String diagnosticoCid;

    @Column(columnDefinition = "TEXT")
    private String prescricoes;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    protected RegistroAtendimentoEntity() {}

    public static RegistroAtendimentoEntity fromDomain(RegistroAtendimento a) {
        RegistroAtendimentoEntity e = new RegistroAtendimentoEntity();
        e.id = a.getId();
        e.prontuarioId = a.getProntuarioId();
        e.profissionalId = a.getProfissionalId();
        e.unidadeSaudeId = a.getUnidadeSaudeId();
        e.data = a.getData();
        e.queixaPrincipal = a.getQueixaPrincipal();
        e.anamnese = a.getAnamnese();
        e.diagnosticoCid = a.getDiagnosticoCid();
        e.prescricoes = a.getPrescricoes();
        e.observacoes = a.getObservacoes();
        e.criadoEm = a.getCriadoEm();
        return e;
    }

    public RegistroAtendimento toDomain() {
        return new RegistroAtendimento(id, prontuarioId, profissionalId, unidadeSaudeId,
                data, queixaPrincipal, anamnese, diagnosticoCid, prescricoes, observacoes, criadoEm);
    }

    public UUID getId() { return id; }
    public UUID getProntuarioId() { return prontuarioId; }
    public LocalDate getData() { return data; }
    public String getDiagnosticoCid() { return diagnosticoCid; }
}
