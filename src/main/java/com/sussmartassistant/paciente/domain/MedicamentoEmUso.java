package com.sussmartassistant.paciente.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidade de domínio MedicamentoEmUso — POJO puro, sem dependências de framework.
 */
public class MedicamentoEmUso {

    private UUID id;
    private UUID pacienteId;
    private String nome;
    private String dosagem;
    private String frequencia;
    private LocalDate dataInicio;
    private LocalDate dataDescontinuacao;
    private boolean ativo;
    private Instant registradoEm;
    private UUID registradoPorId;

    public MedicamentoEmUso(UUID id, UUID pacienteId, String nome, String dosagem, String frequencia,
                            LocalDate dataInicio, LocalDate dataDescontinuacao, boolean ativo,
                            Instant registradoEm, UUID registradoPorId) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.nome = nome;
        this.dosagem = dosagem;
        this.frequencia = frequencia;
        this.dataInicio = dataInicio;
        this.dataDescontinuacao = dataDescontinuacao;
        this.ativo = ativo;
        this.registradoEm = registradoEm;
        this.registradoPorId = registradoPorId;
    }

    public static MedicamentoEmUso criar(UUID pacienteId, String nome, String dosagem, String frequencia,
                                          LocalDate dataInicio, UUID registradoPorId) {
        return new MedicamentoEmUso(UUID.randomUUID(), pacienteId, nome, dosagem, frequencia,
                dataInicio, null, true, Instant.now(), registradoPorId);
    }

    public void descontinuar() {
        this.ativo = false;
        this.dataDescontinuacao = LocalDate.now();
    }

    public UUID getId() { return id; }
    public UUID getPacienteId() { return pacienteId; }
    public String getNome() { return nome; }
    public String getDosagem() { return dosagem; }
    public String getFrequencia() { return frequencia; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataDescontinuacao() { return dataDescontinuacao; }
    public boolean isAtivo() { return ativo; }
    public Instant getRegistradoEm() { return registradoEm; }
    public UUID getRegistradoPorId() { return registradoPorId; }
}
