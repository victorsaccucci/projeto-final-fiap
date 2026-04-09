package com.sussmartassistant.paciente.domain;

import com.sussmartassistant.shared.domain.CNS;
import com.sussmartassistant.shared.domain.CPF;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidade de domínio Paciente — POJO puro, sem dependências de framework.
 */
public class Paciente {

    private UUID id;
    private String nome;
    private CPF cpf;
    private CNS cns;
    private LocalDate dataNascimento;
    private String sexo;
    private String contato;
    private Instant criadoEm;
    private Instant atualizadoEm;

    public Paciente(UUID id, String nome, CPF cpf, CNS cns, LocalDate dataNascimento,
                    String sexo, String contato, Instant criadoEm, Instant atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.cns = cns;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.contato = contato;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Paciente criar(String nome, CPF cpf, CNS cns, LocalDate dataNascimento,
                                  String sexo, String contato) {
        Instant agora = Instant.now();
        return new Paciente(UUID.randomUUID(), nome, cpf, cns, dataNascimento, sexo, contato, agora, agora);
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public CPF getCpf() { return cpf; }
    public CNS getCns() { return cns; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getSexo() { return sexo; }
    public String getContato() { return contato; }
    public Instant getCriadoEm() { return criadoEm; }
    public Instant getAtualizadoEm() { return atualizadoEm; }
}
