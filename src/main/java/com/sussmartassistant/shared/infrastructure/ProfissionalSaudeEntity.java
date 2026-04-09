package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.domain.ProfissionalSaude;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "profissionais_saude")
public class ProfissionalSaudeEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String registroProfissional;

    private String especialidade;

    @Column(nullable = false)
    private UUID unidadeSaudeId;

    protected ProfissionalSaudeEntity() {}

    public static ProfissionalSaudeEntity fromDomain(ProfissionalSaude p) {
        ProfissionalSaudeEntity e = new ProfissionalSaudeEntity();
        e.id = p.getId();
        e.nome = p.getNome();
        e.registroProfissional = p.getRegistroProfissional();
        e.especialidade = p.getEspecialidade();
        e.unidadeSaudeId = p.getUnidadeSaudeId();
        return e;
    }

    public ProfissionalSaude toDomain() {
        return new ProfissionalSaude(id, nome, registroProfissional, especialidade, unidadeSaudeId);
    }

    public UUID getId() { return id; }
    public String getRegistroProfissional() { return registroProfissional; }
}
