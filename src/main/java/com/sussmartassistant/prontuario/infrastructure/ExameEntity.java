package com.sussmartassistant.prontuario.infrastructure;

import com.sussmartassistant.prontuario.domain.Exame;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "exames")
public class ExameEntity {

    @Id
    private UUID id;

    @Column(name = "prontuario_id", nullable = false)
    private UUID prontuarioId;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private LocalDate dataRealizacao;

    @Column(columnDefinition = "TEXT")
    private String resultado;

    @Column(name = "unidade_saude_origem_id")
    private UUID unidadeSaudeOrigemId;

    @Column(nullable = false, updatable = false)
    private Instant registradoEm;

    @Column(name = "registrado_por_id")
    private UUID registradoPorId;

    protected ExameEntity() {}

    public static ExameEntity fromDomain(Exame ex) {
        ExameEntity e = new ExameEntity();
        e.id = ex.getId();
        e.prontuarioId = ex.getProntuarioId();
        e.tipo = ex.getTipo();
        e.dataRealizacao = ex.getDataRealizacao();
        e.resultado = ex.getResultado();
        e.unidadeSaudeOrigemId = ex.getUnidadeSaudeOrigemId();
        e.registradoEm = ex.getRegistradoEm();
        e.registradoPorId = ex.getRegistradoPorId();
        return e;
    }

    public Exame toDomain() {
        return new Exame(id, prontuarioId, tipo, dataRealizacao, resultado,
                unidadeSaudeOrigemId, registradoEm, registradoPorId);
    }

    public UUID getId() { return id; }
    public UUID getProntuarioId() { return prontuarioId; }
    public String getTipo() { return tipo; }
    public LocalDate getDataRealizacao() { return dataRealizacao; }
}
