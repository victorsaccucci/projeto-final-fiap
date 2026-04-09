package com.sussmartassistant.prontuario.infrastructure;

import com.sussmartassistant.prontuario.domain.Prontuario;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "prontuarios")
public class ProntuarioEntity {

    @Id
    private UUID id;

    @Column(name = "paciente_id", nullable = false, unique = true)
    private UUID pacienteId;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private Instant atualizadoEm;

    protected ProntuarioEntity() {}

    public static ProntuarioEntity fromDomain(Prontuario p) {
        ProntuarioEntity e = new ProntuarioEntity();
        e.id = p.getId();
        e.pacienteId = p.getPacienteId();
        e.criadoEm = p.getCriadoEm();
        e.atualizadoEm = p.getAtualizadoEm();
        return e;
    }

    public Prontuario toDomain() {
        return new Prontuario(id, pacienteId, criadoEm, atualizadoEm);
    }

    public UUID getId() { return id; }
    public UUID getPacienteId() { return pacienteId; }
}
