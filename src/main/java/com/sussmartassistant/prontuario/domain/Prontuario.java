package com.sussmartassistant.prontuario.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidade de domínio Prontuário — POJO puro, sem dependências de framework.
 */
public class Prontuario {

    private UUID id;
    private UUID pacienteId;
    private Instant criadoEm;
    private Instant atualizadoEm;

    public Prontuario(UUID id, UUID pacienteId, Instant criadoEm, Instant atualizadoEm) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Prontuario criar(UUID pacienteId) {
        Instant agora = Instant.now();
        return new Prontuario(UUID.randomUUID(), pacienteId, agora, agora);
    }

    public UUID getId() { return id; }
    public UUID getPacienteId() { return pacienteId; }
    public Instant getCriadoEm() { return criadoEm; }
    public Instant getAtualizadoEm() { return atualizadoEm; }
}
