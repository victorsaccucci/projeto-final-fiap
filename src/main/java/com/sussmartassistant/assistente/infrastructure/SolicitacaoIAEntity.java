package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.domain.SolicitacaoIA;
import com.sussmartassistant.shared.domain.StatusSolicitacaoIA;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "solicitacoes_ia")
public class SolicitacaoIAEntity {

    @Id
    private UUID id;

    @Column(name = "paciente_id", nullable = false)
    private UUID pacienteId;

    @Column(name = "profissional_id", nullable = false)
    private UUID profissionalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacaoIA status;

    @Column(columnDefinition = "TEXT")
    private String sintomasInformados;

    @Column(columnDefinition = "TEXT")
    private String promptEnviado;

    @Column(columnDefinition = "TEXT")
    private String respostaLlm;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    private Instant concluidoEm;

    protected SolicitacaoIAEntity() {}

    public static SolicitacaoIAEntity fromDomain(SolicitacaoIA s) {
        SolicitacaoIAEntity e = new SolicitacaoIAEntity();
        e.id = s.getId();
        e.pacienteId = s.getPacienteId();
        e.profissionalId = s.getProfissionalId();
        e.status = s.getStatus();
        e.sintomasInformados = s.getSintomasInformados();
        e.promptEnviado = s.getPromptEnviado();
        e.respostaLlm = s.getRespostaLlm();
        e.criadoEm = s.getCriadoEm();
        e.concluidoEm = s.getConcluidoEm();
        return e;
    }

    public SolicitacaoIA toDomain() {
        return new SolicitacaoIA(id, pacienteId, profissionalId, status,
                sintomasInformados, promptEnviado, respostaLlm, criadoEm, concluidoEm);
    }

    public UUID getId() { return id; }
    public StatusSolicitacaoIA getStatus() { return status; }
}
