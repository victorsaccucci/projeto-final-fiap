package com.sussmartassistant.paciente.infrastructure;

import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "medicamentos_em_uso")
public class MedicamentoEmUsoEntity {

    @Id
    private UUID id;

    @Column(name = "paciente_id", nullable = false)
    private UUID pacienteId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String dosagem;

    @Column(nullable = false)
    private String frequencia;

    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataDescontinuacao;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false, updatable = false)
    private Instant registradoEm;

    private UUID registradoPorId;

    protected MedicamentoEmUsoEntity() {}

    public static MedicamentoEmUsoEntity fromDomain(MedicamentoEmUso m) {
        MedicamentoEmUsoEntity e = new MedicamentoEmUsoEntity();
        e.id = m.getId();
        e.pacienteId = m.getPacienteId();
        e.nome = m.getNome();
        e.dosagem = m.getDosagem();
        e.frequencia = m.getFrequencia();
        e.dataInicio = m.getDataInicio();
        e.dataDescontinuacao = m.getDataDescontinuacao();
        e.ativo = m.isAtivo();
        e.registradoEm = m.getRegistradoEm();
        e.registradoPorId = m.getRegistradoPorId();
        return e;
    }

    public MedicamentoEmUso toDomain() {
        return new MedicamentoEmUso(id, pacienteId, nome, dosagem, frequencia,
                dataInicio, dataDescontinuacao, ativo, registradoEm, registradoPorId);
    }
}
