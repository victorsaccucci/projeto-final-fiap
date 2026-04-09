package com.sussmartassistant.paciente.infrastructure;

import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.shared.domain.CNS;
import com.sussmartassistant.shared.domain.CPF;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pacientes")
public class PacienteEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String cns;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String sexo;

    private String contato;

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private Instant atualizadoEm;

    protected PacienteEntity() {}

    public static PacienteEntity fromDomain(Paciente p) {
        PacienteEntity e = new PacienteEntity();
        e.id = p.getId();
        e.nome = p.getNome();
        e.cpf = p.getCpf().valor();
        e.cns = p.getCns() != null ? p.getCns().valor() : null;
        e.dataNascimento = p.getDataNascimento();
        e.sexo = p.getSexo();
        e.contato = p.getContato();
        e.criadoEm = p.getCriadoEm();
        e.atualizadoEm = p.getAtualizadoEm();
        return e;
    }

    public Paciente toDomain() {
        return new Paciente(id, nome, new CPF(cpf), cns != null ? new CNS(cns) : null,
                dataNascimento, sexo, contato, criadoEm, atualizadoEm);
    }

    // Getters for JPA/queries
    public UUID getId() { return id; }
    public String getCpf() { return cpf; }
}
