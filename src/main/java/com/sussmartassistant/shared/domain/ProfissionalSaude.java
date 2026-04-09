package com.sussmartassistant.shared.domain;

import java.util.UUID;

/**
 * Entidade de domínio ProfissionalSaude — POJO puro, sem dependências de framework.
 */
public class ProfissionalSaude {

    private UUID id;
    private String nome;
    private String registroProfissional;
    private String especialidade;
    private UUID unidadeSaudeId;

    public ProfissionalSaude(UUID id, String nome, String registroProfissional,
                              String especialidade, UUID unidadeSaudeId) {
        this.id = id;
        this.nome = nome;
        this.registroProfissional = registroProfissional;
        this.especialidade = especialidade;
        this.unidadeSaudeId = unidadeSaudeId;
    }

    public static ProfissionalSaude criar(String nome, String registroProfissional,
                                           String especialidade, UUID unidadeSaudeId) {
        return new ProfissionalSaude(UUID.randomUUID(), nome, registroProfissional, especialidade, unidadeSaudeId);
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getRegistroProfissional() { return registroProfissional; }
    public String getEspecialidade() { return especialidade; }
    public UUID getUnidadeSaudeId() { return unidadeSaudeId; }
}
