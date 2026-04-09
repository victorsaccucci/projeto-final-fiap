package com.sussmartassistant.prontuario.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidade de domínio Exame — POJO puro, sem dependências de framework.
 */
public class Exame {

    private UUID id;
    private UUID prontuarioId;
    private String tipo;
    private LocalDate dataRealizacao;
    private String resultado;
    private UUID unidadeSaudeOrigemId;
    private Instant registradoEm;
    private UUID registradoPorId;

    public Exame(UUID id, UUID prontuarioId, String tipo, LocalDate dataRealizacao, String resultado,
                 UUID unidadeSaudeOrigemId, Instant registradoEm, UUID registradoPorId) {
        this.id = id;
        this.prontuarioId = prontuarioId;
        this.tipo = tipo;
        this.dataRealizacao = dataRealizacao;
        this.resultado = resultado;
        this.unidadeSaudeOrigemId = unidadeSaudeOrigemId;
        this.registradoEm = registradoEm;
        this.registradoPorId = registradoPorId;
    }

    public static Exame criar(UUID prontuarioId, String tipo, LocalDate dataRealizacao, String resultado,
                               UUID unidadeSaudeOrigemId, UUID registradoPorId) {
        return new Exame(UUID.randomUUID(), prontuarioId, tipo, dataRealizacao, resultado,
                unidadeSaudeOrigemId, Instant.now(), registradoPorId);
    }

    public UUID getId() { return id; }
    public UUID getProntuarioId() { return prontuarioId; }
    public String getTipo() { return tipo; }
    public LocalDate getDataRealizacao() { return dataRealizacao; }
    public String getResultado() { return resultado; }
    public UUID getUnidadeSaudeOrigemId() { return unidadeSaudeOrigemId; }
    public Instant getRegistradoEm() { return registradoEm; }
    public UUID getRegistradoPorId() { return registradoPorId; }
}
